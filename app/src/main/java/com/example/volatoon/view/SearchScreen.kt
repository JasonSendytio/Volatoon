package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.Comic
import com.example.volatoon.model.Genres
import com.example.volatoon.viewmodel.SearchViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewState : SearchViewModel,
    genres : List<Genres>,
    navigateToDetail : (String) -> Unit,
    navigateToGenre: (String) -> Unit

){
    Log.d("SearchScreen", "Initial genres size: ${genres.size}")

    val searchText by viewState.searchText.collectAsState()
    val isSearching by viewState.isSearching.collectAsState()
    val comicsState by viewState.comics.collectAsState()

    Column (
        modifier = Modifier.fillMaxWidth().padding(10.dp).wrapContentHeight()
    ){
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    onQueryChange = viewState::onSearchTextChange,
                    query = searchText,
                    onSearch =  viewState::onSearchTextChange,
                    expanded = isSearching,
                    onExpandedChange = {  },
                    placeholder = { Text("Cari Komiknya!") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewState.onClearSearch()
                            },)},
                )
            },
            expanded = isSearching,
            onExpandedChange = {}
        ) {
            when {
                comicsState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }

                comicsState.error != null -> {
                    Text(text = "ERROR OCCURRED ${comicsState.error}")
                }

                else -> {
                    LazyColumn {
                        items(comicsState.listComic){
                                comic ->
                            ComicSearchItem(comic = comic, navigateToDetail)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Recent Search", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {
                Icon(Icons.Default.Search, contentDescription = null)
                Text("Kaguya Sama")
            }
            Icon(Icons.Default.Clear, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Favourite Genres", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genres) { genre ->
                GenreItem(
                    genre = genre,
                    onGenreClick = { navigateToGenre(genre.genre_id) }
                )
            }
        }



    }
}

@Composable
fun ComicSearchItem(
    comic : Comic,
    navigateToDetail : (String) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable { navigateToDetail(comic.komik_id) },
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Image(
            painter = rememberAsyncImagePainter(model = comic.image),
            contentDescription = null,
            modifier = Modifier
                .width(71.dp).height(90.dp)
                .aspectRatio(1f)
        )

        Column(
            horizontalAlignment = Alignment.Start
        ){
            Text(comic.title)
            Text(comic.type)
            Spacer(modifier = Modifier.height(6.dp))
            Text(comic.chapter)
        }

        Text(comic.score)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen(){
//    SearchScreen()
}

@Composable
private fun GenreItem(
    genre: Genres,
    onGenreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onGenreClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getGenreEmoji(genre.name),
            fontSize = 32.sp
        )
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

private fun getGenreEmoji(genreName: String): String {
    return when (genreName.lowercase(Locale.ROOT)) {
        "action" -> "⚔️"
        "adventure" -> "🗺️"
        "comedy" -> "😂"
        "drama" -> "🎭"
        "fantasy" -> "🧙‍♂️"
        "horror" -> "👻"
        "romance" -> "❤️"
        // Add more genres and emojis
        else -> "📚"
    }
}