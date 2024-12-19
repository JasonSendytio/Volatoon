package com.example.volatoon.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.volatoon.R
import com.example.volatoon.viewmodel.ProfileViewModel
import kotlinx.coroutines.Job
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen (
    onLogOut: () -> Job,
    onNavigateToUserActivity: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToUpdateProfile: () -> Unit,
    onUpdateStatus: (String) -> Unit,
    viewState: ProfileViewModel.ProfileResState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED: ${viewState.error}")
                Button(onClick = { onLogOut() }) {
                    Text("LogOut", color = Color.Red)
                }
            }

            viewState.profileDataRes == null -> {
                Text(text = "No profile data available.")
            }

            else -> {
                ProfileHeader(
                    fullName = viewState.profileDataRes.body()?.userData?.fullName ?: "N/A",
                    userName = viewState.profileDataRes.body()?.userData?.userName ?: "N/A",
                    ispremium = viewState.profileDataRes.body()?.userData?.ispremium ?: false,
                    quote = viewState.profileDataRes.body()?.userData?.status ?: "N/A"
                )

                ProfileActions(
                    onNavigateToUserActivity = onNavigateToUserActivity,
                    onLogOut = onLogOut,
                    onNavigateToPremium = onNavigateToPremium,
                    onNavigateToUpdateProfile = onNavigateToUpdateProfile
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(fullName: String, userName: String, ispremium: Boolean, quote: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {

        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_avatar),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 8.dp)
        )


        Text(
            text = fullName,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                color = Color.Black
            )
        )


        Text(
            text = "@$userName",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                color = Color.Gray
            )
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (ispremium) Color(0x33FFD700) else Color(0x33FF0000))
                .border(
                    width = 1.dp,
                    color = if (ispremium) Color(0xFFFF9900) else Color.Red,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (ispremium) "✨ PREMIUM" else "FREE USER",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    color = if (ispremium) Color(0xFFFF9900) else Color.Red,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }

        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White)
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = quote,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileActions(
    onNavigateToUserActivity: () -> Unit,
    onLogOut: () -> Job,
    onNavigateToPremium: () -> Unit,
    onNavigateToUpdateProfile: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onNavigateToPremium() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow
            )
        ) {
            Text("VolaToon Premium", color = Color.Black)
        }

        Button(
            onClick = { onNavigateToUserActivity() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan
            )
        ) {
            Text("Bookmarks & History", color = Color.Black)
        }

        Button(
            onClick = { onNavigateToUpdateProfile() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan
            )
        ) {
            Text("Update Profile", color = Color.Black)
        }

        Button(
            onClick = { onLogOut() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text("Logout", color = Color.Red)
        }
    }
}
