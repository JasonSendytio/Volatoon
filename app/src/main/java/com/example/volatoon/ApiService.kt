package com.example.volatoon

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


private val retrofit = Retrofit.Builder().baseUrl("https://bread-finance-api.vercel.app/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @POST("api/auth/login")
    suspend fun loginUser(@Body user: User?): Response<authData>
    // this code will execute the api and return the data as Categories List data in our previous data class
}