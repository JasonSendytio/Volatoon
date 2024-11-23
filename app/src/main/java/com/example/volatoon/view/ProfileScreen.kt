package com.example.volatoon.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.viewmodel.ProfileViewModel
import kotlinx.coroutines.Job


@Composable
fun ProfileScreen(
    onLogOut :  () -> Job,
    onNavigateToBookmark : () -> Unit,
    viewState : ProfileViewModel.ProfileResState
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    progress = 0.89f,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED ${viewState.error}")

                Button(
                    onClick = {onLogOut()}
                ) {
                    Text("LogOut", color = Color.Red)
                }
            }

            viewState.profileDataRes == null -> {
                Text(text = "No comic details available.")
            }

            else -> {

                Text("Profile Screen")

                Text("FullName :  ${viewState.profileDataRes.body()?.userData?.fullName}")
                Text("Username : ${viewState.profileDataRes.body()?.userData?.userName}")
                Text("Email : ${viewState.profileDataRes.body()?.userData?.email}")

                Button(
                    onClick = {onNavigateToBookmark()}
                ) {
                    Text("Bookmark", color = Color.Yellow)
                }

                Button(
                    onClick = {onLogOut()}
                ) {
                    Text("LogOut", color = Color.Red)
                }
            }
        }
    }




}