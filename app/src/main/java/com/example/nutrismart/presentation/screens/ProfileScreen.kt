package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.presentation.viewmodel.ProfileViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    appViewModel: AppViewModel,
    onNavigateToSavedRecipes: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    if (showEditDialog) {
        EditProfileDialog(
            profile = uiState.profile,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedProfile ->
                viewModel.saveProfile(updatedProfile)
                showEditDialog = false
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF16A34A))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
            ) {
                ProfileHeader(
                    name = appViewModel.userName,
                    email = appViewModel.userEmail,
                    onEditClick = { showEditDialog = true }
                )

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-30).dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(Modifier.weight(1f), "${appViewModel.savedRecipes.size}", "Recipes Saved", onClick = onNavigateToSavedRecipes)
                        StatCard(Modifier.weight(1f), "12", "Weeks Planned")
                        StatCard(Modifier.weight(1f), "${uiState.profile?.weeklyBudget?.toInt() ?: 0} TND", "Avg. Weekly")
                    }

                    ProfileSectionTitle("Preferences")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column {
                            ProfileMenuItem(
                                icon = Icons.Default.Person,
                                iconColor = Color(0xFF16A34A),
                                title = "Dietary Restrictions",
                                subtitle = uiState.profile?.dietCategory ?: "Balanced"
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                            ProfileMenuItem(
                                icon = Icons.Default.Language,
                                iconColor = Color(0xFF3B82F6),
                                title = "Cooking Skill Level",
                                subtitle = uiState.profile?.cookingSkill ?: "Beginner"
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    ProfileSectionTitle("Budget & Goals")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column {
                            ProfileMenuItem(
                                icon = Icons.Default.AccountBalanceWallet,
                                iconColor = Color(0xFF9B59B6),
                                title = "Weekly Budget",
                                subtitle = "${uiState.profile?.weeklyBudget?.toInt() ?: 50} TND"
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                            ProfileMenuItem(
                                icon = Icons.Default.Favorite,
                                iconColor = Color(0xFFEF4444),
                                title = "Calorie Goal",
                                subtitle = "${uiState.profile?.availableMinutesPerDay ?: 2000} cal/day"
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    ProfileSectionTitle("Support")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Default.Logout,
                            iconColor = Color(0xFFEF4444),
                            title = "Log Out",
                            onClick = onLogout,
                            showArrow = true
                        )
                    }

                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    profile: com.example.nutrismart.domain.model.UserProfile?,
    onDismiss: () -> Unit,
    onConfirm: (com.example.nutrismart.domain.model.UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var diet by remember { mutableStateOf(profile?.dietCategory ?: "Balanced") }
    var skill by remember { mutableStateOf(profile?.cookingSkill ?: "Beginner") }
    var budget by remember { mutableStateOf(profile?.weeklyBudget?.toString() ?: "50") }
    var calories by remember { mutableStateOf(profile?.availableMinutesPerDay?.toString() ?: "2000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                
                Text("Dietary Preference", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                val dietOptions = listOf("Balanced", "Vegetarian", "Vegan", "Keto")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    dietOptions.forEach { option ->
                        FilterChip(
                            selected = diet == option,
                            onClick = { diet = option },
                            label = { Text(option, fontSize = 10.sp) }
                        )
                    }
                }

                Text("Cooking Skill", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                val skillOptions = listOf("Beginner", "Intermediate", "Expert")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    skillOptions.forEach { option ->
                        FilterChip(
                            selected = skill == option,
                            onClick = { skill = option },
                            label = { Text(option, fontSize = 10.sp) }
                        )
                    }
                }

                OutlinedTextField(value = budget, onValueChange = { budget = it }, label = { Text("Weekly Budget (TND)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Daily Calorie Goal") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedProfile = profile?.copy(
                        name = name,
                        dietCategory = diet,
                        cookingSkill = skill,
                        weeklyBudget = budget.toDoubleOrNull() ?: 50.0,
                        availableMinutesPerDay = calories.toIntOrNull() ?: 2000
                    ) ?: com.example.nutrismart.domain.model.UserProfile(
                        name = name,
                        dietCategory = diet,
                        cookingSkill = skill,
                        weeklyBudget = budget.toDoubleOrNull() ?: 50.0,
                        availableMinutesPerDay = calories.toIntOrNull() ?: 2000
                    )
                    onConfirm(updatedProfile)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ProfileHeader(name: String, email: String, onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color(0xFF16A34A))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, value: String, label: String, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {},
    showArrow: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E))
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 13.sp, color = Color.Gray)
            }
        }

        if (showArrow) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1A1C1E),
        modifier = Modifier.padding(vertical = 16.dp)
    )
}
