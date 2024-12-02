package com.example.realestatemanagementsystem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.realestatemanagementsystem.Appointment.Appointment
import com.example.realestatemanagementsystem.Appointment.AppointmentDao
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyDao
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.contractor.ContractorDao
import com.example.realestatemanagementsystem.favorites.Favorite
import com.example.realestatemanagementsystem.favorites.FavoriteDao
import com.example.realestatemanagementsystem.image.ImageDao
import com.example.realestatemanagementsystem.image.ImageEntity
import com.example.realestatemanagementsystem.review.Review
import com.example.realestatemanagementsystem.review.ReviewDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao

@Database(entities = [UserProfile::class, Property::class, ImageEntity::class,
    Favorite::class, Contractor::class,  Review::class, Appointment::class], version = 16)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun propertyDao(): PropertyDao
    abstract fun imageDao(): ImageDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun contractorDao(): ContractorDao
    abstract fun reviewDao(): ReviewDao
    abstract fun appointmentDao(): AppointmentDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ) .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,MIGRATION_4_5,
                    MIGRATION_5_4,MIGRATION_5_6, MIGRATION_6_7,MIGRATION_7_8, MIGRATION_8_9,
                    MIGRATION_9_10,MIGRATION_10_11, MIGRATION_11_12,
                    MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15

                    ,MIGRATION_15_16
                )
                     // Add the migration here
                    .build()
                INSTANCE = db
                db
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new table with the updated schema
        db.execSQL(
            """
            CREATE TABLE user_profile_new (
                email TEXT NOT NULL PRIMARY KEY,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                contact TEXT NOT NULL,
                city TEXT NOT NULL,
                region TEXT NOT NULL,
                postalCode INT NOT NULL,
                rating INT NOT NULL
            )
            """.trimIndent()
        )
        // Copy data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO user_profile_new (email, firstName, lastName, contact, city, region, postalCode)
            SELECT email, firstName, lastName, contact, city, region, postalCode, rating FROM user_profile
            """.trimIndent()
        )
        // Drop and rename to the original table name
        db.execSQL("DROP TABLE user_profile")
        db.execSQL("ALTER TABLE user_profile_new RENAME TO user_profile")
    }
}

//PROPERTY TABLE MIGRATION
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `property` (
                `propertyId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `city` TEXT NOT NULL, 
                `state` TEXT NOT NULL, 
                `propertyNumber` TEXT NOT NULL, 
                `rooms` INTEGER NOT NULL, 
                `bedrooms` INTEGER NOT NULL, 
                `garage` INTEGER NOT NULL, 
                `area` REAL NOT NULL, 
                `type` TEXT NOT NULL, 
                `price` REAL NOT NULL, 
                `zipCode` TEXT NOT NULL, 
                `email` TEXT NOT NULL, 
                `isSold` INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(`email`) REFERENCES `user_profile`(`email`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Ensure property_images table is created
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `property_images` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `propertyId` INTEGER NOT NULL, 
                `imageUrl` TEXT NOT NULL,
                FOREIGN KEY(`propertyId`) REFERENCES `property`(`propertyId`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // This is for upgrading from version 4 to 5
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `favorites` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `email` TEXT NOT NULL, 
                `propertyId` INTEGER NOT NULL, 
                FOREIGN KEY(`email`) REFERENCES `user_profile`(`email`) ON DELETE CASCADE,
                FOREIGN KEY(`propertyId`) REFERENCES `property`(`propertyId`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}


val MIGRATION_5_4 = object : Migration(5, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Drop the 'favorites' table when downgrading
        db.execSQL("DROP TABLE IF EXISTS `favorites`")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `contractor` (
                `contractorId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `email` TEXT NOT NULL,
                `experience` TEXT NOT NULL,
                `rate` TEXT NOT NULL,
                `speciality` TEXT NOT NULL,
                `overallRating` REAL NOT NULL DEFAULT 0.0,
                `imageUrl` TEXT NOT NULL,
                FOREIGN KEY(`email`) REFERENCES `user_profile`(`email`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `previous_works` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `contractorId` INTEGER NOT NULL,
                `propertyId` INTEGER NOT NULL,
                FOREIGN KEY(`contractorId`) REFERENCES `contractor`(`contractorId`) ON DELETE CASCADE,
                FOREIGN KEY(`propertyId`) REFERENCES `property`(`propertyId`) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `review` (
                `reviewId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `contractorId` INTEGER NOT NULL,
                `propertyId` INTEGER NOT NULL,
                `email` TEXT NOT NULL,
                `rating` INTEGER NOT NULL,
                `comment` TEXT NOT NULL,
                FOREIGN KEY(`contractorId`) REFERENCES `contractor`(`contractorId`) ON DELETE CASCADE,
                FOREIGN KEY(`propertyId`) REFERENCES `property`(`propertyId`) ON DELETE CASCADE,
                FOREIGN KEY(`email`) REFERENCES `user_profile`(`email`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create a new table with the updated schema (without the imageUrl column)
        db.execSQL(
            """
            CREATE TABLE contractor_new (
                contractorId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                email TEXT NOT NULL,
                experience TEXT NOT NULL,
                contact TEXT NOT NULL,
                speciality TEXT NOT NULL,
                overallRating REAL NOT NULL,
                FOREIGN KEY(email) REFERENCES user_profile(email) ON DELETE CASCADE
            )
        """
        )

        db.execSQL(
            """
            INSERT INTO contractor_new (contractorId, email, experience, contact, speciality, overallRating)
            SELECT contractorId, email, experience, contact, speciality, overallRating
            FROM contractor
        """.trimIndent()
        )

        db.execSQL("DROP TABLE contractor")
        db.execSQL("ALTER TABLE contractor_new RENAME TO contractor")
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create a new table with the updated schema (without the imageUrl column)
        db.execSQL(
            """
            CREATE TABLE review_new (
                `reviewId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `contractorId` INTEGER NOT NULL,
                `email` TEXT NOT NULL,
                `rating` INTEGER NOT NULL,
                `comment` TEXT NOT NULL,
                FOREIGN KEY(`contractorId`) REFERENCES `contractor`(`contractorId`) ON DELETE CASCADE,
                FOREIGN KEY(`email`) REFERENCES `user_profile`(`email`) ON DELETE CASCADE
            )
            """
        )

        db.execSQL(
            """
            INSERT INTO review_new (reviewId,contractorId, email,rating,comment)
            SELECT reviewId, contractorId, email, rating,comment
            FROM review
        """.trimIndent()
        )

        db.execSQL("DROP TABLE review")
        db.execSQL("ALTER TABLE review_new RENAME TO review")
    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS 'appointments' (
                'appointmentId' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                'propertyId' INTEGER NOT NULL,
                'ownerEmail' TEXT NOT NULL,
                'buyerEmail' TEXT NOT NULL,
                'date' TEXT NOT NULL,
                FOREIGN KEY ('propertyId') REFERENCES 'property' ('propertyId') ON DELETE CASCADE,
                FOREIGN KEY ('ownerEmail') REFERENCES 'user_profile' ('email') ON DELETE CASCADE,
                FOREIGN KEY ('buyerEmail') REFERENCES 'user_profile' ('email') ON DELETE CASCADE
            )
        """.trimIndent()
        )
    }
}
val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_profile_contact ON user_profile(contact)")
        db.execSQL(
            """
            CREATE TABLE appointment_new (
               'appointmentId' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                'propertyId' INTEGER NOT NULL,
                'ownerEmail' TEXT NOT NULL,
                'buyerEmail' TEXT NOT NULL,
                
                'date' TEXT NOT NULL,
                FOREIGN KEY ('propertyId') REFERENCES 'property' ('propertyId') ON DELETE CASCADE,
                FOREIGN KEY ('ownerEmail') REFERENCES 'user_profile' ('email') ON DELETE CASCADE,
                FOREIGN KEY ('buyerEmail') REFERENCES 'user_profile' ('email') ON DELETE CASCADE,
                FOREIGN KEY ('contact') REFERENCES 'user_profile' ('contact') ON DELETE CASCADE
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO appointment_new (propertyId, ownerEmail,buyerEmail, date)
            SELECT appointmentId,propertyId, ownerEmail,buyerEmail,date
            FROM appointments
        """.trimIndent()
        )

        db.execSQL("DROP TABLE appointments")
        db.execSQL("ALTER TABLE appointment_new RENAME TO appointments")
    }

}
val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new table with the added 'imageUrl' column
        db.execSQL(
            """
            CREATE TABLE user_profile_new (
                email TEXT NOT NULL PRIMARY KEY,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                contact TEXT NOT NULL UNIQUE,
                city TEXT NOT NULL,
                region TEXT NOT NULL,
                postalCode TEXT NOT NULL,
                rating INTEGER NOT NULL DEFAULT 0,
                imageUrl TEXT NOT NULL DEFAULT ''
            )
            """.trimIndent()
        )

        // Copy data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO user_profile_new (email, firstName, lastName, contact, city, region, postalCode, rating)
            SELECT email, firstName, lastName, contact, city, region, postalCode, rating
            FROM user_profile
            """.trimIndent()
        )

        // Drop the old table
        db.execSQL("DROP TABLE user_profile")

        // Rename the new table to the original table name
        db.execSQL("ALTER TABLE user_profile_new RENAME TO user_profile")

        // Recreate the unique index
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_profile_contact ON user_profile(contact)")
    }
}




val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create the new table without the `rating` column
        db.execSQL(
            """
            CREATE TABLE user_profile_new (
                email TEXT NOT NULL PRIMARY KEY,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                contact TEXT NOT NULL UNIQUE,
                city TEXT NOT NULL,
                region TEXT NOT NULL,
                postalCode TEXT NOT NULL,
                imageUrl TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Step 2: Copy data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO user_profile_new (email, firstName, lastName, contact, city, region, postalCode, imageUrl)
            SELECT email, firstName, lastName, contact, city, region, postalCode, imageUrl
            FROM user_profile
            """.trimIndent()
        )

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE user_profile")

        // Step 4: Rename the new table to the original name
        db.execSQL("ALTER TABLE user_profile_new RENAME TO user_profile")
    }
}

val MIGRATION_13_14 = object : Migration(13, 14) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create a new table with the updated schema
        db.execSQL(
            """
            CREATE TABLE property_new (
                propertyId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                city TEXT NOT NULL,
                state TEXT NOT NULL,
                propertyNumber TEXT NOT NULL,
                bathrooms INTEGER NOT NULL, -- Renamed column
                bedrooms INTEGER NOT NULL,
                garage INTEGER NOT NULL,
                area REAL NOT NULL,
                type TEXT NOT NULL,
                price REAL NOT NULL,
                zipCode TEXT NOT NULL,
                email TEXT NOT NULL,
                isSold INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(email) REFERENCES user_profile(email) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        // Step 2: Copy data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO property_new (
                propertyId, city, state, propertyNumber, bathrooms, bedrooms, garage, area, type, price, zipCode, email, isSold
            )
            SELECT propertyId, city, state, propertyNumber, rooms, bedrooms, garage, area, type, price, zipCode, email, isSold
            FROM property
            """.trimIndent()
        )

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE property")

        // Step 4: Rename the new table to the original table name
        db.execSQL("ALTER TABLE property_new RENAME TO property")
    }
}

val MIGRATION_14_15 = object : Migration(14, 15) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Drop the `previous_works` table
        db.execSQL("DROP TABLE IF EXISTS previous_works")
    }
}

val MIGRATION_15_16 = object : Migration(15, 16) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the ChangeLog table
        db.execSQL(
            """
            CREATE TABLE ChangeLog (
                log_id INTEGER PRIMARY KEY AUTOINCREMENT,
                table_name TEXT NOT NULL,
                operation TEXT NOT NULL,  -- INSERT, UPDATE, DELETE
                column_name TEXT,  -- Which column was changed
                old_value TEXT,    -- Old value for UPDATE/DELETE operations
                new_value TEXT,    -- New value for INSERT/UPDATE operations
                changed_by TEXT,   -- Email or user who made the change
                change_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """
        )
        db.execSQL(
            """
            CREATE TRIGGER user_profile_insert_trigger
            AFTER INSERT ON user_profile
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('user_profile', 'INSERT', 'email', NEW.email, NEW.email);
            END;
            """
        )

        // Trigger for UPDATE on user_profile
        db.execSQL(
            """
            CREATE TRIGGER user_profile_update_trigger
            AFTER UPDATE ON user_profile
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, new_value, changed_by)
                VALUES 
                ('user_profile', 'UPDATE', 'firstName', OLD.firstName, NEW.firstName, NEW.email),
                ('user_profile', 'UPDATE', 'lastName', OLD.lastName, NEW.lastName, NEW.email),
                ('user_profile', 'UPDATE', 'contact', OLD.contact, NEW.contact, NEW.email),
                ('user_profile', 'UPDATE', 'city', OLD.city, NEW.city, NEW.email),
                ('user_profile', 'UPDATE', 'region', OLD.region, NEW.region, NEW.email),
                ('user_profile', 'UPDATE', 'postalCode', OLD.postalCode, NEW.postalCode, NEW.email),
                ('user_profile', 'UPDATE', 'rating', OLD.rating, NEW.rating, NEW.email),
                ('user_profile', 'UPDATE', 'imageUrl', OLD.imageUrl, NEW.imageUrl, NEW.email);
            END;
            """
        )

        // Trigger for DELETE on user_profile
        db.execSQL(
            """
            CREATE TRIGGER user_profile_delete_trigger
            AFTER DELETE ON user_profile
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('user_profile', 'DELETE', 'email', OLD.email, OLD.email);
            END;
            """
        )
        db.execSQL(
            """
            CREATE TRIGGER review_insert_trigger
            AFTER INSERT ON review
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('review', 'INSERT', 'reviewId', NEW.reviewId, NEW.email);
            END;
            """
        )


        // Trigger for INSERT on favorites
        db.execSQL(
            """
            CREATE TRIGGER favorites_insert_trigger
            AFTER INSERT ON favorites
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('favorites', 'INSERT', 'propertyId', NEW.propertyId, NEW.email);
            END;
            """
        )

        // Trigger for DELETE on favorites
        db.execSQL(
            """
            CREATE TRIGGER favorites_delete_trigger
            AFTER DELETE ON favorites
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('favorites', 'DELETE', 'propertyId', OLD.propertyId, OLD.email);
            END;
            """
        )
        // Trigger for INSERT on contractor
        db.execSQL(
            """
            CREATE TRIGGER contractor_insert_trigger
            AFTER INSERT ON contractor
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('contractor', 'INSERT', 'contractorId', NEW.contractorId, NEW.email);
            END;
            """
        )

        // Trigger for DELETE on contractor
        db.execSQL(
            """
            CREATE TRIGGER contractor_delete_trigger
            AFTER DELETE ON contractor
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('contractor', 'DELETE', 'contractorId', OLD.contractorId, OLD.email);
            END;
            """
        )
        // Trigger for INSERT on property_images
        db.execSQL(
            """
            CREATE TRIGGER property_images_insert_trigger
            AFTER INSERT ON property_images
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('property_images', 'INSERT', 'propertyId', NEW.propertyId, NULL);
            END;
            """
        )

        // Trigger for DELETE on property_images
        db.execSQL(
            """
            CREATE TRIGGER property_images_delete_trigger
            AFTER DELETE ON property_images
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('property_images', 'DELETE', 'propertyId', OLD.propertyId, NULL);
            END;
            """
        )
        db.execSQL(
            """
            CREATE TRIGGER appointments_insert_trigger
            AFTER INSERT ON appointments
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('appointments', 'INSERT', 'appointmentId', NEW.appointmentId, NEW.ownerEmail);
            END;
            """
        )

        // Trigger for DELETE on appointments
        db.execSQL(
            """
            CREATE TRIGGER appointments_delete_trigger
            AFTER DELETE ON appointments
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('appointments', 'DELETE', 'appointmentId', OLD.appointmentId, OLD.ownerEmail);
            END;
            """
        )
        db.execSQL(
            """
            CREATE TRIGGER property_insert_trigger
            AFTER INSERT ON property
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, new_value, changed_by)
                VALUES ('property', 'INSERT', 'propertyId', NEW.propertyId, NEW.email);
            END;
            """
        )

        // Create a trigger for DELETE on property table (for auditing purposes, even with cascading deletes)
        db.execSQL(
            """
            CREATE TRIGGER property_delete_trigger
            AFTER DELETE ON property
            FOR EACH ROW
            BEGIN
                INSERT INTO ChangeLog (table_name, operation, column_name, old_value, changed_by)
                VALUES ('property', 'DELETE', 'propertyId', OLD.propertyId, OLD.email);
            END;
            """
        )
    }
}