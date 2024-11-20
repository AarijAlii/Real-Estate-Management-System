package com.example.realestatemanagementsystem.user.UserProfile

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyDao
import com.example.realestatemanagementsystem.image.ImageDao
import com.example.realestatemanagementsystem.image.ImageEntity

@Database(entities = [UserProfile::class, Property::class, ImageEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun propertyDao(): PropertyDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ) .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // Add the migration here
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
