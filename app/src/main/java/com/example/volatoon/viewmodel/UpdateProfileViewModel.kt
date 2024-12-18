package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.volatoon.model.ApiService
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.model.UserResponse
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import retrofit2.Response

class UpdateProfileViewModel : ViewModel() {
    fun updateUserProfile(
        token: String,
        updateUserProfile: UpdateUserProfile
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.updateUserProfile("Bearer $token", updateUserProfile)
                Log.i("update profile", response.toString())

            } catch (e: Exception) {
                Log.e("update profile", e.toString())
            }
        }
    }


}
