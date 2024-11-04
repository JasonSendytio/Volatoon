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

data class ComicsList(val data : List<Comic>)

