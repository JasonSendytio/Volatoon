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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.volatoon.model.SearchDatabase
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
import com.example.volatoon.view.MoreComicScreen
import com.example.volatoon.view.PremiumRedemptionScreen
import com.example.volatoon.view.UpdateProfileScreen
import com.example.volatoon.view.VolatoonPremiumScreen
import com.example.volatoon.viewmodel.BookmarkViewModel
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.viewmodel.RegisterViewModel
import com.example.volatoon.viewmodel.CommentViewModel
import com.example.volatoon.viewmodel.HistoryViewModel
import com.example.volatoon.viewmodel.PremiumRedemptionViewModel
import com.example.volatoon.viewmodel.UpdateProfileViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun VolatoonApp(
    navController: NavHostController,
    preferenceDataStore : DataStore<Preferences>,
    dataStoreManager: DataStoreManager
){
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true // Makes the icons and text black
        )
    }

    var isLogin by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect (key1 = Unit){
        checkRegisterState(preferenceDataStore){
            isLogin = it
        }
    }

    val comicViewModel : ComicViewModel = viewModel()
    val profileViewModel : ProfileViewModel = viewModel()
    val registerViewModel : RegisterViewModel = viewModel()
    val loginViewModel : LoginViewModel = viewModel()
    val bookmarkViewModel : BookmarkViewModel = viewModel()
    val historyViewModel : HistoryViewModel = viewModel()
    val commentViewModel : CommentViewModel = viewModel()
    val updateUserProfile : UpdateProfileViewModel = viewModel()
    val viewState by comicViewModel.comicstate

    val onLogOut = {
        isLogin = false
        profileViewModel.clearUserData()
        scope.launch{
            dataStoreManager.clearDataStore()
        }
    }

    var navigateOnce by remember { mutableStateOf(false) }

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
                           if (!navigateOnce) {
                               navController.navigate(TopLevelRoute.Dashboard.route) {
                                   popUpTo("login") { inclusive = true }
                                   launchSingleTop = true
                               }
                               navigateOnce = true // Set the flag to prevent further navigation
                               isLogin = true
                           }
                       }
                       )
                }

                composable(
                    route = "login",
                ) {
                    LoginScreen(
                        navigateToDashboard = {
                            if (!navigateOnce) {
                                navController.navigate(TopLevelRoute.Dashboard.route) {
                                    popUpTo("login") { inclusive = true }
                                    launchSingleTop = true
                                }
                                navigateOnce = true // Set the flag to prevent further navigation
                                isLogin = true
                            }
                        },
                        dataStoreManager,
                        navigateToRegister = {
                            navController.navigate(route = "register")
                        })
                }

                composable(route = TopLevelRoute.Dashboard.route){
                    LaunchedEffect(Unit) {
                        profileViewModel.fetchUserData(dataStoreManager)
                    }
                    DashboardScreen(
                        viewState = viewState,
                        viewModel = comicViewModel,
                        profileViewModel = profileViewModel,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        },
                        navigateToGenre = { genreId ->
                            navController.navigate("genre/$genreId")
                        },
                        navigateToMore = { type ->
                            navController.navigate("more/${type.lowercase()}")
                        }
                    )
                }


                composable(
                    route = "more/{type}",
                    arguments = listOf(navArgument("type") { type = NavType.StringType })
                ) { backStackEntry ->
                    val type = backStackEntry.arguments?.getString("type") ?: ""
                    MoreComicScreen(
                        type = type,
                        comicViewModel = comicViewModel,
                        navigateToDetail = { comicId ->
                            navController.navigate("detailcomic/$comicId")
                        }
                    )
                }

                composable(route = TopLevelRoute.Trending.route){
                    TrendingScreen(
                        viewState = comicViewModel.trendingComicsState.value, // Use trendingComicsState
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        },
                        navigateToMore = { type ->
                            navController.navigate("more/${type.lowercase()}")
                        }
                    )
                }

                composable(route = TopLevelRoute.UserActivity.route){
                    val bookmarkState by bookmarkViewModel.bookmarkstate
                    val historyState by historyViewModel.historyState

                    LaunchedEffect(dataStoreManager) {
                        bookmarkViewModel.fetchUserBookmark(dataStoreManager)
                        historyViewModel.fetchHistory(dataStoreManager)
                    }

                    UserActivityScreen(
                        bookmarkViewState = bookmarkState,
                        historyViewState = historyState,
                        navController,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        }
                    )
                }

                composable(route = TopLevelRoute.Profile.route){
                    LaunchedEffect(dataStoreManager) {
                        profileViewModel.fetchUserData(dataStoreManager)
                    }

                    ProfileScreen(
                        onLogOut = onLogOut,
                        onNavigateToUserActivity = {
                            navController.navigate(route = TopLevelRoute.UserActivity.route)
                        },
                        onNavigateToPremium = {
                            navController.navigate(route = "premium")
                        },
                        onNavigateToUpdateProfile = {
                            navController.navigate(route = "updateProfile")
                        },
                        viewState = profileViewModel.profileResState.value
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
                        bookmarkViewModel,
                        viewState = bookmarkState,
                        dataStoreManager,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        }
                    )
                }

                composable(
                    route = "updateProfile",
                ){
                    UpdateProfileScreen(
                        viewModel = updateUserProfile,
                        profileViewModel = profileViewModel,
                        dataStoreManager = dataStoreManager,
                        navigateToProfile = {
                            navController.navigate(route = "profile")
                        }
                    )
                }

                composable(
                    route = "premium"
                ) {
                    VolatoonPremiumScreen(
                        profileViewModel = profileViewModel,
                        navigateToRedemption = {
                            navController.navigate(route = "redemption")
                        }
                    )
                }

                composable(route = "redemption") {
                    PremiumRedemptionScreen(
                        PremiumRedemptionViewModel(),
                        dataStoreManager
                    )
                }

                composable(
                    route = "history"
                ) {
                    val historyState by historyViewModel.historyState

                    LaunchedEffect(dataStoreManager) {
                        historyViewModel.fetchHistory(dataStoreManager)
                    }
                    HistoryScreen(
                        historyViewModel,
                        viewState = historyState,
                        dataStoreManager,
                        navigateToDetail = { comicId ->
                            navController.navigate(route = "detailcomic/$comicId")
                        }
                    )
                }

                composable(route = TopLevelRoute.Search.route) {
                    val genreViewModel: GenreViewModel = viewModel()
                    val genreState by genreViewModel.genreState
                    val searchDatabase = SearchDatabase(LocalContext.current)
                    val searchViewModel = viewModel<SearchViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return SearchViewModel(searchDatabase) as T
                            }
                        }
                    )

                    SearchScreen(
                        viewState = searchViewModel,
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

                    LaunchedEffect(comicId) {
                        comicViewModel.fetchDetailComic(comicId)
                        bookmarkViewModel.fetchUserBookmark(dataStoreManager)
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
                                    detailComicState,
                                    navigateToDetail = { chapterId ->
                                        comicViewModel.setSelectedComicId(comicId)
                                        navController.navigate(route = "detailchapter/$chapterId")
                                    },
                                    dataStoreManager,
                                    bookmarkViewModel,
                                    historyViewModel,
                                    comicId,
                                )
                            }
                        }
                    }
                }

                composable("detailchapter/{chapterId}") {
                    val chapterId = it.arguments?.getString("chapterId") ?: ""
                    val comicId = comicViewModel.selectedComicId.value
                    val detailChapterState by comicViewModel.detailChapterState

                    LaunchedEffect(Unit) {
                        comicViewModel.fetchDetailChapter(chapterId)

                        chapterId.let { id ->
                            if (id.isNotEmpty()) {
                                commentViewModel.fetchComments(id, dataStoreManager)
                            }
                            comicId.let { comic ->
                                if (comic != null) {
                                    historyViewModel.addHistory(dataStoreManager, comic, id)
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()){
                        when {
                            detailChapterState.loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
                                )
                            }

                            detailChapterState.detailChapter != null -> {
                                DetailChapterScreen(
                                    viewState = detailChapterState,
                                    navigateToOtherChapter = { chapterId ->
                                        navController.navigate(route = "detailchapter/$chapterId")
                                    },
                                    navigateToComicDetail = { comicId ->
                                        navController.navigate("detailcomic/$comicId")
                                    },
                                    commentViewModel = commentViewModel,
                                    dataStoreManager = dataStoreManager,
                                    profileViewModel = profileViewModel

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
