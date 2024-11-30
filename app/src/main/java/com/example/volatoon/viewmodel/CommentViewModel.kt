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

class CommentViewModel : ViewModel() {
    private val _commentState = mutableStateOf(CommentState())
    val commentState: State<CommentState> = _commentState

    fun fetchComments(chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val response = apiService.getComments(chapterId, bearerToken)

                    _commentState.value = _commentState.value.copy(
                        loading = false,
                        commentResponse = response,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _commentState.value = _commentState.value.copy(
                    loading = false,
                    error = "Error fetching comments: ${e.message}"
                )
            }
        }
    }

    fun postComment(komikId: String, content: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d("CommentViewModel", "Attempting to post comment to komik: $komikId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val commentRequest = CommentRequest(komik_id = komikId, content = content)
                    val response = apiService.postComment(commentRequest, bearerToken)
                    fetchComments(komikId, dataStoreManager)
                }
            } catch (e: Exception) {
                Log.e("CommentViewModel", "Error posting comment: ${e.message}", e)
                _commentState.value = _commentState.value.copy(
                    error = "Error posting comment: ${e.message}"
                )
            }
        }
    }

    fun likeComment(commentId: String, chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d("CommentViewModel", "Attempting to like comment: $commentId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val response = apiService.likeComment(commentId, bearerToken)
                    Log.d("CommentViewModel", "Like comment response: ${response.message}")

                    fetchComments(chapterId, dataStoreManager)
                }
            } catch (e: Exception) {
                Log.e("CommentViewModel", "Error liking comment: ${e.message}", e)
                _commentState.value = _commentState.value.copy(
                    error = "Error liking comment: ${e.message}"
                )
            }
        }
    }

    fun deleteComment(commentId: String, chapterId: String, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                Log.d("CommentViewModel", "Attempting to delete comment: $commentId")
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val response = apiService.deleteComment(commentId, bearerToken)
                    Log.d("CommentViewModel", "Delete comment response: ${response.message}")

                    fetchComments(chapterId, dataStoreManager)
                }
            } catch (e: Exception) {
                Log.e("CommentViewModel", "Error deleting comment: ${e.message}", e)
                _commentState.value = _commentState.value.copy(
                    error = "Error deleting comment: ${e.message}"
                )
            }
        }
    }
    data class CommentState(
        val loading: Boolean = true,
        val commentResponse: CommentResponse? = null,
        val error: String? = null
    )
}


