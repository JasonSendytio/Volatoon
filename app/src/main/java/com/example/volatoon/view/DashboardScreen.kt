package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volatoon.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),

    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                modifier = Modifier.width(50.dp).height(50.dp),
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "test"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hi, VVolatile!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Image(
            modifier = Modifier.fillMaxWidth()
                .width(384.dp).height(120.dp),
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "banner image"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF04FFFB), // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("All")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("Sci-fi")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("Fantasy")
            }

            Button(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(50.dp) // Adjust corner radius as needed
                ),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color to #04FFFB
                    contentColor = Color.Black // Sets the text color to black
                ),
                shape = RoundedCornerShape(45.dp)
            ) {
                Text("Romance")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Featured Comics!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp)

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen(){
    DashboardScreen()
}