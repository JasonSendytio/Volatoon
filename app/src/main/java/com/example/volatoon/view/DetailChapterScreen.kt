package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.volatoon.viewmodel.ComicViewModel

@Composable
fun DetailChapterScreen(
    viewState : ComicViewModel.DetailChapterState,
    navigateToOtherChapter : (String) -> Unit
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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {navigateToOtherChapter(chapter.prev_chapter_id)}
                    ){
                        Text("Prev Ch")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray, // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {}
                    ){
                        Text("All Chapter")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {navigateToOtherChapter(chapter.next_chapter_id)}
                    ){
                        Text("Next Ch")
                    }
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

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {navigateToOtherChapter(chapter.prev_chapter_id)}
                    ){
                        Text("Prev Ch")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray, // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {}
                    ){
                        Text("All Chapter")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                            contentColor = Color.Black // Sets the text color to black
                        ),
                        onClick = {navigateToOtherChapter(chapter.next_chapter_id)}
                    ){
                        Text("Next Ch")
                    }
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
