package com.example.volatoon.model

data class authData(
    val status : Int,
    val token : String,
    val message : String,
)
//
//data class CategoriesList(val categories : List<Category>)
//
data class UpdatePasswordRequest(
    val email: String,
    val newPassword: String
)

data class UserResponse(
    val status: Int,
    val message: String,
    val userData: Profile?
)

data class UpdateUserProfile(
    val fullName: String,
    val userName: String,
    val email: String,
    val status: String
)
