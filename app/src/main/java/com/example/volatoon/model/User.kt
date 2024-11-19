package com.example.volatoon.model

data class Account(
    val email : String,
    val password : String
)

data class Auth(
    val authToken : String
)

data class Profile(
    val fullName : String,
    val userName : String,
    val email : String
)

data class ProfileResponse(
    val status : Int,
    val message : String,
    val userData : Profile
)