package com.example.volatoon.model

data class BookmarkResponseData(
    val status : Int,
    val message : String,
    val data : List<ComicBookmark>,
)

data class BookmarkPostData(
    val status : Int,
    val message : String,
    val data : ComicBookmark,
)

data class BookmarkRequest(val komikId: String?)