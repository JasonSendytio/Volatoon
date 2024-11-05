package com.example.volatoon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comic(
    val title : String,
    val image : String,
    val chapter : String,
    val score : String,
    val type : String,
    val komik_id : String
) : Parcelable

@Parcelize
data class Chapter(
    val title : String,
    val date : String,
    val chapter_id : String,
) : Parcelable

@Parcelize
data class DetailChapter(
    val title : String,
    val komik_id : String,
    val prev_chapter_id : String,
    val next_chapter_id : String,
    val images : List<String>,
) : Parcelable

@Parcelize
data class DetailComic(
    val title : String,
    val alternativeTitle : String,
    val image : String,
    val chapter : String,
    val score : String,
    val synopsis : String,
    val status : String,
    val type : String,
    val released : String,
    val author : String,
    val genres : List<String>,
    val chapterList : List<Chapter> = emptyList()
) : Parcelable



data class ComicsList(val data : List<Comic>)

