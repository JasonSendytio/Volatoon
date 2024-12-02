package com.example.volatoon.model

import com.google.gson.annotations.SerializedName

data class Comment(
    val comment_id: String,
    val userId: String,  // Changed from user_id to match Prisma schema
    val chapter_id: String,
    val content: String,
    val likes: Int = 0,
    val createdAt: String, // Changed to match Prisma schema
    val user: Profile? = null
)


data class CommentRequest(
    val chapter_id: String,
    val content: String,
    @SerializedName("chapterId") // Add this to match the API expectation
    val chapterId: String
)
data class CommentResponse(
    val status: Int,
    val message: String,
    val data: List<Comment>? = null
)

