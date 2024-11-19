package com.example.volatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.utils.DataStoreManager.Companion.USER_ID
import com.example.volatoon.utils.USER_DATASTORE
import com.example.volatoon.view.DashboardScreen
import com.example.volatoon.view.DetailChapterScreen
import com.example.volatoon.view.DetailComicScreen
import com.example.volatoon.view.DetailGenreScreen
import com.example.volatoon.view.LoginScreen
import com.example.volatoon.view.NotificationsScreen
import com.example.volatoon.view.ProfileScreen
import com.example.volatoon.view.RegisterScreen
import com.example.volatoon.view.SearchScreen
import com.example.volatoon.view.TrendingScreen
import com.example.volatoon.viewmodel.ComicViewModel
import com.example.volatoon.viewmodel.GenreViewModel
import com.example.volatoon.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.first

@Composable
fun VolatoonApp(
    navController: NavHostController,
    preferenceDataStore : DataStore<Preferences>,
    dataStoreManager: DataStoreManager
){
    var isLogin by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val onLoginSuccess = {isLogin = true}

    LaunchedEffect (key1 = Unit){
        checkRegisterState(preferenceDataStore){it->
            isLogin = it
        }
    }

    val comicViewModel : ComicViewModel = viewModel()
    val viewState by comicViewModel.comicstate

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            NavHost(
                navController = navController,
                startDestination = if (isLogin) TopLevelRoute.Dashboard.route else "login"
            ){
                composable(
                    route = "register",
                ) {
                   RegisterScreen()
                }

                composable(
                    route = "login",
                ) {
                    LoginScreen(navigateToDashboard = {
                        navController.navigate(route = TopLevelRoute.Dashboard.route)},
                        dataStoreManager)
                }

                composable(route = TopLevelRoute.Dashboard.route){
                    DashboardScreen(viewState = viewState,
                        navigateToDetail = { comicId ->
                         navController.navigate(route = "detailcomic/$comicId")
                    })
                }

                composable(route = TopLevelRoute.Trending.route){
                    TrendingScreen()
                }

                composable(route = TopLevelRoute.Notifications.route){
                    NotificationsScreen()
                }

                composable(route = TopLevelRoute.Profile.route){
                    ProfileScreen()
                }
                composable(route = TopLevelRoute.Search.route) {
                    val genreViewModel: GenreViewModel = viewModel()
                    val genreState by genreViewModel.genreState

                    SearchScreen(
                        viewState = SearchViewModel(),
                        genres = genreState.listGenres,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        },
                        navigateToGenre = { genreId ->
                            navController.navigate(route = "genre/$genreId")
                        }
                    )
                }
                composable(
                    route = "genre/{genreId}",
                    arguments = listOf(navArgument("genreId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val genreId = backStackEntry.arguments?.getString("genreId") ?: ""
                    DetailGenreScreen(
                        genreId = genreId,
                        navigateToDetail = { comicId ->
                            navController.navigate("detailcomic/$comicId")
                        }
                    )
                }

                composable("detailcomic/{comicId}") {
                    val comicId = it.arguments?.getString("comicId") ?: ""

                    val detailComicState by comicViewModel.detailComicState

                    // Use LaunchedEffect to ensure fetchDetailComic is called only once
                    LaunchedEffect(comicId) {
                        comicViewModel.fetchDetailComic(comicId)
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailComicState.loading -> {
                                CircularProgressIndicator(progress = 0.89f, modifier = Modifier.align(Alignment.Center))
                            }

                            detailComicState.error != null -> {
                                Text(text = detailComicState.error!!)
                            }

                            else -> {
                                DetailComicScreen(detailComicState, navigateToDetail = { chapterId ->
                                    navController.navigate(route = "detailchapter/$chapterId")
                                })
                            }
                        }
                    }
                }

                composable("detailchapter/{chapterId}") {
                    val chapterId = it.arguments?.getString("chapterId") ?: ""
                    val detailChapterState by comicViewModel.detailChapterState

                    LaunchedEffect(chapterId) {
                        comicViewModel.fetchDetailChapter(chapterId)
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailChapterState.loading -> {
                                CircularProgressIndicator(
                                    progress = { 0.89f },
                                    modifier = Modifier.align(Alignment.Center),
                                    trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
                                )
                            }
                            detailChapterState.detailChapter != null -> {
                                DetailChapterScreen(detailChapterState)
                            }

                            detailChapterState.error != null -> {
                                Text(text = detailChapterState.error!!)
                            }
                        }
                    }
                }
            }
        }
    }



}

suspend fun checkRegisterState(preferenceDataStore: DataStore<Preferences>, onResult: (Boolean) -> Unit) {
    val preferences = preferenceDataStore.data.first()
    val userId = preferences[USER_ID]
    val isLogin = userId != null
    onResult(isLogin)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        TopLevelRoute.Dashboard,
        TopLevelRoute.Trending,
        TopLevelRoute.Search,
        TopLevelRoute.Notifications,
        TopLevelRoute.Profile
    )

    NavigationBar  {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = "${item.label} icon")
                       },
                label = { Text(item.label) },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}