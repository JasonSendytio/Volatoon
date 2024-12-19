package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.ComicBookmark
import com.example.volatoon.model.ComicHistory
import com.example.volatoon.viewmodel.BookmarkViewModel
import com.example.volatoon.viewmodel.HistoryViewModel
import java.time.ZonedDateTime

@Composable
fun UserActivityScreen(
    bookmarkViewState: BookmarkViewModel.BookmarkState,
    historyViewState: HistoryViewModel.HistoryState,
    navController: NavController,
    navigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, top = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate("history")
                }
        ) {
            Text(
                text = "History >",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
        Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
            when {
                historyViewState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }
                historyViewState.error != null -> {
                    Text(text = "Error: ${historyViewState.error}")
                }
                historyViewState.responseData == null -> {
                    Text(text = "No history")
                }
                else -> {
                    val historyList = historyViewState.responseData.data
                    if (historyList.isEmpty()){
                        Text(
                            text = "No history"
                        )
                    }
                    if (historyList.isNotEmpty()) {
                        val sortedHistoryList = historyList.sortedByDescending {
                            ZonedDateTime.parse(it.createdAt)
                        }
                        LazyRow(modifier = Modifier.fillMaxSize()) {
                            items(items = sortedHistoryList) { history ->
                                HistoryItemPreview(
                                    comicHistory = history,
                                    navigateToDetail
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate("bookmark")
                }
        ) {
            Text(
                text = "Bookmarks >",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )
        }
        Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
            when {
                bookmarkViewState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }

                bookmarkViewState.error != null -> {
                    Text(text = "Error: ${bookmarkViewState.error}")
                }

                bookmarkViewState.responseData == null -> {
                    Text(
                        text = "No bookmark",
                        modifier = Modifier.padding(16.dp))
                }

                else -> {
                    val bookmarkList = bookmarkViewState.responseData.data
                    if (bookmarkList.isEmpty()) {
                        Text(text = "No bookmark")
                    }
                    if (bookmarkList.isNotEmpty()) {
                        val sortedBookmarkList = bookmarkList.sortedByDescending {
                            ZonedDateTime.parse(it.createdAt)
                        }
                        LazyRow(modifier = Modifier.fillMaxSize()) {
                            items(items = sortedBookmarkList) { bookmark ->
                                BookmarkItemPreview(
                                    comicBookmark = bookmark,
                                    navigateToDetail
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemPreview(
    comicHistory: ComicHistory,
    navigateToDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .fillMaxHeight()
            .clickable { navigateToDetail(comicHistory.komik_id) }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = comicHistory.comicDetails.image
                ),
                contentDescription = "Comic Cover",
                modifier = Modifier
                    .height(160.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = comicHistory.comicDetails.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun BookmarkItemPreview(
    comicBookmark: ComicBookmark,
    navigateToDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .fillMaxHeight()
            .clickable { navigateToDetail(comicBookmark.komik_id) }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = comicBookmark.comicDetails.image
                ),
                contentDescription = "Comic Cover",
                modifier = Modifier
                    .height(160.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = comicBookmark.comicDetails.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
