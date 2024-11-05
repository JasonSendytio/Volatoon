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
    val chapterList : List<Chapter>
) : Parcelable

data class ComicsList(val data : List<Comic>)

