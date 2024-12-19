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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.TextField
import androidx.compose.runtime.MutableState


@Composable
fun ProfileScreen (
    onLogOut: () -> Job,
    onNavigateToBookmark: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToUpdateProfile: () -> Unit,
    onUpdateStatus: (String) -> Unit, // Add this parameter
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
                    userName = viewState.profileDataRes.body()?.userData?.userName ?: "N/A"
                )

                UserQuote(
                    quote = viewState.profileDataRes.body()?.userData?.status ?: "N/A",
                    onUpdateStatus = onUpdateStatus // Pass the callback to UserQuote
                )



                ProfileActions(
                    onNavigateToBookmark = onNavigateToBookmark,
                    onLogOut = onLogOut,
                    onNavigateToPremium = onNavigateToPremium,
                    onNavigateToUpdateProfile = onNavigateToUpdateProfile
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(fullName: String, userName: String) {
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
    }
}

@Composable
fun UserQuote(quote: String, onUpdateStatus: (String) -> Unit) {
//    var statusText = remember { mutableStateOf(quote.value) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Display the existing status
        Text(
            text = "$quote",
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Gray),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // TextField to edit the status
//        TextField(
//            value = quote,
//            onValueChange = { quote = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp)
//        )



    }
}


@Composable
fun ProfileActions(
    onNavigateToBookmark: () -> Unit,
    onLogOut: () -> Job,
    onNavigateToPremium: () -> Unit,
    onNavigateToUpdateProfile: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onNavigateToPremium() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFFDAA520),
                    shape = MaterialTheme.shapes.small
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow
            )
        ) {
            Text("VolaToon Premium", color = Color.Black)
        }

        Button(
            onClick = { onNavigateToBookmark() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF008B8B),
                    shape = MaterialTheme.shapes.small
                ),
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
                .padding(vertical = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF008B8B),
                    shape = MaterialTheme.shapes.small
                ),
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
                .padding(vertical = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF008B8B),
                    shape = MaterialTheme.shapes.small
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan
            )
        ) {
            Text("Logout", color = Color.Red)
        }
    }
}
