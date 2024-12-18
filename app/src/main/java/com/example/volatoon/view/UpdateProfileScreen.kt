import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volatoon.R
import com.example.volatoon.model.UpdateUserProfile
import kotlinx.coroutines.launch

@Composable
fun UpdateProfileScreen(
    viewModel: UpdateProfileViewModel = viewModel(),
    token: String,
    onUpdateSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val updateStatus = viewModel.updateStatus.observeAsState() // Ubah ke LiveData.observeAsState

    var fullName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Simulasi data awal (Ambil data dari server/database)
    LaunchedEffect(Unit) {
        fullName = "Mulyono Darsono"
        userName = "lyono_2014"
        email = "lyono@gmail.com"
        status = "Belum ada status"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "Edit Profile",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

//        // Avatar
//        Image(
//            painter = painterResource(id = R.drawable.placeholder), // Ganti dengan resource valid
//            contentDescription = "Avatar",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(100.dp)
//                .padding(bottom = 16.dp)
//        )

        // Kolom Full Name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Kolom Username
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Kolom Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        // Kolom Status
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 16.dp),
            maxLines = 4
        )

        // Tombol Update
        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    viewModel.updateUserProfile(
                        token = token,
                        updateUserProfile = UpdateUserProfile(fullName, userName, email, status)
                    )
                    isLoading = false
                    onUpdateSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Atur tinggi tombol
            colors = ButtonDefaults.buttonColors() // Gunakan default saja, tanpa warna custom
        ) {
            Text(
                text = "Continue",
                color = Color.White // Warna teks tetap putih agar kontras
            )
        }


        // Loading Indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        // Menampilkan Error
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    // Observasi hasil update
    updateStatus.value?.let { result ->
        result.onSuccess {
            errorMessage = null
        }.onFailure { error ->
            errorMessage = error.message
        }
    }
}
