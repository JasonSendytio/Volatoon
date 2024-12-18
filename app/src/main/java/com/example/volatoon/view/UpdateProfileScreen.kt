package com.example.volatoon.view

import android.util.Log
import com.example.volatoon.viewmodel.UpdateProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.utils.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun UpdateProfileScreen(
    viewModel: UpdateProfileViewModel,
    dataStoreManager: DataStoreManager,
    navigateToProfile: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

//        // Avatar
//        Image(
//            painter = painterResource(id = R.drawable.placeholder), // Ganti dengan resource valid
//            contentDescription = "Avatar",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(100.dp)
//                .padding(bottom = 16.dp)
//        )

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
                isLoading = true
                scope.launch {
                    val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                    if (token != null) {
                        viewModel.updateUserProfile(
                            token,
                            updateUserProfile = UpdateUserProfile(fullName, userName, status)
                        )
                        navigateToProfile()
                    }
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Atur tinggi tombol
            colors = ButtonDefaults.buttonColors() // Gunakan default saja, tanpa warna custom
        ) {
            Text(
                text = "Continue",
                color = Color.White // Warna teks tetap putih agar kontras
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
