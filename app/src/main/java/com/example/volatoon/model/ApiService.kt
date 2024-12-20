package com.example.volatoon.model

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder().baseUrl("https://volatoon.vercel.app/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @POST("api/auth/login")
    suspend fun loginUser(@Body account: Account?): Response<authData>

    @POST("api/auth/register")
    suspend fun registerUser(@Body account: RegisterData?): Response<authData>

    @GET("api/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @POST("api/bookmark")
    suspend fun postBookmark(
        @Body komikId: BookmarkRequest?,
        @Header("Authorization") token: String
    ): BookmarkPostData

    @GET("api/bookmark")
    suspend fun fetchBookmark(@Header("Authorization") token: String): BookmarkResponseData

    @DELETE("api/bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Path("bookmarkId") bookmarkId: String
    ): BookmarkPostData

    // Add these new endpoints
    @Headers("Content-Type: application/json")

    @GET("api/comments/{chapterId}")
    suspend fun getComments(
        @Path("chapterId") chapterId: String,
        @Header("Authorization") token: String
    ): Response<CommentResponse>

    @Headers("Content-Type: application/json")

    @POST("api/comments")
    suspend fun postComment(
        @Body commentRequest: CommentRequest,
        @Header("Authorization") token: String
    ): Response<CommentResponse>

    @Headers("Content-Type: application/json")

    @DELETE("api/comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: String,
        @Header("Authorization") token: String
    ): Response<CommentResponse>

    @Headers("Content-Type: application/json")

    @POST("api/comments/{commentId}/like")
    suspend fun likeComment(
        @Path("commentId") commentId: String,
        @Header("Authorization") token: String
    ): Response<CommentResponse>

    @GET("api/history")
    suspend fun getHistory(@Header("Authorization") token: String): HistoryResponseData

    @POST("api/history")
    suspend fun postHistory(
        @Header("Authorization") token: String,
        @Body komik: HistoryRequest?
    ): HistoryAPIData

    @GET("api/history/{comicId}")
    suspend fun getChapterHistoryByComicId(
        @Header("Authorization") token: String,
        @Path("comicId") comicId: String
    ): HistoryChapterResponse

    @DELETE("api/history/{comicId}")
    suspend fun deleteHistory(
        @Header("Authorization") token: String,
        @Path("comicId") comicId: String
    ): HistoryAPIData

    @POST("api/redeem")
    suspend fun redeemPremium(
        @Header("Authorization") token: String,
        @Body voucherCode: PremiumPost?
    ): PremiumResponse

    @PUT("api/auth/update-password")
    suspend fun updatePassword(
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Response<authData>

    @GET("api/auth/find-user")
    suspend fun findUserByEmail(
        @Query("email") email: String
    ): Response<UserResponse>

    @PUT("api/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body updateUserProfile: UpdateUserProfile?
    ): Response<UpdateUserProfileResponse>

    @PUT("user/status")
    suspend fun updateUserStatus(
        @Header("Authorization") token: String,
        @Body newStatus: String
    ): Response<Any>
}
