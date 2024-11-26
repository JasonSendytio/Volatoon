package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.R

@Composable

fun UserActivityScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Notifications >",
//                modifier = Modifier
//                    .clickable { navigateToPage("history") }
            )
        }
        LazyColumn {
            items(5) {
                Text("Notification")
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text("History >")
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
//                            .clickable { navigateToDetail(comic.komik_id) }
                        .padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comic_thumbnail),
                            contentDescription = "Comic Cover - ",
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                        )

                        // Comic Details
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
//                                .clickable { navigateToDetail(comic.komik_id) },
//                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
//                            Image(
//                                painter = rememberAsyncImagePainter(model = comic.image),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .width(119.dp).height(153.dp)
//                                    .aspectRatio(1f)
//                            )
//                            val displayTitle = if (comic.title.length > 20) {
//                                comic.title.take(12) + "..."
//                            } else {
//                                comic.title
//                            }
                            Text("Comic Title")
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ){
            Text("Bookmarks >")
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
//                            .clickable { navigateToDetail(comic.komik_id) }
                        .padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comic_thumbnail),
                            contentDescription = "Comic Cover - ",
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                        )

                        // Comic Details
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
//                                .clickable { navigateToDetail(comic.komik_id) },
//                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
//                            Image(
//                                painter = rememberAsyncImagePainter(model = comic.image),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .width(119.dp).height(153.dp)
//                                    .aspectRatio(1f)
//                            )
//                            val displayTitle = if (comic.title.length > 20) {
//                                comic.title.take(12) + "..."
//                            } else {
//                                comic.title
//                            }
                            Text("Comic Title")
                        }
                    }
                }
            }
        }
    }
}

//@Preview (showBackground = true)
//@Composable
//fun PreviewScreen(){
//    UserActivityScreen()
//}
