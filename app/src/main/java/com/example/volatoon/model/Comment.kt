package com.example.volatoon.model

data class Comment(
    val comment_id: String,
    val userId: String,  // Changed from user_id to match Prisma schema
    val komik_id: String,  // Changed from chapter_id to match Prisma schema
    val content: String,
    val likes: Int = 0,
    val createdAt: String, // Changed to match Prisma schema
    val user: Profile? = null  // Keep the user relationship
)


data class CommentRequest(
    val komik_id: String,  // Changed from chapter_id to match backend
    val content: String
)
data class CommentResponse(
    val status: Int,
    val message: String,
    val data: List<Comment>? = null
)

