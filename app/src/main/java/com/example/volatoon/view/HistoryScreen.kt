package com.example.volatoon.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.ComicHistory
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.HistoryViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel,
    viewState: HistoryViewModel.HistoryState,
    dataStoreManager: DataStoreManager,
    navigateToDetail: (String) -> Unit
){
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        historyViewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All History",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )

        when {
            viewState.loading -> {
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

            viewState.responseData == null -> {
                Text(
                    text = "No history",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                val historyList = viewState.responseData.data
                if (historyList.isEmpty()) {
                    Text(
                        text = "No history",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                if (historyList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val sortedHistoryList = historyList.sortedByDescending {
                            ZonedDateTime.parse(it.createdAt)
                        }
                        items(items = sortedHistoryList) { comic ->
                            HistoryItem(
                                historyViewModel = historyViewModel,
                                comicHistory = comic,
                                dataStoreManager = dataStoreManager,
                                navigateToDetail = navigateToDetail
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    historyViewModel: HistoryViewModel,
    comicHistory: ComicHistory,
    dataStoreManager: DataStoreManager,
    navigateToDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(comicHistory.komik_id) }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Comic Image
            Image(
                painter = rememberAsyncImagePainter(
                    model = comicHistory.comicDetails.image
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
                    text = comicHistory.comicDetails.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Type: ${comicHistory.comicDetails.type}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Genre: ${comicHistory.comicDetails.genres.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Last chapter read: ${comicHistory.chapter_id.takeLastWhile { it.isDigit() }}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Last read: ${formatCreatedAt((comicHistory.createdAt))}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Icon(
                tint = Color.Black,
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier
                    .clickable {
                        historyViewModel.deleteHistory(dataStoreManager, comicHistory.history_id)
                    }
            )
        }
    }
}

fun formatCreatedAt(createdAt: String): String {
    val zonedDateTime = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss") // Example: 09 Dec 2024, 19:27:45
    return zonedDateTime.format(formatter)
}
