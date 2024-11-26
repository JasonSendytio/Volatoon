package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.R
import com.example.volatoon.viewmodel.GenreViewModel

@Composable
fun HistoryScreen(

){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Section
        Text(
            text = "All history",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )

        when {
//            viewState.loading && viewState.genreComics.isEmpty() -> {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//
//            viewState.error != null -> {
//                Text(
//                    text = "Error: ${viewState.error}",
//                    color = Color.Red,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) { comic ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .clickable { navigateToDetail(comic.komik_id) }
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                // Comic Image
                                Image(
                                    painter = painterResource(R.drawable.comic_thumbnail),
                                    contentDescription = "Comic Cover - ",
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(150.dp)
                                )

                                // Comic Details
                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = "comic.title",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = "Type: {}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = "Score: {}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = "Last chapter read: {}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }

                    // Pagination Controls
//                    item {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            if (viewState.hasPreviousPage) {
//                                Button(
//                                    onClick = { genreViewModel.loadPreviousPage() },
//                                    enabled = !viewState.loading
//                                ) {
//                                    Text("Previous")
//                                }
//                            }
//
//                            if (viewState.hasNextPage) {
//                                Button(
//                                    onClick = { genreViewModel.loadNextPage() },
//                                    enabled = !viewState.loading
//                                ) {
//                                    Text("Next")
//                                }
//                            }
//                        }
//
//                        if (viewState.loading) {
//                            CircularProgressIndicator(
//                                modifier = Modifier
//                                    .align(Alignment.CenterHorizontally)
//                                    .padding(8.dp)
//                            )
//                        }
//                    }
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewHistoryScreen(){
    HistoryScreen()
}