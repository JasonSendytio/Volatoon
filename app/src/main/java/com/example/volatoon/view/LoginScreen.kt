package com.example.volatoon.view

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.R
import com.example.volatoon.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(){

    val loginViewModel : LoginViewModel = viewModel()
    val viewState by loginViewModel.loginState

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when{
            viewState.isLogin ->{
//                DashboardScreen()
            }

            viewState.loading -> {
                CircularProgressIndicator(progress = 0.89f)
            }

            else ->{

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
                    text = "Login")
                Spacer(modifier = Modifier.height(10.dp))

                if(viewState.error != null) {
                    Text(
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic),
                        color = Color.Red,
                        fontSize = 20.sp,
                        text = "${viewState.error}")
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    label = { Text("Email") })

                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    label = { Text("Password") })

                Text(
                    color = Color(0xFFA2D7E2),
                    modifier = Modifier.padding(5.dp),
                    text = "Don't have an account? Sign Up Here")

                Button(
                    onClick = { loginViewModel.loginUser(User(email, password))},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA2D7E2)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ){
                    Text(text = "Login")
                }

            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    LoginScreen()
}