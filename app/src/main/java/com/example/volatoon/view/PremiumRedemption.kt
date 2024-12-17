package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volatoon.R
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.PremiumRedemptionViewModel

@Composable
fun PremiumRedemptionScreen(
    premiumRedemptionViewModel: PremiumRedemptionViewModel,
    dataStoreManager: DataStoreManager,
){
    val premiumViewState by premiumRedemptionViewModel.redeemState
    var code by remember { mutableStateOf("") }
    LaunchedEffect(premiumViewState) {
        Log.i("response", premiumViewState.toString())
    }
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
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Enter Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = { premiumRedemptionViewModel.redeemPremium(dataStoreManager, code) },
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
            if (premiumViewState.loading) {
                Text(
                    text = "Processing...",
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            premiumViewState.responseData?.let {
                Text(
                    text = it.message,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            premiumViewState.error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}