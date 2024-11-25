package com.example.realestatemanagementsystem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyDao
import com.example.realestatemanagementsystem.appoitnment.Appointment
import com.example.realestatemanagementsystem.appoitnment.AppointmentDao
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.contractor.ContractorDao
import com.example.realestatemanagementsystem.favorites.Favorite
import com.example.realestatemanagementsystem.favorites.FavoriteDao
import com.example.realestatemanagementsystem.image.ImageDao
import com.example.realestatemanagementsystem.image.ImageEntity
import com.example.realestatemanagementsystem.previousworks.PreviousWorks
import com.example.realestatemanagementsystem.previousworks.PreviousWorksDao
import com.example.realestatemanagementsystem.review.Review
import com.example.realestatemanagementsystem.review.ReviewDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao

@Database(entities = [UserProfile::class, Property::class, ImageEntity::class,
    Favorite::class, Contractor::class, PreviousWorks::class, Review::class, Appointment::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun propertyDao(): PropertyDao
    abstract fun imageDao(): ImageDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun contractorDao(): ContractorDao
    abstract fun previousWorksDao(): PreviousWorksDao
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
                    MIGRATION_9_10,MIGRATION_10_11) // Add the migration here
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
                `contact` TEXT NOT NULL,
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
                'contact' TEXT NOT NULL,
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
            INSERT INTO appointment_new (propertyId, ownerEmail,buyerEmail,contact, date)
            SELECT appointmentId,propertyId, ownerEmail,buyerEmail,date
            FROM appointments
        """.trimIndent()
            )

            db.execSQL("DROP TABLE appointments")
            db.execSQL("ALTER TABLE appointment_new RENAME TO appointments")
        }
    }

