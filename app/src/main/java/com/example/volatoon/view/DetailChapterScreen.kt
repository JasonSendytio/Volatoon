package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.viewmodel.ComicViewModel

@Composable
fun DetailChapterScreen(
    viewState : ComicViewModel.DetailChapterState
){
    Column(
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

            viewState.detailChapter == null -> {
                Text(text = "No comic details available.")
            }

            else -> {
                val chapter = viewState.detailChapter

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(chapter.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                chapter.images.forEachIndexed { index, chapterImg ->
                    SubcomposeAsyncImage(
                        model = chapterImg,
                        contentDescription = "Chapter Image $index",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), // Ensures it respects its intrinsic height
                        contentScale = ContentScale.FillWidth // Ensures it fills the width without cropping
                    )
                }
            }
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewDetailChapterScreen(){
//    DetailChapterScreen()
//}
