import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.volatoon.model.ApiService
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.model.UserResponse
import retrofit2.Response

class UpdateProfileViewModel(private val apiService: ApiService) : ViewModel() {

    private val _updateStatus = MutableLiveData<Result<UserResponse>>()
    val updateStatus: LiveData<Result<UserResponse>> = _updateStatus

    fun updateUserProfile(
        token: String,
        updateUserProfile: UpdateUserProfile
    ) {
        viewModelScope.launch {
            try {
                // Call the API to update user profile
                val response = apiService.updateUserProfile("Bearer $token", updateUserProfile)
                if (response.isSuccessful) {
                    _updateStatus.postValue(Result.success(response.body()!!))
                } else {
                    _updateStatus.postValue(Result.failure(Exception("Error: ${response.errorBody()?.string()}")))
                }
            } catch (e: Exception) {
                _updateStatus.postValue(Result.failure(e))
            }
        }
    }
}
