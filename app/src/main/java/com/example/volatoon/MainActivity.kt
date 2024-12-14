package com.example.volatoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.volatoon.ui.theme.VolatoonTheme
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.utils.preferenceDataStore
import com.example.volatoon.view.LoginScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            VolatoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dataStoreContext = LocalContext.current
                    val dataStoreManager = DataStoreManager(dataStoreContext)

                    VolatoonApp(navController, preferenceDataStore, dataStoreManager)
                }
            }
        }
    }
}