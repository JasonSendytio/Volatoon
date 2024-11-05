package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.volatoon.viewmodel.DashboardViewModel

@Composable
fun DetailComicScreen(
    viewState : DashboardViewModel.DetailComicState
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
                    progress = 0.89f,
                    modifier =  Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED ${viewState.error}")
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    viewState.detailComic?.let {
                        Text(
                            it.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Image(
                    painter = rememberAsyncImagePainter(
                        model = viewState.detailComic?.image
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
                            ": ${viewState.detailComic?.title ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "alternateTitle",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${viewState.detailComic?.alternativeTitle ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "score",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${viewState.detailComic?.score ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "status",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${viewState.detailComic?.status ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "released",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${viewState.detailComic?.released ?: "-"}",
                            modifier = Modifier.weight(.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            "author",
                            modifier = Modifier.weight(.3f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ": ${viewState.detailComic?.author ?: "-"}",
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
                        "\t\t\t ${viewState.detailComic?.synopsis ?: "-"}",
                        textAlign = TextAlign.Justify
                    )


                }
            }
        }
    }
}

@Composable
fun ListChapter(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFA2D7E2))
            .height(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text("Chapter 1124")
        Text("August 23, 2024")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDetailComicScreen(){
//    DetailComicScreen()
//}