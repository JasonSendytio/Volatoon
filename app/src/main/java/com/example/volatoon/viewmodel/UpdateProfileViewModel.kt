package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.model.UpdateUserProfileResponse
import com.example.volatoon.model.UserResponse
import com.example.volatoon.model.apiService
import retrofit2.Response

class UpdateProfileViewModel : ViewModel() {
    private val _updateProfileState = mutableStateOf(UpdateProfileState())
    val updateProfileState : State<UpdateProfileState> = _updateProfileState

    fun updateUserProfile(
        token: String,
        updateUserProfile: UpdateUserProfile
    ) {
        viewModelScope.launch {
            _updateProfileState.value = _updateProfileState.value.copy(
                loading = true,
                error = null
            )
            try {
                val response = apiService.updateUserProfile("Bearer $token", updateUserProfile)
                _updateProfileState.value = _updateProfileState.value.copy(
                    loading = false,
                    data = response
                )
                Log.i("update profile", response.toString())
            } catch (e: Exception) {
                Log.e("update profile", e.toString())
                _updateProfileState.value = _updateProfileState.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    data class UpdateProfileState(
        val loading: Boolean? = false,
        val data: Response<UpdateUserProfileResponse>? = null,
        val error: String? = null
    )
}
