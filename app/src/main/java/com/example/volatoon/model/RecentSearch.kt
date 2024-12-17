package com.example.volatoon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecentSearch(
    val id: Int = 0,
    val searchText: String,
    val timestamp: Long = System.currentTimeMillis()
): Parcelable