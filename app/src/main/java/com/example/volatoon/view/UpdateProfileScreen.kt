package com.example.volatoon.view

import com.example.volatoon.viewmodel.UpdateProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun UpdateProfileScreen(
    viewModel: UpdateProfileViewModel,
    dataStoreManager: DataStoreManager,
    navigateToProfile: () -> Unit,
    profileViewModel: ProfileViewModel
) {
    val scope = rememberCoroutineScope()
    val currentUserData by profileViewModel.userData
    var fullName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    LaunchedEffect(currentUserData) {
        currentUserData?.let {
            fullName = it.fullName ?: ""
            userName = it.userName ?: ""
            status = it.status ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Profile",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 16.dp),
            maxLines = 4
        )

        Button(
            onClick = {
                scope.launch {
                    val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                    if (token != null) {
                        viewModel.updateUserProfile(
                            token,
                            updateUserProfile = UpdateUserProfile(fullName, userName, status)
                        )

                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Atur tinggi tombol
            colors = ButtonDefaults.buttonColors() // Gunakan default saja, tanpa warna custom
        ) {
            Text(
                text = "Save",
                color = Color.White // Warna teks tetap putih agar kontras
            )
        }

        (viewModel.updateProfileState.value.data?.message() ?: viewModel.updateProfileState.value.error)?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }
    }
}
