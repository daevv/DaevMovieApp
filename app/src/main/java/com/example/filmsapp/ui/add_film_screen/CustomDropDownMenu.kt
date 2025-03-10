package com.example.filmsapp.ui.add_film_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDropDownMenu(defaultOption: String, onOptionSelected: (String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(defaultOption) }

    val categoryList = listOf(
        "Action",
        "Crime",
        "Series"
    )

    LaunchedEffect(defaultOption) {
        selectedOption.value =
            if (defaultOption.isNotEmpty()) defaultOption else categoryList.first()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Match TextInput padding
            .background(
                color = Color(0x33FFFFFF), // Semi-transparent white for frosted glass effect
                shape = RoundedCornerShape(12.dp) // Match TextInput rounded corners
            )
            .clickable { expanded.value = true }
            .padding(15.dp)
    ) {
        Text(
            text = selectedOption.value,
            color = Color(0xFF212121), // Match TextInput text color
            fontSize = 16.sp // Match TextInput font size
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .background(Color(0xE6FFFFFF)) // Slightly more opaque white for the dropdown
        ) {
            categoryList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = Color(0xFF212121), // Match TextInput text color
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        selectedOption.value = option
                        expanded.value = false
                        onOptionSelected(option)
                    },
                    modifier = Modifier
                        .background(Color(0xE6FFFFFF)) // Match dropdown background
                )
            }
        }
    }
}