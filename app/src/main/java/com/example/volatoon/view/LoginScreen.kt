package com.example.volatoon.view

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.R
import com.example.volatoon.model.Account
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.utils.GoogleAuthManager
import com.google.android.gms.common.api.ApiException
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToDashboard : () -> Unit,
    dataStoreManager: DataStoreManager,
    navigateToRegister : () -> Unit
){
    val context = LocalContext.current
    val googleAuthManager = remember { GoogleAuthManager(context) }
    val loginViewModel : LoginViewModel = viewModel()
    val viewState by loginViewModel.loginState
   val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.let { token ->
                        val credential = GoogleAuthProvider.getCredential(token, null)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    loginViewModel.handleGoogleSignInResult(token, dataStoreManager)
                                } else {
                                    Log.e("LoginScreen", "Firebase Auth failed", authTask.exception)
                                }
                            }
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("LoginScreen", "Google Sign-In canceled by user")

                }
                else -> {
                    Log.e("LoginScreen", "Google Sign-In failed: resultCode = ${result.resultCode}")
                }
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google Sign-In failed", e)
        }
    }
    var email by remember { mutableStateOf("") }
    val password =  remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when{
            viewState.isLogin ->{
                navigateToDashboard()
            }

            viewState.loading -> {
                CircularProgressIndicator()
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

//                OutlinedTextField(
//                    value = password,
//                    onValueChange = {password = it},
//                    modifier = Modifier
//                        .padding(10.dp)
//                        .fillMaxWidth(),
//                    label = { Text("Password") },
//
//                )

                PasswordTextField("Password", password)

                Text(
                    color = Color(0xFFA2D7E2),
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable { navigateToRegister() },
                    text = "Don't have an account? Sign Up Here",

                )

                Button(
                    onClick = { loginViewModel.loginUser(Account(email, password.value), dataStoreManager)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA2D7E2)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ){
                    Text(text = "Login")
                }
                Button(
                    onClick = {
                        launcher.launch(googleAuthManager.getSignInIntent())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(50.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google), // Add Google icon

                            contentDescription = "Google Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in with Google",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            }
        }


    }


@Composable
fun PasswordTextField(
    labelText : String,
    data : MutableState<String>
){
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

    OutlinedTextField(
        value = data.value,
        onValueChange = {
            data.value = it
        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
//        placeholder = { Text(text = labelText) },
        label = { Text(text = labelText) },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    painter = icon,
                    contentDescription = "Visibility Icon"
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){

}