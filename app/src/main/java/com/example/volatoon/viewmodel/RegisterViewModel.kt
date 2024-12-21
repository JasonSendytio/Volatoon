package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.RegisterData
import com.example.volatoon.model.apiService
import com.example.volatoon.model.authData
import com.example.volatoon.viewmodel.LoginViewModel.APIError
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    data class RegisterState(
        val isRegister : Boolean = false,
        val loading : Boolean = false,
        val data : authData? = null,
        val error : String? = null
    )

    private val _registerState = mutableStateOf(RegisterState())
    val registerState : State<RegisterState> = _registerState

    fun registerUser(registerData: RegisterData){
        viewModelScope.launch {
            try {
                _registerState.value = _registerState.value.copy(
                    loading = true,
                    error = null
                )
                val response = apiService.registerUser(registerData)
                if (response.code() != 201) {
                    val errorBodyString = response.errorBody()?.string() ?: "No error body received."
                    val errorMessage = try {
                        val apiError = Gson().fromJson(errorBodyString, APIError::class.java)
                        apiError.message
                    } catch (e: Exception) {
                        errorBodyString.ifEmpty {
                            "Unknown error occurred"
                        }
                    }
                    throw Exception(errorMessage)
                }
                _registerState.value = _registerState.value.copy(
                    isRegister = true,
                    loading = false,
                    data = response.body(),
                    error = null
                )
            } catch (e : Exception){
                _registerState.value = _registerState.value.copy(
                    loading = false,
                    error = "${e.message}"
                )
            }
        }
    }

    fun setMessage(message: String) {
        viewModelScope.launch {
            _registerState.value = _registerState.value.copy(
                error = message
            )
        }
    }
}
