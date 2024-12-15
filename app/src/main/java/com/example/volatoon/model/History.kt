package com.example.volatoon.model

data class HistoryResponseData(
    val status: Int,
    val message: String,
    val data: List<ComicHistory>,
)

data class HistoryAPIData (
    val status: Int,
    val message: String,
    val data: ComicHistory,
)

data class HistoryRequest(
    val komikId: String,
    val chapterId: String
)