package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.Comic
import com.example.volatoon.viewmodel.ComicViewModel

@Composable
fun TrendingScreen(
    viewState: ComicViewModel.ComicsState,
    navigateToDetail: (String) -> Unit,
    navigateToMore: (String) -> Unit
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        item {
            // Manga Section
            TrendingSection(
                title = "Trending Manga!",
                comics = viewState.listManga,
                isLoading = viewState.loading,
                navigateToDetail = navigateToDetail,
                navigateToMore = { navigateToMore("manga") }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Manhwa Section
            TrendingSection(
                title = "Trending Manhwa!",
                comics = viewState.listManhwa,
                isLoading = viewState.loading,
                navigateToDetail = navigateToDetail,
                navigateToMore = { navigateToMore("manhwa") }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Manhua Section
            TrendingSection(
                title = "Trending Manhua!",
                comics = viewState.listManhua,
                isLoading = viewState.loading,
                navigateToDetail = navigateToDetail,
                navigateToMore = { navigateToMore("manhua") }
            )
        }
    }
}

@Composable
private fun TrendingSection(
    title: String,
    comics: List<Comic>,
    isLoading: Boolean,
    navigateToDetail: (String) -> Unit,
    navigateToMore: () -> Unit
) {
    Column {

        Text(
            text = "Trending 🔥",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = navigateToMore)
            ) {
                Text(
                    text = "View More",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "See More",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isLoading && comics.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Top 3 Section (Horizontal)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                comics.take(3).forEachIndexed { index, comic ->
                    TopRankedComicItem(
                        comic = comic,
                        rank = index + 1,
                        navigateToDetail = navigateToDetail
                    )
                }
            }

            // Rankings 4-10 (Vertical List)
            Column {
                comics.drop(3).take(7).forEachIndexed { index, comic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .clickable { navigateToDetail(comic.komik_id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${index + 4}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )

                        Image(
                            painter = rememberAsyncImagePainter(model = comic.image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = comic.title,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Score: ${comic.score}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
private fun TopRankedComicItem(
    comic: Comic,
    rank: Int,
    navigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable { navigateToDetail(comic.komik_id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = comic.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
                shape = CircleShape,
                color = when(rank) {
                    1 -> Color(0xFFFFD700) // Gold
                    2 -> Color(0xFFC0C0C0) // Silver
                    3 -> Color(0xFFCD7F32) // Bronze
                    else -> MaterialTheme.colorScheme.primary
                }
            ) {
                Text(
                    text = "#$rank",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = comic.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}
