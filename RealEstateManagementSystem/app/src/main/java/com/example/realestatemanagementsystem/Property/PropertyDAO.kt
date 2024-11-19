package com.example.realestatemanagementsystem.Property



import androidx.room.*


@Dao
interface PropertyDao {

    @Insert
    suspend fun addProperty(property: Property)

    @Update
    suspend fun updateProperty(property: Property)

    @Delete
    suspend fun deleteProperty(property: Property)

    @Query("SELECT * FROM property WHERE email = :email AND isSold = 0")
    suspend fun getCurrentListings(email: String): List<Property>

    @Query("SELECT * FROM property WHERE email = :email AND isSold = 1")
    suspend fun getSoldListings(email: String): List<Property>

    @Query("UPDATE property SET isSold = 1 WHERE propertyId = :propertyId")
    suspend fun markPropertyAsSold(propertyId: Int)


    @Query("""
        INSERT INTO property (
            city, state, propertyNumber, rooms, bedrooms, garage, area, type, price, zipCode, email, isSold
        ) VALUES (
            :city, :state, :propertyNumber, :rooms, :bedrooms, :garage, :area, :type, :price, :zipCode, :email, :isSold
        )
    """)
    suspend fun adddProperty(
        city: String,
        state: String,
        propertyNumber: String,
        rooms: Int,
        bedrooms: Int,
        garage: Int,
        area: Double,
        type: String,
        price: Double,
        zipCode: String,
        email: String,
        isSold: Boolean = false
    )

    @Query("""
        UPDATE property 
        SET city = :city, state = :state, propertyNumber = :propertyNumber, rooms = :rooms, bedrooms = :bedrooms, 
            garage = :garage, area = :area, type = :type, price = :price, zipCode = :zipCode, isSold = :isSold
        WHERE propertyId = :propertyId
    """)
    suspend fun updateeProperty(
        propertyId: Int,
        city: String,
        state: String,
        propertyNumber: String,
        rooms: Int,
        bedrooms: Int,
        garage: Int,
        area: Double,
        type: String,
        price: Double,
        zipCode: String,
        isSold: Boolean
    )

    @Query("DELETE FROM property WHERE propertyId = :propertyId")
    suspend fun deleteeProperty(propertyId: Int)

}

