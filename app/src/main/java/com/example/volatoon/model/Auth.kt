package com.example.volatoon.model

data class authData(
    val status : Int,
    val token : String,
    val message : String,
)

data class UpdatePasswordRequest(
    val email: String,
    val newPassword: String
)

data class UserResponse(
    val status: Int,
    val message: String,
    val userData: String
)

data class UpdateUserProfile(
    val name: String?,
    val username: String?,
    val status: String?
)

data class UpdateUserProfileResponse(
    val status: Int,
    val message: String,
    val data: Profile
)
