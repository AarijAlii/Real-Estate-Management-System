package com.example.realestatemanagementsystem.contractor

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ContractorDao {

    @Query("INSERT INTO contractor (email, experience, contact, speciality, overallRating) VALUES (:email, :experience, :contact, :speciality, :overallRating)")
    suspend fun insertContractor(
        email: String,
        experience: String,
        contact: String,
        speciality: String,
        overallRating: Float = 0.0f
    )

    @Query("SELECT * FROM contractor WHERE contractorId = :id")
    suspend fun getContractorById(id: Int): Contractor

    @Query("SELECT * FROM contractor WHERE email = :email")
    suspend fun getContractorByEmail(email: String): Contractor

    @Query("SELECT * FROM contractor")
    suspend fun getAllContractors(): List<Contractor>

    @Query("DELETE FROM contractor WHERE contractorId = :id")
    suspend fun deleteContractor(id: Int)

    @Query("""
        UPDATE contractor 
        SET experience = :experience, 
            contact = :contact, 
            speciality = :speciality
        WHERE contractorId = :id
    """)
    suspend fun updateContractor(id: Int, experience: String, contact: String, speciality: String)


    @Query("""
        SELECT contractor.contractorId, contractor.email, contractor.experience, contractor.contact, contractor.speciality, contractor.overallRating,
               user_profile.firstName, user_profile.lastName
        FROM contractor
        INNER JOIN user_profile
        ON contractor.email = user_profile.email
    """)
    suspend fun getAllContractorDetails(): List<ContractorWithUserProfile>

    @Query("UPDATE contractor SET overallRating = :newRating WHERE contractorId = :contractorId")
    suspend fun updateOverallRating(contractorId: Int, newRating: Float)
}