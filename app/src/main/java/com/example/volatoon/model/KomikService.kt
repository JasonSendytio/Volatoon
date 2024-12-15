package com.example.volatoon.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder().baseUrl("https://api-otaku.vercel.app/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val comicApiService: ComicApiService = retrofit.create(ComicApiService::class.java)

interface ComicApiService {
    @GET("komik/manga")

    suspend fun getAllManga(
        @Query("page") page : Int = 1,
    ): ComicsList

    @GET("komik/manhua")
    suspend fun getAllManhua(
        @Query("page") page : Int = 1,
    ): ComicsList

    @GET("komik/manhwa")
    suspend fun getAllManhwa(
        @Query("page") page : Int = 1,
    ): ComicsList

    @GET("komik/genres")
    suspend fun getAllGenres() : List<Genres>

    @GET("komik/genre/{genre_id}")
    suspend fun getAllGenre(
        @Path("genre_id") genreId: Int,
        @Query("page") page: Int = 1
    ): ComicsList

    @GET("komik/search")
    suspend fun searchComic(@Query("query") query : String) : ComicsList

    @GET("komik/{komik_id}")
    suspend fun getDetailComic(@Path("komik_id") comicId : String) : DetailComic

    @GET("komik/chapter/{chapter_id}")
    suspend fun getDetailChapter(@Path("chapter_id") chapterId : String) : DetailChapter
    // this code will execute the api and return the data as Categories List data in our previous data class

}