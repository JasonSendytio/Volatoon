package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.CommentRequest
import com.example.volatoon.model.Comment
import com.example.volatoon.model.CommentResponse
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CommentViewModel : ViewModel() {
    private val _commentState = mutableStateOf(CommentState())
    val commentState: State<CommentState> = _commentState
    private val TAG = "CommentViewModel"

    fun fetchComments(chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching comments for chapter: $chapterId")
                _commentState.value = _commentState.value.copy(loading = true)
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    Log.d(TAG, "Making API call to fetch comments")
                    val response = apiService.getComments(chapterId, bearerToken)

                    if (response.isSuccessful && response.body() != null) {
                        Log.d(TAG, "Successfully fetched comments: ${response.body()?.data?.size} comments")
                        _commentState.value = _commentState.value.copy(
                            loading = false,
                            commentResponse = response.body(),
                            error = null
                        )
                    } else {
                        Log.e(TAG, "Error fetching comments: ${response.message()}")
                        _commentState.value = _commentState.value.copy(
                            loading = false,
                            error = "Error: ${response.message()}"
                        )
                    }
                } else {
                    Log.e(TAG, "Token is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while fetching comments", e)
                _commentState.value = _commentState.value.copy(
                    loading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    fun postComment(chapterId: String, content: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                if (content.isBlank()) {
                    _commentState.value = _commentState.value.copy(
                        error = "Comment cannot be empty"
                    )
                    return@launch
                }
                if (content.length > 256) {
                    _commentState.value = _commentState.value.copy(
                        error = "Comment is too long (max 256 characters)"
                    )
                    return@launch
                }
                if (chapterId.isBlank()) {
                    _commentState.value = _commentState.value.copy(
                        error = "Invalid chapter ID"
                    )
                    return@launch
                }
                Log.d(TAG, "Posting comment for chapter: $chapterId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                Log.d(TAG, "Token: ${token?.take(10)}...") // Only log first 10 chars for security

                if (token != null && chapterId.isNotEmpty()) {
                    val bearerToken = "Bearer $token"
                    val commentRequest = CommentRequest(chapter_id = chapterId,                     chapterId = chapterId, // Add this
                        content = content)
                    Log.d(TAG, "Comment request: chapter_id=${commentRequest.chapter_id}, content length=${commentRequest.content.length}")

                    val response = apiService.postComment(commentRequest, bearerToken)
                    Log.d(TAG, "Response code: ${response.code()}")
                    Log.d(TAG, "Response body: ${response.body()}")
                    Log.d(TAG, "Response error body: ${response.errorBody()?.string()}")
                    if (response.isSuccessful && response.body() != null) {
                        Log.d(TAG, "Successfully posted comment")
                        fetchComments(chapterId, dataStoreManager)
                    } else {
                        Log.e(TAG, "Error posting comment: ${response.message()}")
                        _commentState.value = _commentState.value.copy(
                            error = "Failed to post comment: ${response.message()}"
                        )
                    }
                } else {
                    Log.e(TAG, "Invalid chapterId or token is null")
                    _commentState.value = _commentState.value.copy(
                        error = "Invalid chapter ID or authentication"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while posting comment", e)
                _commentState.value = _commentState.value.copy(
                    error = when (e) {
                        is IOException -> "Network error: Check your connection"
                        is HttpException -> "Server error: ${e.message}"
                        else -> "Error posting comment: ${e.message}"
                    }
                )
            }
        }
    }

    fun likeComment(commentId: String, chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Liking comment: $commentId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    Log.d(TAG, "Making API call to like comment")
                    val response = apiService.likeComment(commentId, bearerToken)

                    if (response.isSuccessful && response.body() != null) {
                        Log.d(TAG, "Successfully liked comment")
                        fetchComments(chapterId, dataStoreManager)
                    } else {
                        Log.e(TAG, "Error liking comment: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "Token is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while liking comment", e)
                _commentState.value = _commentState.value.copy(
                    error = "Error liking comment: ${e.message}"
                )
            }
        }
    }

    fun deleteComment(commentId: String, chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Deleting comment: $commentId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    Log.d(TAG, "Making API call to delete comment")
                    val response = apiService.deleteComment(commentId, bearerToken)

                    if (response.isSuccessful ) {
                        Log.d(TAG, "Successfully deleted comment")
                        fetchComments(chapterId, dataStoreManager)
                    } else {
                        Log.e(TAG, "Error deleting comment: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "Token is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while deleting comment", e)
                _commentState.value = _commentState.value.copy(
                    error = "Error deleting comment: ${e.message}"
                )
            }
        }
    }
}

    data class CommentState(
        val loading: Boolean = true,
        val commentResponse: CommentResponse? = null,
        val error: String? = null
    )



