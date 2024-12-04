package com.example.volatoon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelRoute(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Dashboard : TopLevelRoute("dashboard", Icons.Default.Home, "Home")
    object Trending : TopLevelRoute("trending", Icons.Default.Info, "Trending")
    object Search : TopLevelRoute("search", Icons.Default.Search, "Search")
    object UserActivity : TopLevelRoute("activity", Icons.Default.Favorite, "Activity") // Changed from "Notification" to "Notifications"
    object Profile : TopLevelRoute("profile", Icons.Default.Person, "Profile")

    @Composable
    fun LabelText() {
        Text(
            text = label,
            fontSize = if (label.length > 12) 10.sp else 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
