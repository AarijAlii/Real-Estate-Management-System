package com.example.realestatemanagementsystem.Property



import androidx.room.*
import com.example.realestatemanagementsystem.image.ImageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PropertyDao {


    @Insert
    suspend fun insertProperty(property: Property): Long

    // Update property
    @Update
    suspend fun updateProperty(property: Property)

    // Delete property
    @Delete
    suspend fun deleteProperty(property: Property)

    // Get properties by user email
    @Query("SELECT * FROM property WHERE email = :email")
    suspend fun getPropertiesByUser(email: String): List<Property>

    @Query("SELECT * FROM property WHERE email = :email AND isSold = 0")
    suspend fun getCurrentListings(email: String): List<Property>

    @Query("SELECT * FROM property where isSold = 0")
    suspend fun getAllBuyingProperties(): List<Property>

    @Query("SELECT * FROM property WHERE email = :email AND isSold = 1")
    suspend fun getSoldListings(email: String): List<Property>

    @Query("UPDATE property SET isSold = 1 WHERE propertyId = :propertyId")
    suspend fun markPropertyAsSold(propertyId: Int)

    @Query("SELECT * FROM property WHERE propertyId = :propertyId")
    suspend fun getPropertyById(propertyId: Int): Property?

    @Query("SELECT * FROM property Where isSold=0 ORDER BY price ASC")
    suspend fun getPropertiesByPriceAsc(): List<Property>

    @Query("SELECT * FROM property Where isSold=0 ORDER BY price DESC")
    suspend fun getPropertiesByPriceDesc(): List<Property>

    @Query(
        """
        INSERT INTO property (
            city, state, propertyNumber, rooms, bedrooms, garage, area, type, price, zipCode, email, isSold
        ) VALUES (
            :city, :state, :propertyNumber, :rooms, :bedrooms, :garage, :area, :type, :price, :zipCode, :email, :isSold
        )
    """
    )
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
    ): Long

    @Query(
        """
        UPDATE property 
        SET city = :city, state = :state, propertyNumber = :propertyNumber, rooms = :rooms, bedrooms = :bedrooms, 
            garage = :garage, area = :area, type = :type, price = :price, zipCode = :zipCode, isSold = :isSold
        WHERE propertyId = :propertyId
    """
    )
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

    @Query(
        """SELECT * FROM property WHERE 
            (:city IS NULL OR city LIKE :city) AND
            (:state IS NULL OR state LIKE :state) AND
            (:minPrice IS NULL OR price >= :minPrice) AND
            (:maxPrice IS NULL OR price <= :maxPrice) AND
            (:zipCode IS NULL OR zipcode LIKE :zipCode) AND
            (:type IS NULL OR type LIKE :type) AND
            (:noOfRooms IS NULL OR rooms = :noOfRooms) AND
            (:bedrooms IS NULL OR bedrooms = :bedrooms) AND
            (:garage IS NULL OR garage = :garage) AND
            (isSold = 0)
       """
    )
    fun filterProperties(
        city: String?,
        state: String?,
        minPrice: Double?,
        maxPrice: Double?,
        zipCode: String?,
        type: String?,
        noOfRooms: Int?,
        bedrooms: Int?,
        garage: Boolean?,

        ): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE propertyId = :propertyId")
    fun searchByPropertyId(propertyId: Int): Flow<Property?>

    @Query("SELECT * FROM property WHERE email LIKE :userEmail")
    fun searchByEmail(userEmail: String): Flow<List<Property>>


    @Insert
    suspend fun insertImages(images: List<ImageEntity>)
}
