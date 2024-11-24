package com.example.volatoon.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Text("bookmark screen ")
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    progress = 0.89f,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED ${viewState.error}")
            }

            viewState.responseData == null -> {
                Text(text = "No bookmarks available.")
            }

            else -> {
                val listBookmarkk: List<ComicBookmark> = viewState.responseData.data ?: emptyList()

//                Text(listBookmarkk[0].komik_id)
                LazyColumn ( modifier = Modifier.fillMaxSize()){
                    items(items = listBookmarkk) { bookmark ->
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable { navigateToDetail(comicBookmark.komik_id) },
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
//        Text("asdas")
//
//        Column(
//            horizontalAlignment = Alignment.Start
//        ){
//            Text(comicBookmark.komik_id)
//            Text(comic.type)
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(comic.chapter)
//        }

        Text(comicBookmark.komik_id)
    }
}
