package com.example.volatoon.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.ComicBookmark
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.BookmarkViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookmarkScreen(
    bookmarkViewModel: BookmarkViewModel,
    viewState : BookmarkViewModel.BookmarkState,
    dataStoreManager: DataStoreManager,
    navigateToDetail : (String) -> Unit
){
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bookmarkViewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "All Bookmark",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )

        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED ${viewState.error}")
            }

            viewState.responseData == null -> {
                Text(text = "No bookmark")
            }

            else -> {
                Spacer(modifier = Modifier.height(8.dp))
                val bookmarkList = viewState.responseData.data
                if (bookmarkList.isEmpty()) {
                    Text(text = "No bookmark")
                }
                if (bookmarkList.isNotEmpty()) {
                    LazyColumn ( modifier = Modifier.fillMaxWidth()){
                        val sortedBookmarkList = bookmarkList.sortedByDescending {
                            ZonedDateTime.parse(it.createdAt)
                        }
                        items(items = sortedBookmarkList) { bookmark ->
                            BookmarkItem(bookmarkViewModel, dataStoreManager, comicBookmark = bookmark, navigateToDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarkItem(
    bookmarkViewModel: BookmarkViewModel,
    dataStoreManager: DataStoreManager,
    comicBookmark: ComicBookmark,
    navigateToDetail : (String) -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { navigateToDetail(comicBookmark.komik_id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Comic Image
            Image(
                painter = rememberAsyncImagePainter(
                    model = comicBookmark.comicDetails.image
                ),
                contentDescription = "Comic Cover",
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp)
                    .align(Alignment.CenterVertically)
                    .height(180.dp)
                    .aspectRatio(1f)
            )

            // Comic Details
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = comicBookmark.comicDetails.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Type: ${comicBookmark.comicDetails.type}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Genres: ${comicBookmark.comicDetails.genres.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Status: ${comicBookmark.comicDetails.status}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Score: ${comicBookmark.comicDetails.score}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Added at: ${formatAddedAt(comicBookmark.createdAt)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                tint = Color.Black,
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        bookmarkViewModel.deleteUserBookmark(dataStoreManager, comicBookmark.bookmark_id)
                    }
            )
        }
    }
}

fun formatAddedAt(createdAt: String): String {
    val zonedDateTime = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    val targetZoneId = ZoneId.of("Asia/Jakarta")
    val convertedTime = zonedDateTime.withZoneSameInstant(targetZoneId)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
    return convertedTime.format(formatter)
}
