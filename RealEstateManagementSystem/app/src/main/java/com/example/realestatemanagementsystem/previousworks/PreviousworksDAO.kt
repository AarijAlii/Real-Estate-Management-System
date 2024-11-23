package com.example.realestatemanagementsystem.previousworks

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PreviousWorksDao {
    @Query("INSERT INTO previous_works (contractorId, propertyId) VALUES (:contractorId, :propertyId)")
    suspend fun insertPreviousWork(contractorId: Int, propertyId: Int)

    @Query("SELECT * FROM previous_works WHERE contractorId = :contractorId")
    suspend fun getPreviousWorksByContractor(contractorId: Int): List<PreviousWorks>
}
