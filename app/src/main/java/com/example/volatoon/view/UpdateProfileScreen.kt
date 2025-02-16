package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.volatoon.viewmodel.UpdateProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volatoon.R
import com.example.volatoon.model.UpdateUserProfile
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


@Composable
fun UpdateProfileScreen(
    viewModel: UpdateProfileViewModel,
    dataStoreManager: DataStoreManager,
    profileViewModel: ProfileViewModel,
    navigateToProfile: () -> Unit
)
{
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
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp) // Ukuran avatar sesuai halaman profil
                .padding(top = 20.dp, bottom = 10.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium, // Rounded corners for the avatar
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(120.dp)
                )
            }
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
                scope.launch {
                    val token = dataStoreManager.getFromDataStore().firstOrNull()?.authToken
                    if (token != null) {
                        viewModel.updateUserProfile(
                            token,
                            updateUserProfile = UpdateUserProfile(fullName, userName, status)
                        )
                        navigateToProfile()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White
            )
        }

        if (viewModel.updateProfileState.value.loading == true) {
            Text(
                text = "Updating...",
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        viewModel.updateProfileState.value.data?.message().let {
            if (it != null) {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }

        viewModel.updateProfileState.value.error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    height: Dp = 64.dp
) {
    val isDarkTheme = isSystemInDarkTheme()
    val borderColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.outline // Use outline for light theme border
    }
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = textColor) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(height),
        colors = OutlinedTextFieldDefaults.colors( // Use colors() here
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = textColor
            // containerColor is not available in colors()
        ),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp
        ),
        singleLine = false,
        maxLines = if (height > 64.dp) Int.MAX_VALUE else 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        )
    )
}

