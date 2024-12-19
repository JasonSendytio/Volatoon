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
    val chapter_id: String,
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

@Parcelize
data class Genres(
    val name : String,
    val genre_id : String
) : Parcelable

@Parcelize
data class Genre(
    val genre_id : Int,
    val page : Int,
    val order : String,
) : Parcelable

data class ComicBookmark(
    val bookmark_id : String,
    val userId : String,
    val komik_id: String,
    val createdAt : String,
    val comicDetails : DetailComic
)

data class ComicHistory(
    val history_id : String,
    val userId : String,
    val komik_id: String,
    val createdAt : String,
    val chapter_id : String,
    val comicDetails: DetailComic
)



data class GenresList(val genres: List<Genres>)// The one that matches your model

data class ComicsList(
    val data : List<Comic>,
    val prevPage: Boolean,
    val nextPage: Boolean
)
