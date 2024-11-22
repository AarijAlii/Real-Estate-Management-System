package com.example.realestatemanagementsystem.contractor

import androidx.room.Embedded
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile

data class ContractorWithUserProfile(
    @Embedded val contractor: Contractor,
    @Embedded val userProfile: UserProfile
)
