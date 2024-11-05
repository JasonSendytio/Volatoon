package com.example.volatoon.model

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private val retrofit = Retrofit.Builder().baseUrl("https://api-otaku.vercel.app/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val comicApiService = retrofit.create(ComicApiService::class.java)

interface ComicApiService {
    @GET("komik/manga")
    suspend fun getAllManga(): ComicsList

    @GET("komik/manhua")
    suspend fun getAllManhua(): ComicsList

    @GET("komik/manhwa")
    suspend fun getAllManhwa(): ComicsList

    @GET("komik/{komik_id}")
    suspend fun getDetailComic(@Path("komik_id") comicId : String) : DetailComic

    @GET("komik/chapter/{chapter_id}")
    suspend fun getDetailChapter(@Path("chapter_id") chapterId : String) : DetailChapter
    // this code will execute the api and return the data as Categories List data in our previous data class
}