package com.example.volatoon.viewmodel

import android.util.Log
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
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ProfileViewModel: ViewModel() {
    private val _profileResState = mutableStateOf(ProfileResState())
    val profileResState : State<ProfileResState> = _profileResState

    private val _userData = mutableStateOf<CurrentUserData?>(null)
    val userData: State<CurrentUserData?> = _userData

    fun fetchUserData(dataStoreManager : DataStoreManager) {
        viewModelScope.launch {
            val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
            if (token != null) {
                try {
                    val bearerToken = "Bearer $token"
                    val response = apiService.getProfile(bearerToken)
                    Log.i("fetch user data", "in progress")

                    _profileResState.value = _profileResState.value.copy(
                        loading = false,
                        profileDataRes = response,
                        error = null
                    )

                    val userData = response.body()?.userData.let {
                        CurrentUserData(
                            fullName = it?.fullName ?: "",
                            userName = it?.userName ?: "",
                            email = it?.email ?: "",
                            status = it?.status ?: "",
                            ispremium = it?.ispremium ?: false,
                            premiumUntil = it?.premiumUntil?.let { isoDate ->
                                try {
                                    val zonedDateTime = ZonedDateTime.parse(isoDate) // Parse ISO 8601
                                    zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))
                                } catch (e: Exception){
                                    println("Error parsing premiumUntil: ${e.message}")
                                    null
                                }

                            }

                        )
                    }
                    _userData.value = userData

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

    data class CurrentUserData(
        val fullName : String?,
        val userName : String?,
        val email : String?,
        val status : String?,
        val ispremium: Boolean?,
        val premiumUntil: String?
    )
}