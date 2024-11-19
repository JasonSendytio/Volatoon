package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Account
import com.example.volatoon.model.authData
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import com.google.gson.Gson
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel(){

    data class LoginState(
        val isLogin : Boolean = false,
        val loading : Boolean = false,
        val data : authData? = null,
        val error : String? = null
    )

    private val _loginState = mutableStateOf(LoginState())
    val loginState : State<LoginState> = _loginState

    data class APIError (
        val status : Int,
        val message : String,
    )

    fun loginUser(accountData : Account, dataStoreManager : DataStoreManager){
        viewModelScope.launch {
            try {
                val response = apiService.loginUser(accountData)

                if(response.code() != 200){
                    val errorBody : APIError = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        APIError::class.java
                    );
                    throw Exception(errorBody.message)
                }

                _loginState.value = _loginState.value.copy(
                    isLogin = true,
                    loading = false,
                    data = response.body(),
                    error = null
                )

                dataStoreManager.saveToDataStore(response.body()!!.token)

            }catch (e : Exception){
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "${e.message}"
                )
            }
        }
    }


}