package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.R
import com.example.volatoon.model.Comic
import com.example.volatoon.viewmodel.ComicViewModel
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.getValue
import com.example.volatoon.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
    navigateToDetail : (String) -> Unit,
    navigateToGenre: (String) -> Unit,
    navigateToMore: (String) -> Unit,
    viewState : ComicViewModel.ComicsState,
    viewModel: ComicViewModel,
    profileViewModel: ProfileViewModel
){
    val currentUserData by profileViewModel.userData

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewState.loading,
        onRefresh = {
            viewModel.refreshComics() // Panggil fungsi refresh di viewModel
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),

    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                modifier = Modifier.width(50.dp).height(50.dp),
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "test"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hi, ${currentUserData?.userName ?: "User"}!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        if (currentUserData?.isPremium == false) {
            Image(
                modifier = Modifier.fillMaxWidth()
                    .height(120.dp).aspectRatio(1f),
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "banner image"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {navigateToGenre("all")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("All")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {navigateToGenre("6")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("Sci-fi")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("Fantasy")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {navigateToGenre("12")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text(
                    text = "Romance",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
        ){
            when {
                viewState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }
                viewState.error != null -> {
                    Text(text = "ERROR OCCURRED ${viewState.error}")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = rememberLazyListState()

                    ) {
                        // Manga Section
                        item {
                            ComicsScreen(title = "Manga", comics = viewState.listManga, navigateToDetail,
                                navigateToMore = navigateToMore)
                        }

                        // Manhua Section
                        item {
                            ComicsScreen(title = "Manhua", comics = viewState.listManhua, navigateToDetail,
                                navigateToMore = navigateToMore)
                        }

                        // Manhwa Section
                        item {
                            ComicsScreen(title = "Manhwa", comics = viewState.listManhwa, navigateToDetail,
                                navigateToMore = navigateToMore)
                        }
                    }
                }
            }
        }
    }
    PullRefreshIndicator(
        refreshing = viewState.loading,
        state = pullRefreshState,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    )
}

@Composable
fun ComicsScreen(
    title: String,
    comics : List<Comic>,
    navigateToDetail : (String) -> Unit,
    navigateToMore: (String) -> Unit
){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "List $title!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navigateToMore(title.lowercase())
                }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(comics) { comic ->
                ComicItem(comic = comic, navigateToDetail)
            }
        }
    }
}

@Composable
fun ComicItem(
    comic : Comic,
    navigateToDetail : (String) -> Unit
){
    Column (
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .padding(2.dp)
            .clickable { navigateToDetail(comic.komik_id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = rememberAsyncImagePainter(model = comic.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = comic.title,
            maxLines = 2,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
