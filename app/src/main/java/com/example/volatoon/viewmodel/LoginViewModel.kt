package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Account
import com.example.volatoon.model.RegisterData
import com.example.volatoon.model.authData
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import com.google.gson.Gson
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.util.Log
import com.google.android.gms.common.api.Response


class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "LoginViewModel"
    private val GOOGLE_PASSWORD_PREFIX = "GOOGLE_AUTH_"

    data class LoginState(
        val isLogin: Boolean = false,
        val loading: Boolean = false,
        val data: authData? = null,
        val error: String? = null
    )

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    data class APIError(
        val status: Int,
        val message: String,
    )

    fun loginUser(accountData: Account, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                val response = apiService.loginUser(accountData)
                _loginState.value =
                    _loginState.value.copy(loading = true)

                if (response.code() != 200) {
                    val errorBody: APIError = Gson().fromJson(
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

            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "${e.message}"
                )
            }
        }
    }

    fun handleGoogleSignInResult(
        idToken: String,
        dataStoreManager: DataStoreManager
    ) {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                Log.d(TAG, "Starting Google Sign-In process with idToken: ${idToken.take(10)}...")

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    Log.e(TAG, "No Firebase user found after authentication")
                    throw Exception("No user signed in")
                }

                val email = currentUser.email ?: throw Exception("Email not found")
                val googlePassword = "${GOOGLE_PASSWORD_PREFIX}${currentUser.email}"

                // Coba login dulu dengan email yang ada
                try {
                    Log.d(TAG, "Attempting to login first with email: $email")
                    loginWithGoogle(email, googlePassword, dataStoreManager)
                } catch (e: Exception) {
                    // Jika login gagal, berarti user belum terdaftar
                    Log.d(TAG, "Login failed, attempting to register new user", e)

                    val registerData = RegisterData(
                        fullName = currentUser.displayName ?: "",
                        userName = email.split("@")[0],
                        email = email,
                        password = googlePassword
                    )

                    try {
                        val registerResponse = apiService.registerUser(registerData)
                        when (registerResponse.code()) {
                            201 -> {
                                Log.d(TAG, "Registration successful")
                                loginWithGoogle(email, googlePassword, dataStoreManager)
                            }
                            else -> {
                                val errorMessage = try {
                                    val errorBody: APIError = Gson().fromJson(
                                        registerResponse.errorBody()?.charStream(),
                                        APIError::class.java
                                    )
                                    errorBody.message
                                } catch (e: Exception) {
                                    "Registration failed: ${registerResponse.code()}"
                                }
                                throw Exception(errorMessage)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Registration failed", e)
                        _loginState.value = _loginState.value.copy(
                            loading = false,
                            error = "Registration failed: ${e.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In failed", e)
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "Google Sign-In failed: ${e.message}"
                )
            }
        }
    }





    private suspend fun loginWithGoogle(
        email: String,
        password: String,
        dataStoreManager: DataStoreManager
    ) {
        try {
            Log.d(TAG, "Attempting Google login for email: $email")

            val response = apiService.loginUser(Account(email, password))

            if (!response.isSuccessful) {
                val errorMessage = try {
                    val errorBody: APIError = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        APIError::class.java
                    )
                    errorBody.message
                } catch (e: Exception) {
                    "Login failed: ${response.code()}"
                }
                throw Exception(errorMessage)
            }

            response.body()?.let { authData ->
                _loginState.value = _loginState.value.copy(
                    isLogin = true,
                    loading = false,
                    data = authData,
                    error = null
                )
                dataStoreManager.saveToDataStore(authData.token)
            } ?: throw Exception("Empty response body")

        } catch (e: Exception) {
            Log.e(TAG, "Login error", e)
            _loginState.value = _loginState.value.copy(
                loading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
}



