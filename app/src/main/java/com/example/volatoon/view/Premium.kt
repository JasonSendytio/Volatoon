package com.example.volatoon.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volatoon.R

@Composable
fun VolatoonPremiumScreen() {
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
                        text = "Access to Exclue Comics"
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
                onClick = { /* Handle subscribe click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5D5FEF))
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
@Preview(showBackground = true)
fun VolatoonPremiumScreenPreview() {
    VolatoonPremiumScreen()
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
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(text)
    }
}

@Composable
@Preview
fun FeatureItemPreview() {
    FeatureItem(
        icon = R.drawable.ic_check,
        text = "Access to Exclue Comics"
    )
}