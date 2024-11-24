package com.example.volatoon.model

data class HistoryResponseData(
    val status : Int,
    val message : String,
    val data : List<ComicHistory>? = emptyList(),
)