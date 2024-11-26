package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.Chapter
import com.example.volatoon.viewmodel.ComicViewModel

@Composable
fun DetailComicScreen(
    viewState : ComicViewModel.DetailComicState,
    navigateToDetail : (String) -> Unit
){
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
                        .width(162.dp)
                        .height(198.dp)
                        .aspectRatio(1f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Title",
                            modifier = Modifier
                                .weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.title}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Other Title",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.alternativeTitle}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Score",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.score ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Status",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.status ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Released",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.released ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Author",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${comic.author}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "Genre",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": Action, Comedy, Fantasy",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Text(
                        "Sinopsis",
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "\t\t\t ${comic.synopsis}",
                        textAlign = TextAlign.Justify
                    )


                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFA2D7E2))
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
                ListChaptersScreen(comic.chapterList, navigateToDetail)
            }
        }
    }
}

@Composable
fun ListChaptersScreen(chapters : List<Chapter>, navigateToDetail : (String) -> Unit){

    Column {
        chapters.forEachIndexed() { index, chapter ->
            Row {
                ListChapter(chapter = chapter, index, navigateToDetail)
            }
        }
    }
}

@Composable
fun ListChapter(chapter : Chapter, index : Int, navigateToDetail : (String) -> Unit){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) Color(0xFFA2D7E2) else Color(0xFFD9D9D9))
            .height(30.dp)
            .clickable { navigateToDetail(chapter.chapter_id) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(chapter.title)
        Text(chapter.date)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDetailComicScreen(){
//    DetailComicScreen()
//}