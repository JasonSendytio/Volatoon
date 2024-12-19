package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.volatoon.viewmodel.UpdateProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.volatoon.R
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
    var status by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF3F4F6)), // Warna latar belakang
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp) // Ukuran avatar sesuai halaman profil
                .padding(top = 20.dp, bottom = 10.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape, // Menggunakan CircleShape untuk lingkaran
                color = Color(0xFFE0E0E0) // Warna latar belakang
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(120.dp) // Sesuaikan ukuran dengan Surface
                )
            }
        }

        // Nama dan username
        Box(
            modifier = Modifier
                .background(Color(0xFFa8eded), shape = RoundedCornerShape(15.dp))
                .padding(4.dp)

        ) {
            Text(
                text = "Biarkan Kosong Pada Data Yang Tidak Diubah",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(4.dp)
            )
        }


        CustomOutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name"
        )

        CustomOutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = "Username"
        )

        CustomOutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = "Status",
            height = 120.dp
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BCD4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Update Profile",
                color = Color.White
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

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    height: Dp = 56.dp
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(height),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color(0xFFF0F0F0),
            focusedBorderColor = Color.Cyan, // Highlight
            unfocusedBorderColor = Color.Black, // non Highlight
            textColor = Color.Black
        ),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 16.sp
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        )
    )
}

