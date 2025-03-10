package com.example.filmsapp.ui.settings_screen

import CustomButton
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.filmsapp.R
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.filmsapp.ui.login.data.LoginScreenObject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen(
    email: String,
    uid: String,
    navController: NavController,
    db: FirebaseFirestore
) {
    val isAdminState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        db.collection("admin")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                isAdminState.value = document.get("isAdmin") as Boolean
            }
    }

    // Диалог подтверждения удаления
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteAccount(uid, db) {
                            // После успешного удаления переходим на экран логина
                            navController.navigate(LoginScreenObject) {
                                popUpTo(0) { inclusive = true } // Очищаем весь стек навигации
                            }
                        }
                        showDialog.value = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = R.drawable.ic_default_user,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.titleLarge
        )

        if (!isAdminState.value) {
            Spacer(modifier = Modifier.height(32.dp))
            CustomButton(text = "Delete Account") { showDialog.value = true }

        }
    }
}

private fun deleteAccount(
    uid: String,
    db: FirebaseFirestore,
    onSuccess: () -> Unit,
) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null && currentUser.uid == uid) {
        db.collection("users")
            .document(uid)
            .delete()
            .addOnSuccessListener {
                currentUser.delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.d("MyTag", "Failed to delete auth user: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.d("MyTag", "Failed to delete account data: ${e.message}")
            }
    } else {
        Log.d("MyTag", "User not authenticated")
    }
}