package com.example.volatoon.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.volatoon.utils.DataStoreManager
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.PremiumPost
import com.example.volatoon.model.PremiumResponse
import com.example.volatoon.model.apiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PremiumRedemptionViewModel: ViewModel() {
    data class RedeemState (
        val loading: Boolean = false,
        val error: String? = null,
        val responseData: PremiumResponse? = null
    )

    private val _redeemState = mutableStateOf(RedeemState())
    val redeemState: State<RedeemState> = _redeemState

    fun redeemPremium(dataStoreManager: DataStoreManager, code: String) {
        _redeemState.value = _redeemState.value.copy(
            loading = true,
            error = null
        )
        viewModelScope.launch {
            try {
                val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                if (token != null) {
                    val response = apiService.redeemPremium("Bearer $token", PremiumPost(code))
                    _redeemState.value = _redeemState.value.copy(
                        responseData = response,
                        loading = false
                    )
                    Log.i("response", response.toString())
                }
            } catch (e: Exception) {
                _redeemState.value = _redeemState.value.copy(
                    error = e.message,
                    loading = false
                )
                Log.e("error", e.message.toString())
            }
        }
    }
}