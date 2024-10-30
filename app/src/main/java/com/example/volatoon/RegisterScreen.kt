package com.example.volatoon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(){
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .height(220.dp)
                .width(220.dp),
            contentDescription = null)
        Text(
            style = TextStyle(fontWeight = FontWeight.Bold),
            color = Color.Black,
            fontSize = 35.sp,
            text = "Register")
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = text,
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            onValueChange = {text = it},
            label = { Text("Name") })

        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            label = { Text("Email") })

        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            label = { Text("Password") })

        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            label = { Text("Confirm Password") })

        Text(
            color = Color(0xFFA2D7E2),
            modifier = Modifier.padding(5.dp),
            text = "Already have an account ?")

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA2D7E2)
            ),
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        ){
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen(){
    RegisterScreen()
}