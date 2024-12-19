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
import com.example.volatoon.model.UpdatePasswordRequest
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "LoginViewModel"
    private val GOOGLE_PASSWORD_PREFIX = "GOOGLE_AUTH_"

    data class LoginState(
        val isLogin: Boolean = false,
        val loading: Boolean = false,
        val data: authData? = null,
        val error: String? = null,
        val isGoogleSignIn: Boolean = false
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
                _loginState.value =
                    _loginState.value.copy(loading = true)
                val response = apiService.loginUser(accountData)

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
                fun generateGooglePassword(email: String): String {
                    // Get first part of email (before @)
                    val username = email.split("@")[0]
                    return "G${username}123#".take(20)
                }
                val googlePassword = generateGooglePassword(currentUser.email ?: "")

                val email = currentUser.email ?: throw Exception("Email not found")
                Log.d(TAG, "User email: $email")
                Log.d(TAG, "DATASTORE: ${dataStoreManager.getFromDataStore().toString()}")
                //val googlePassword = "${GOOGLE_PASSWORD_PREFIX}${currentUser.email}"
                val userExists = checkUserExists(email)
                if (userExists) {
                    // User exists but login failed, try updating password
                    updatePasswordAndLogin(email, googlePassword, dataStoreManager)
                } else {
                    // Only register if user doesn't exist
                    registerNewUser(currentUser, googlePassword, dataStoreManager)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In failed", e)
                Log.e(TAG, "Failed to update password: ${e.message}")
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "Google Sign-In failed: ${e.message}"
                )
            }
                        }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        return try {
            val response = apiService.findUserByEmail(email)
            response.isSuccessful && response.body()?.userData == email
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user existence: ${e.message}")
            false
        }
    }

    private suspend fun registerNewUser(currentUser: FirebaseUser, googlePassword: String, dataStoreManager: DataStoreManager) {
        val registerData = RegisterData(
            fullName = currentUser.displayName ?: "",
            userName = currentUser.email?.split("@")?.get(0) ?: "",
            email = currentUser.email ?: "",
            password = googlePassword
        )
        val registerResponse = apiService.registerUser(registerData)
        if (registerResponse.code() == 201) {
            Log.d(TAG, "Registration successful")
            loginWithGoogle(currentUser.email ?: "", googlePassword, dataStoreManager)
        } else {
            val errorMessage = Gson().fromJson(
                registerResponse.errorBody()?.charStream(),
                APIError::class.java
            ).message
            throw Exception(errorMessage)
        }
    }

    private suspend fun updatePasswordAndLogin(email: String, newPassword: String, dataStoreManager: DataStoreManager) {
        try {
            // Create UpdatePasswordRequest object
            val updatePasswordRequest = UpdatePasswordRequest(
                email = email,
                newPassword = newPassword
            )
            Log.d(TAG, newPassword)
            // Update the password in your backend
            val updateResponse = apiService.updatePassword(updatePasswordRequest)

            if (!updateResponse.isSuccessful) {
                val errorMessage = try {
                    val errorBody: APIError = Gson().fromJson(
                        updateResponse.errorBody()?.charStream(),
                        APIError::class.java
                    )
                    errorBody.message
                } catch (e: Exception) {
                    "Update password failed: ${updateResponse.code()}"
                }
                throw Exception(errorMessage)
            }

            // If password update is successful, proceed with login
            loginWithGoogle(email, newPassword, dataStoreManager)
        } catch (e: Exception) {
            Log.e(TAG, "Password update failed: ${e.message}")
            throw Exception("Failed to update password: ${e.message}")
        }
    }

    private suspend fun loginWithGoogle(email: String, password: String, dataStoreManager: DataStoreManager) {
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




