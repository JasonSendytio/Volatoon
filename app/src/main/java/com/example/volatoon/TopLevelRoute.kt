package com.example.volatoon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute(val route : String, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home" , Icons.Default.Home),
    TopLevelRoute("Trending", Icons.Default.Info),
    TopLevelRoute("Search", Icons.Default.Search),
    TopLevelRoute("Notifications", Icons.Default.Notifications),
    TopLevelRoute("Profile", Icons.Default.Person)
)