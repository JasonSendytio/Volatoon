package com.example.volatoon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.volatoon.R
import com.example.volatoon.model.Account
import com.example.volatoon.model.RegisterData
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToLogin : () -> Unit,
    dataStoreManager : DataStoreManager,
    viewModel: RegisterViewModel,
    loginViewModel : LoginViewModel,
    navigateToDashboard : () -> Unit
){
    var userName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val viewState = viewModel.registerState.value
        val loginState = loginViewModel.loginState.value

        when {
            viewState.isRegister -> {
                loginViewModel.loginUser(Account(email, password.value), dataStoreManager)

                if(loginState.isLogin){
                    navigateToDashboard()
                }
            }

            viewState.loading -> {
                CircularProgressIndicator()
            }

            else -> {
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

                if(viewState.error != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic),
                        color = Color.Red,
                        fontSize = 20.sp,
                        text = "${viewState.error}")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = userName,
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    onValueChange = {userName = it},
                    label = { Text("Username") })

                OutlinedTextField(
                    value = fullName,
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    onValueChange = {fullName = it},
                    label = { Text("Full Name") })

                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    label = { Text("Email") })

                PasswordTextField("Password", password)
                PasswordTextField("Confirm Password", confirmPassword)

                Text(
                    color = Color.Blue,
                    modifier = Modifier.padding(5.dp)
                        .clickable { navigateToLogin() },
                    text = "Already have an account?"
                )

                Button(
                    onClick = {
                        println("Password: ${password.value}")
                        viewModel.registerUser(RegisterData(fullName, userName, email, password.value), dataStoreManager)
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                ){
                    Text(text = "Register")
                }
            }
        }
    }
}
