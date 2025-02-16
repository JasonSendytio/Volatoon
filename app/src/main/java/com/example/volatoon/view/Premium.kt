package com.example.volatoon.view

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.volatoon.R
import com.example.volatoon.viewmodel.ProfileViewModel

@Composable
fun VolatoonPremiumScreen(
    navigateToRedemption : (String) -> Unit,
    profileViewModel: ProfileViewModel
) {
    val currentUserData by profileViewModel.userData
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFC0E0E0))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Volatoon Logo",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Premium",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Rp15.000/month",
                style = TextStyle(
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    FeatureItem(
                        icon = R.drawable.ic_check,
                        text = "Access to Exclusive Comics"
                    )
                    FeatureItem(
                        icon = R.drawable.ic_check,
                        text = "Access stories in all genres"
                    )
                    FeatureItem(
                        icon = R.drawable.ic_check,
                        text = "No Ads"
                    )
                }
            }

            Button(
                onClick = {
                    if (currentUserData?.isPremium == false) {
                        navigateToRedemption("premiumredemption")
                    }
                    else if (currentUserData?.isPremium == true) {
                        Toast.makeText(context, "You already have a premium account", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Subscribe",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FeatureItem(
    @DrawableRes icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Features",
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(text)
    }
}
