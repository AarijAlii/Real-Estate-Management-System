package com.example.realestatemanagementsystem.appoitnment

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Query("INSERT INTO appointments (propertyId, ownerEmail, buyerEmail, contact,date) VALUES (:propertyId, :ownerEmail, :buyerEmail,:contact, :date)")
    suspend fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String, contact:String,date: String)

    @Query("SELECT * FROM appointments WHERE buyerEmail = :buyerEmail")
    fun getAppointmentsByBuyer(buyerEmail: String): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE ownerEmail = :ownerEmail")
    fun getAppointmentsByOwner(ownerEmail: String): Flow<List<Appointment>>

    @Query("DELETE FROM appointments WHERE appointmentId = :appointmentId")
    suspend fun deleteAppointment(appointmentId: Int)

}
