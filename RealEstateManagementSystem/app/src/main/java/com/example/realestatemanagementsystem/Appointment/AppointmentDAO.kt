package com.example.realestatemanagementsystem.Appointment

import androidx.room.Dao
import androidx.room.Query
import com.example.realestatemanagementsystem.Appointment.AppointmentDetails
import com.example.realestatemanagementsystem.Appointment.AppointmentDetailsSeller

@Dao
interface AppointmentDao {

    @Query("INSERT INTO appointments (propertyId, ownerEmail, buyerEmail,date) VALUES (:propertyId, :ownerEmail, :buyerEmail, :date)")
    suspend fun insertAppointment(propertyId: Int, ownerEmail: String, buyerEmail: String,date: String)

    @Query("SELECT * FROM appointments WHERE buyerEmail = :buyerEmail")
    suspend fun getAppointmentsByBuyer(buyerEmail: String): List<Appointment>
    // For Buyer
    @Query("""
        SELECT a.appointmentId, a.date, a.propertyId, p.propertyNumber, 
            u1.firstName || ' ' || u1.lastName AS ownerName, u1.contact AS ownerContact, 
            u2.firstName || ' ' || u2.lastName AS buyerName, u2.contact AS buyerContact,
            a.ownerEmail
        FROM appointments a
        INNER JOIN property p ON a.propertyId = p.propertyId
        INNER JOIN user_profile u1 ON a.ownerEmail = u1.email
        INNER JOIN user_profile u2 ON a.buyerEmail = u2.email
        WHERE a.buyerEmail = :buyerEmail
    """)
   suspend fun getAppointmentsForBuyer(buyerEmail: String): List<AppointmentDetails>

    // For Owner
    @Query("""
        SELECT a.appointmentId, a.date, a.propertyId, p.propertyNumber, 
          u1.firstName || ' ' || u1.lastName AS ownerName, u1.contact AS ownerContact, 
            u2.firstName || ' ' || u2.lastName AS buyerName, u2.contact AS buyerContact,
            a.buyerEmail
        FROM appointments  a
        INNER JOIN property p ON a.propertyId = p.propertyId
        INNER JOIN user_profile u1 ON a.ownerEmail = u1.email
        INNER JOIN user_profile u2 ON a.buyerEmail = u2.email
        WHERE a.ownerEmail = :ownerEmail
    """)
    suspend fun getAppointmentsForOwner(ownerEmail: String): List<AppointmentDetailsSeller>

    @Query("SELECT * FROM appointments WHERE ownerEmail = :ownerEmail")
    suspend fun getAppointmentsByOwner(ownerEmail: String): List<Appointment>

    @Query("DELETE FROM appointments WHERE appointmentId = :appointmentId")
    suspend fun deleteAppointment(appointmentId: Int)


}
