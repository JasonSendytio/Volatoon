package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.viewmodel.ComicViewModel
import java.util.Locale

@Composable
fun MoreComicScreen(
    type: String,
    comicViewModel: ComicViewModel = viewModel(),
    navigateToDetail: (String) -> Unit
) {
    val viewState by comicViewModel.comicstate

    LaunchedEffect(type) {
        comicViewModel.fetchMoreComics(type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "More ${type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )

        when {
            viewState.loading && viewState.currentPageComics.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            viewState.error != null -> {
                Text(
                    text = "Error: ${viewState.error}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewState.currentPageComics) { comic ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navigateToDetail(comic.komik_id) }
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = comic.image),
                                    contentDescription = "Comic Cover - ${comic.title}",
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(150.dp)
                                )

                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = comic.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Type: ${comic.type}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Score: ${comic.score}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Latest: ${comic.chapter}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (viewState.hasPreviousPage) {
                                Button(onClick = {
                                    comicViewModel.fetchMoreComics(type, viewState.currentPage - 1)
                                }) {
                                    Text("Previous")
                                }
                            }
                            if (viewState.hasNextPage) {
                                Button(onClick = {
                                    comicViewModel.fetchMoreComics(type, viewState.currentPage + 1)
                                }) {
                                    Text("Next")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

