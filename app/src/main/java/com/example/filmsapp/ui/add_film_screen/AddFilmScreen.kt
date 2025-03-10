package com.example.filmsapp.ui.add_film_screen


import TextInput
import LoginButton
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.filmsapp.R
import com.example.filmsapp.data.Film
import com.example.filmsapp.ui.add_film_screen.data.AddFilmObject
import com.example.filmsapp.ui.theme.BgBoxColor
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


@Composable
fun AddFilmScreen(navData: AddFilmObject = AddFilmObject(), onSaved: () -> Unit = {}) {
    val cv = LocalContext.current.contentResolver
    val firestore = remember { Firebase.firestore }

    // Состояния для полей
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val year = remember { mutableStateOf("") }
    val genre = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf(navData.imageUrl) } // Сохраняем imageUrl из Firestore
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    // Загрузка данных по key из Firestore
    LaunchedEffect(navData.key) {
        if (navData.key.isNotEmpty()) {
            firestore.collection("films").document(navData.key)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        title.value = doc.getString("title") ?: ""
                        description.value = doc.getString("description") ?: ""
                        year.value = doc.getString("year") ?: ""
                        genre.value = doc.getString("genre") ?: ""
                        imageUrl.value = doc.getString("imageUrl") ?: ""
                    }
                }
                .addOnFailureListener { e -> Log.e("AddFilmScreen", "Ошибка: $e") }
        }
    }

    // Выбор изображения
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri.value = uri
    }

    // Подготовка изображения для отображения
    val bitmap = remember(imageUrl.value) {
        try {
            Base64.decode(imageUrl.value, Base64.DEFAULT)
                .let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        } catch (e: Exception) {
            null
        }
    }
    val painter = rememberAsyncImagePainter(
        model = selectedImageUri.value ?: bitmap ?: R.drawable.create_film_bg
    )

    // UI
    Image(
        painter = painter,
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillHeight,
        alpha = 0.2f
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBoxColor)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(70.dp)
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = "Add new film",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(10.dp))
        CustomDropDownMenu(defaultOption = genre.value) { genre.value = it }
        Spacer(Modifier.height(10.dp))
        TextInput(text = title.value, label = "Title", singleLine = false) { title.value = it }
        Spacer(Modifier.height(10.dp))
        TextInput(text = description.value, label = "Description", maxLines = 5) { description.value = it }
        Spacer(Modifier.height(10.dp))
        TextInput(text = year.value, label = "Year", singleLine = false) { year.value = it }
        Spacer(Modifier.height(10.dp))
        LoginButton("Select image") { imageLauncher.launch("image/*") }
        LoginButton("Save") {
            val finalImageUrl = selectedImageUri.value?.let { imageToBase64(it, cv) } ?: imageUrl.value
            saveFilmToFirestore(
                firestore,
                Film(
                    key = navData.key.ifEmpty { firestore.collection("films").document().id },
                    title = title.value,
                    description = description.value,
                    year = year.value,
                    genre = genre.value,
                    imageUrl = finalImageUrl
                ),
                onSave = onSaved,
                onError = { Log.e("AddFilmScreen", "Ошибка сохранения") }
            )
        }
    }
}

private fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String =
    contentResolver.openInputStream(uri)?.use { it.readBytes() }
        ?.let { Base64.encodeToString(it, Base64.DEFAULT) } ?: ""

private fun saveFilmToFirestore(
    firestore: FirebaseFirestore,
    film: Film,
    onSave: () -> Unit,
    onError: () -> Unit
) {
    firestore.collection("films").document(film.key)
        .set(film)
        .addOnSuccessListener { onSave() }
        .addOnFailureListener { onError() }
}