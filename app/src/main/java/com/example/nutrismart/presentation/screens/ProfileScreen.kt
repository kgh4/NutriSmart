package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.UserProfile
import com.example.nutrismart.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var dietCategory by remember { mutableStateOf("Balanced") }
    var weeklyBudget by remember { mutableStateOf("") }
    var availableMinutes by remember { mutableStateOf("") }
    
    var isDietDropdownExpanded by remember { mutableStateOf(false) }
    val dietCategories = listOf(
        "Vegan", "Vegetarian", "Pescatarian", "Flexitarian", "Keto", 
        "Low-Carb", "Atkins", "High-Protein", "Low-Fat", "Mediterranean", 
        "DASH", "Balanced", "Gluten-Free", "Dairy-Free", "Nut-Free", 
        "Low-Sodium", "Diabetic-Friendly", "Halal", "Kosher", "Weight Loss", 
        "Weight Gain", "Muscle Gain", "Cutting", "Intermittent Fasting"
    )

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(uiState.profile) {
        uiState.profile?.let {
            name = it.name
            dietCategory = it.dietCategory
            weeklyBudget = it.weeklyBudget.toString()
            availableMinutes = it.availableMinutesPerDay.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }

                    if (uiState.isSaved) {
                        Text(text = "Profile Saved!", color = MaterialTheme.colorScheme.primary)
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(2000)
                            viewModel.resetSavedState()
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = isDietDropdownExpanded,
                        onExpandedChange = { isDietDropdownExpanded = !isDietDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = dietCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Diet Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDietDropdownExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isDietDropdownExpanded,
                            onDismissRequest = { isDietDropdownExpanded = false }
                        ) {
                            dietCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        dietCategory = category
                                        isDietDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = weeklyBudget,
                        onValueChange = { weeklyBudget = it },
                        label = { Text("Weekly Budget (TND)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = availableMinutes,
                        onValueChange = { availableMinutes = it },
                        label = { Text("Available Minutes Per Day") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val updatedProfile = UserProfile(
                                id = uiState.profile?.id ?: "default_user",
                                name = name,
                                dietCategory = dietCategory,
                                dietType = dietCategory, // Keeping dietType for compatibility if needed
                                weeklyBudget = weeklyBudget.toDoubleOrNull() ?: 0.0,
                                availableMinutesPerDay = availableMinutes.toIntOrNull() ?: 0
                            )
                            viewModel.saveProfile(updatedProfile)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Profile")
                    }
                }
            }
        }
    }
}
