package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.R
import com.example.volatoon.model.ComicBookmark
import com.example.volatoon.viewmodel.BookmarkViewModel

@Composable
fun BookmarkScreen(
    viewState : BookmarkViewModel.BookmarkState,
    navigateToDetail : (String) -> Unit
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
//            .verticalScroll(rememberScrollState()),
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
                LazyColumn ( modifier = Modifier.fillMaxSize()){
                    items(items = bookmarkList) { bookmark ->
                        BookmarkItem(comicBookmark = bookmark, navigateToDetail)
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarkItem(
    comicBookmark: ComicBookmark,
    navigateToDetail : (String) -> Unit
    ){
//    Text("adas")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(comicBookmark.komik_id) }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Comic Image
            Image(
                painter = rememberAsyncImagePainter(
                    model = comicBookmark.comicDetails.image
                ),
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
                    text = comicBookmark.comicDetails.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Type : ${comicBookmark.comicDetails.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Score: ${comicBookmark.comicDetails.score}",
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
            Icon(
                tint = Color.Black,
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
//                modifier = Modifier
//                    .clickable {
//                        viewModel.deleteHistory(dataStoreManager, comic.history_id)
//                    }
            )
        }
    }

}
