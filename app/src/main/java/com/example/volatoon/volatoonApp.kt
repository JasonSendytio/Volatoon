package com.example.volatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.volatoon.utils.DataStoreManager.Companion.AUTH_TOKEN
import com.example.volatoon.view.DashboardScreen
import com.example.volatoon.view.DetailChapterScreen
import com.example.volatoon.view.DetailComicScreen
import com.example.volatoon.view.DetailGenreScreen
import com.example.volatoon.view.LoginScreen
import com.example.volatoon.view.UserActivityScreen
import com.example.volatoon.view.ProfileScreen
import com.example.volatoon.view.RegisterScreen
import com.example.volatoon.view.SearchScreen
import com.example.volatoon.view.TrendingScreen
import com.example.volatoon.viewmodel.ComicViewModel
import com.example.volatoon.viewmodel.GenreViewModel
import com.example.volatoon.viewmodel.ProfileViewModel
import com.example.volatoon.viewmodel.SearchViewModel
import com.example.volatoon.view.BookmarkScreen
import com.example.volatoon.view.HistoryScreen
import com.example.volatoon.view.VolatoonPremiumScreen
import com.example.volatoon.viewmodel.BookmarkViewModel
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.viewmodel.RegisterViewModel
import com.example.volatoon.viewmodel.CommentViewModel
import com.example.volatoon.viewmodel.HistoryViewModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun VolatoonApp(
    navController: NavHostController,
    preferenceDataStore : DataStore<Preferences>,
    dataStoreManager: DataStoreManager
){
    var isLogin by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect (key1 = Unit){
        checkRegisterState(preferenceDataStore){it->
            isLogin = it
        }
    }

    val comicViewModel : ComicViewModel = viewModel()
    val profileViewModel : ProfileViewModel = viewModel()
    val registerViewModel : RegisterViewModel = viewModel()
    val loginViewModel : LoginViewModel = viewModel()
    val bookmarkViewModel : BookmarkViewModel = viewModel()
    val historyViewModel : HistoryViewModel = viewModel()

    val viewState by comicViewModel.comicstate

    val onLogOut = {
        isLogin = false
        scope.launch{
            dataStoreManager.clearDataStore()
        }
    }

    Scaffold(
        bottomBar = { if(isLogin) BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            NavHost(
                navController = navController,
                startDestination = if (isLogin) TopLevelRoute.Dashboard.route else "login"
            ){
                composable(
                    route = "register",
                ) {
                   RegisterScreen(navigateToLogin = {
                       navController.navigate(route = "login")
                   },
                       dataStoreManager,
                       registerViewModel,
                       loginViewModel,
                       navigateToDashboard = {
                           navController.navigate(route = TopLevelRoute.Dashboard.route) },
                       )
                }

                composable(
                    route = "login",
                ) {
                    LoginScreen(
                        navigateToDashboard = {
                        navController.navigate(route = TopLevelRoute.Dashboard.route) },
                        dataStoreManager,
                        navigateToRegister = {
                            navController.navigate(route = "register")
                        })
                }

                composable(route = TopLevelRoute.Dashboard.route){
                    DashboardScreen(viewState = viewState,
                        navigateToDetail = { comicId ->
                         navController.navigate(route = "detailcomic/$comicId")
                    },
                        navigateToGenre = { genreId ->
                            navController.navigate("genre/$genreId")
                        })

                }

                composable(route = TopLevelRoute.Trending.route){
                    TrendingScreen()
                }

                composable(route = TopLevelRoute.UserActivity.route){
                    UserActivityScreen(navController)
                }

                composable(route = TopLevelRoute.Profile.route){
                    val profileResState by profileViewModel.profileResState

                    LaunchedEffect(dataStoreManager) {
                        profileViewModel.fetchUserData(dataStoreManager)
                    }

                    ProfileScreen(
                        onLogOut,
                        onNavigateToBookmark = {
                            navController.navigate(route = "bookmark")
                        },
                        onNavigateToPremium = {
                            navController.navigate(route = "premium")
                        },
                        profileResState,
                    )
                }

                composable(
                    route = "bookmark",
                ) {
                    val bookmarkState by bookmarkViewModel.bookmarkstate

                    LaunchedEffect(dataStoreManager) {
                        bookmarkViewModel.fetchUserBookmark(dataStoreManager)
                    }

                    BookmarkScreen (
                        viewState = bookmarkState,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        }
                    )
                }


                composable(
                    route = "premium"
                ) {
                    VolatoonPremiumScreen()
                }

                composable(
                    route = "history"
                ) {
                    HistoryScreen(
                        dataStoreManager,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        }
                    )
                }

                composable(route = TopLevelRoute.Search.route) {
                    val genreViewModel: GenreViewModel = viewModel()
                    val genreState by genreViewModel.genreState

                    SearchScreen(
                        viewState = SearchViewModel(),
                        genres = genreState.listGenres,
//                        context = LocalContext.current, // Add this line

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
                    val addBookmarkState by bookmarkViewModel.addBookmarkstate

                    // Use LaunchedEffect to ensure fetchDetailComic is called only once
                    LaunchedEffect(comicId) {
                        comicViewModel.fetchDetailComic(comicId)
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailComicState.loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                                )
                            }

                            detailComicState.error != null -> {
                                Text(text = detailComicState.error!!)
                            }

                            else -> {
                                DetailComicScreen(
                                    addBookmarkState,
                                    detailComicState,
                                    navigateToDetail = { chapterId ->
                                    navController.navigate(route = "detailchapter/$chapterId")
                                },
                                    dataStoreManager,
                                    bookmarkViewModel,
                                    historyViewModel,
                                    comicId
                                    )
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
                                val commentViewModel: CommentViewModel = viewModel()

                                DetailChapterScreen(
                                    viewState = detailChapterState,
                                    navigateToOtherChapter = { chapterId ->
                                        navController.navigate(route = "detailchapter/$chapterId")
                                    },
                                    commentViewModel = commentViewModel,  // Add this
                                    dataStoreManager = dataStoreManager  // Add this
                                )
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
    val userId = preferences[AUTH_TOKEN]
    val isLogin = userId != null
    onResult(isLogin)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        TopLevelRoute.Dashboard,
        TopLevelRoute.Trending,
        TopLevelRoute.Search,
        TopLevelRoute.UserActivity,
        TopLevelRoute.Profile
    )

    NavigationBar  {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = "${item.label} icon")
                       },
                label = { item.LabelText() },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}