package com.example.volatoon.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.ProfileResponse
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class ProfileViewModel: ViewModel() {

    private val _profileResState = mutableStateOf(ProfileResState())
    val profileResState : State<ProfileResState> = _profileResState

    fun fetchUserData(dataStoreManager : DataStoreManager) {

        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken

            if (token != null) {
                try {
                    val bearerToken = "Bearer $token" // Format the token as "Bearer <token>"
                    val response = apiService.getProfile(bearerToken)

                    _profileResState.value = _profileResState.value.copy(
                        loading = false,
                        profileDataRes = response,
                        error = null
                    )

                } catch (e : Exception){
                    _profileResState.value = _profileResState.value.copy(
                        loading = false,
                        error = "error fetching profile ${e.message}"
                    )
                }
            }
        }
    }

    data class ProfileResState(
        val loading : Boolean = true,
        val profileDataRes : Response<ProfileResponse>? = null,
        val error : String? = null
    )

}