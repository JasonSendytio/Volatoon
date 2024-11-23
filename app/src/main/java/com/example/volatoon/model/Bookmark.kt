package com.example.volatoon.model

data class bookmarkResponseData(
    val status : Int,
    val message : String,
    val data : List<ComicBookmark>? = emptyList(),
)