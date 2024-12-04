package com.example.volatoon.model

data class BookmarkResponseData(
    val status : Int,
    val message : String,
    val data : List<ComicBookmark>,
)