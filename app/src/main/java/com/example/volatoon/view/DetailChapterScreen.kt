    package com.example.volatoon.view

    import android.util.Log
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.IntrinsicSize
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.wrapContentHeight
    import androidx.compose.foundation.lazy.LazyColumn
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
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Send
    import androidx.compose.material.icons.filled.KeyboardArrowUp
    import androidx.compose.material.icons.filled.KeyboardArrowDown
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.TextButton
    import androidx.compose.material3.Card
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.setValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.automirrored.filled.Send
    import androidx.compose.material.icons.filled.Delete
    import androidx.compose.material3.Divider
    import androidx.compose.material3.HorizontalDivider
    import androidx.compose.material3.Icon
    import androidx.compose.runtime.LaunchedEffect
    import com.example.volatoon.model.Chapter
    import com.example.volatoon.model.Comment
    import com.example.volatoon.model.Profile
    import com.example.volatoon.utils.DataStoreManager
    import com.example.volatoon.viewmodel.CommentViewModel
    import kotlinx.coroutines.flow.firstOrNull
    import kotlinx.coroutines.runBlocking

    @Composable
    fun CommentItem(
        comment: Comment,
        currentUserId: String?,
        onLike: () -> Unit,
        onDelete: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.user?.userName ?: "Unknown User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                if (currentUserId == comment.userId) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete comment",
                            tint = Color.Red
                        )
                    }
                }
            }

            Text(
                text = comment.content,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLike() }
            ) {
                Text(
                    text = "❤️ ${comment.likes}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }



    @Composable
    fun DetailChapterScreen(
        viewState: ComicViewModel.DetailChapterState,
        navigateToOtherChapter: (String) -> Unit,
        commentViewModel: CommentViewModel,
        dataStoreManager: DataStoreManager,
    ) {
    // Add this near the top of the DetailChapterScreen composable function
        val chapter = viewState.detailChapter  // chapter is defined here

        var expandedComments by remember { mutableStateOf(false) }
        var currentPage by remember { mutableStateOf(0) }
        // Tambahkan setelah deklarasi currentPage
        LaunchedEffect(viewState.detailChapter?.chapter_id) {
            val chapterId = viewState.detailChapter?.chapter_id
            Log.d("DetailChapterScreen", "Chapter ID: $chapterId")

            chapterId?.let { id ->
                if (id.isNotEmpty()) {
                    commentViewModel.fetchComments(id, dataStoreManager)
                }
            }
        }


        LaunchedEffect(Unit) {
            Log.d("DetailChapterScreen", "DetailChapter data: ${viewState.detailChapter}")
            Log.d("DetailChapterScreen", "Chapter ID: ${viewState.detailChapter?.chapter_id}")
        }


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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Comments",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            var commentText by remember { mutableStateOf("") }


                            // Comment Input
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = commentText,
                                    onValueChange = { if (it.length <= 256) commentText = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    placeholder = { Text("Add a comment...") },
                                    maxLines = 3
                                )

                                IconButton(
                                    onClick = {
                                        if (commentText.isNotBlank()) {
                                            val chapterId = viewState.detailChapter?.chapter_id
                                            Log.d("DetailChapterScreen", "Attempting to post comment. ChapterId: $chapterId")
                                            commentViewModel.postComment(viewState.detailChapter?.chapter_id ?: "", commentText, dataStoreManager)
                                            commentText = "" // Clear the input after posting
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send comment",
                                        tint = Color(0xFF04FFFB)
                                    )
                                }
                            }

                            // Comments Display
                            when {
                                commentViewModel.commentState.value.loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                                commentViewModel.commentState.value.error != null -> {
                                    Text(
                                        text = commentViewModel.commentState.value.error ?: "",
                                        color = Color.Red
                                    )
                                }
                                commentViewModel.commentState.value.commentResponse?.data.isNullOrEmpty() -> {
                                    Text(
                                        text = "No comments available",
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }
                                else -> {
                                    // Inside DetailChapterScreen composable
                                    LazyColumn(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                        .height(200.dp)
                                    ) {// Inside the LazyColumn where comments are displayed
                                        items(commentViewModel.commentState.value.commentResponse?.data ?: emptyList()) { comment ->
                                            val currentUserId = remember {
                                                mutableStateOf<String?>(null)
                                            }
                                            LaunchedEffect(Unit) {
                                                val userId = comment.userId
                                                currentUserId.value = userId
                                            }
                                            CommentItem(
                                                    comment = comment,
                                            currentUserId = currentUserId.value,
                                            onLike = {
                                                commentViewModel.likeComment(
                                                    commentId = comment.comment_id,
                                                    chapterId = viewState.detailChapter?.chapter_id ?: "",
                                                    dataStoreManager = dataStoreManager
                                                )
                                            },
                                            onDelete = {
                                                commentViewModel.deleteComment(
                                                    commentId = comment.comment_id,
                                                    chapterId = viewState.detailChapter?.chapter_id ?: "",
                                                    dataStoreManager = dataStoreManager
                                                )
                                            }
                                            )
                                        }

                                    }

                                    // Expand/Collapse and Pagination Controls
    // Expand/Collapse and Pagination Controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { expandedComments = !expandedComments }
                        ) {
                            Icon(
                                imageVector = if (expandedComments) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expandedComments) "Show less" else "Show more"
                            )
                            Text(if (expandedComments) "Show less" else "Show more")
                        }

                        val comments = commentViewModel.commentState.value.commentResponse?.data ?: emptyList()
                        if (expandedComments && comments.size > 10) {
                            Row {
                                IconButton(
                                    onClick = { if (currentPage > 0) currentPage-- },
                                    enabled = currentPage > 0
                                ) {
                                    Text("←")
                                }
                                Text(
                                    "${currentPage + 1}/${(comments.size + 9) / 10}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                                IconButton(
                                    onClick = { if ((currentPage + 1) * 10 < comments.size) currentPage++ },
                                    enabled = (currentPage + 1) * 10 < comments.size
                                ) {
                                    Text("→")
                                }
                            }
                        }
                    }

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )


                    {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                                contentColor = Color.Black // Sets the text color to black
                            ),
                            onClick = {
                                if (chapter != null) {
                                    navigateToOtherChapter(chapter.prev_chapter_id)
                                }
                            }
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
                            onClick = {
                                if (chapter != null) {
                                    navigateToOtherChapter(chapter.next_chapter_id)
                                }
                            }
                        ){
                            Text("Next Ch")
                        }
                    }


                }
            }


                    }}}}

    //@Preview(showBackground = true)
    //@Composable
    //fun PreviewDetailChapterScreen(){
    //    DetailChapterScreen()
    //}


