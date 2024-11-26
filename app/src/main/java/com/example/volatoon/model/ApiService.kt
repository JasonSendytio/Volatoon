package com.example.volatoon.model

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


private val retrofit = Retrofit.Builder().baseUrl("https://volatoon.vercel.app/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @POST("api/auth/login")
    suspend fun loginUser(@Body account: Account?): Response<authData>

    @POST("api/auth/register")
    suspend fun registerUser(@Body account: RegisterData?): Response<authData>

    @GET("api/profile")
    suspend fun getProfile(@Header("Authorization") token: String) : Response<ProfileResponse>

    @POST("api/bookmark")
    suspend fun postBookmark(
        @Header("Authorization") token: String,
        @Body comicId: String?
    ): bookmarkResponseData

    @GET("api/bookmark")
    suspend fun fetchBookmark(@Header("Authorization") token: String): bookmarkResponseData

    @DELETE("api/bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Path("chapter_id") chapterId : String
    ) : bookmarkResponseData

//    @POST("api/auth/register")
//    suspend fun registerUser(@Body user: User?): Response<authData>
    // this code will execute the api and return the data as Categories List data in our previous data class
}