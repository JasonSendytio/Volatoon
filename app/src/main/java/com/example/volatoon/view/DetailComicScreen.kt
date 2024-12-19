package com.example.volatoon.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.Chapter
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.BookmarkViewModel
import com.example.volatoon.viewmodel.ComicViewModel
import com.example.volatoon.viewmodel.HistoryViewModel

@Composable
fun DetailComicScreen(
    viewState : ComicViewModel.DetailComicState,
    navigateToDetail : (String) -> Unit,
    dataStoreManager: DataStoreManager,
    bookmarkViewModel: BookmarkViewModel,
    historyViewModel: HistoryViewModel,
    comicId : String,
){
    LaunchedEffect(comicId) {
        historyViewModel.fetchChapterHistory(dataStoreManager, comicId)
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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

            viewState.detailComic == null -> {
                Text(text = "No comic details available.")
            }

            else -> {
                val comic = viewState.detailComic

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        comic.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter(
                        model = comic.image
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .width(300.dp)
                        .aspectRatio(1f)
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                BookmarkIcon(
                    bookmarkViewModel = bookmarkViewModel,
                    dataStoreManager = dataStoreManager,
                    comicId = comicId,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, top = 4.dp)
                ) {
                    DetailRow("Title", comic.title)
                    DetailRow("Other Title", comic.alternativeTitle)
                    DetailRow("Score", comic.score.toString())
                    DetailRow("Status", comic.status)
                    DetailRow("Released", comic.released)
                    DetailRow("Author", comic.author)
                    DetailRow("Genre", comic.genres.joinToString(", "))

                    Text(
                        "Sinopsis",
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .background(Color(0xFFEEEEEE)),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "\t\t\t ${comic.synopsis}",
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF7CB9E8))
//                        .background(Color(0xFFA2D7E2))
                        .padding(8.dp)
                        .height(30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("${comic.title} Chapter List ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                ListChaptersScreen(
                    chapters = comic.chapterList,
                    navigateToDetail = navigateToDetail,
                    chapterHistory = historyViewModel.chapterHistoryState.value
                )            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE))
                .padding(5.dp)
        ) {
            Text(
                label,
                modifier = Modifier
                    .weight(.3f),
                fontWeight = FontWeight.Bold
            )
            Text(
                ": $value",
                modifier = Modifier.weight(.7f)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFDDDDDD))
        )
    }
}
@Composable
fun ListChaptersScreen(
    chapters: List<Chapter>,
    navigateToDetail: (String) -> Unit,
    chapterHistory: Set<String>
) {
    Column {
        chapters.forEachIndexed { index, chapter ->
            Row {
                ListChapter(
                    chapter = chapter,
                    index = index,
                    navigateToDetail = navigateToDetail,
                    isRead = chapterHistory.contains(chapter.chapter_id)
                )
            }
        }
    }
}

@Composable
fun ListChapter(
    chapter: Chapter,
    index: Int,
    navigateToDetail: (String) -> Unit,
    isRead: Boolean
){    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background( when {
                isRead -> Color.Gray
                index % 2 == 0 -> Color(0xFFA2D7E2)
                else -> Color(0xFFD9D9D9)
            }
            )
            .height(30.dp)
            .clickable { navigateToDetail(chapter.chapter_id) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(chapter.title)
        Text(chapter.date)
    }
}

@Composable
fun BookmarkIcon(
    bookmarkViewModel: BookmarkViewModel,
    dataStoreManager: DataStoreManager,
    comicId: String,
) {
    val bookmarkState = bookmarkViewModel.bookmarkstate.value
    val addBookmarkState = bookmarkViewModel.addBookmarkstate.value
    val context = LocalContext.current

    val isBookmarked by remember(bookmarkState.responseData) {
        mutableStateOf(
            bookmarkState.responseData?.data?.any { it.komik_id == comicId } == true
        )
    }

    val lastLoadingState = remember { mutableStateOf(false) }

    LaunchedEffect(addBookmarkState) {
        if (lastLoadingState.value && !addBookmarkState.loading) {
            if (addBookmarkState.error == null) {
                if (isBookmarked) {
                    Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show()
                    Log.i("bookmark state", addBookmarkState.toString())
                } else {
                    Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show()
                    Log.i("add bookmark state", addBookmarkState.toString())
                }
            } else {
                Toast.makeText(context, addBookmarkState.error, Toast.LENGTH_SHORT).show()
            }
        }
        lastLoadingState.value = addBookmarkState.loading
    }

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isBookmarked) {
                    val bookmark = bookmarkState.responseData?.data?.find { it.komik_id == comicId }
                    val bookmarkId = bookmark?.bookmark_id
                    if (bookmarkId != null) {
                        Toast.makeText(context, "Removing...", Toast.LENGTH_SHORT).show()
                        bookmarkViewModel.deleteUserBookmark(dataStoreManager, bookmarkId)
                    }
                } else {
                    Toast.makeText(context, "Adding...", Toast.LENGTH_SHORT).show()
                    bookmarkViewModel.addUserBookmark(dataStoreManager, comicId)
                }
            }
    ) {
        Icon(
            tint = Color.Black,
            imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isBookmarked) "Remove from bookmarks" else "Add to bookmarks",
        )
        Text(
            text = if (isBookmarked) "Remove from bookmarks" else "Add to bookmarks",
        )
    }
}
