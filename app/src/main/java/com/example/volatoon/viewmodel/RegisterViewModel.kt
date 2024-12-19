package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.RegisterData
import com.example.volatoon.model.apiService
import com.example.volatoon.model.authData
import com.example.volatoon.utils.DataStoreManager
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

    fun registerUser(registerData: RegisterData, dataStoreManager : DataStoreManager){
        viewModelScope.launch {
            try {
                val response = apiService.registerUser(registerData)

                if(response.code() != 201){
                    val errorBody : APIError = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        APIError::class.java
                    );
                    throw Exception(errorBody.message)
                }

                _registerState.value = _registerState.value.copy(
                    isRegister = true,
                    loading = false,
                    data = response.body(),
                    error = null
                )


            }catch (e : Exception){
                _registerState.value = _registerState.value.copy(
                    loading = false,
                    error = "${e.message}"
                )
            }
        }
    }





}