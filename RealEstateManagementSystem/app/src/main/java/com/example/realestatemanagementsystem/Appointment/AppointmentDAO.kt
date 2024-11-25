package com.example.realestatemanagementsystem.Appointment

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AppointmentDao {

    @Query("INSERT INTO appointments (propertyId, ownerEmail, buyerEmail, date) VALUES (:propertyId, :ownerEmail, :buyerEmail, :date)")
    suspend fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String, date: String)

    @Query("SELECT * FROM appointments WHERE buyerEmail = :buyerEmail")
   suspend fun getAppointmentsByBuyer(buyerEmail: String):List<Appointment>

    @Query("SELECT * FROM appointments WHERE ownerEmail = :ownerEmail")
    suspend fun getAppointmentsByOwner(ownerEmail: String): List<Appointment>

    @Query("DELETE FROM appointments WHERE appointmentId = :appointmentId")
    suspend fun deleteAppointment(appointmentId: Int)

}
