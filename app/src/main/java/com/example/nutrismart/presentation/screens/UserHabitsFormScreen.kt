package com.example.nutrismart.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.presentation.viewmodel.AppViewModel

data class UserHabits(
    val dietType: String = "",
    val allergies: List<String> = emptyList(),
    val cookingLevel: String = "",
    val budget: Double = 0.0,
    val mealsPerDay: Int = 3,
    val preferences: List<String> = emptyList()
)

@Composable
fun UserHabitsFormScreen(
    appViewModel: AppViewModel,
    onComplete: (UserHabits) -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var dietType by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf(setOf<String>()) }
    var cookingLevel by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var mealsPerDay by remember { mutableStateOf("3") }
    var preferences by remember { mutableStateOf(setOf<String>()) }

    val dietOptions = listOf("Vegetarian", "Vegan", "Balanced", "Keto", "Low Carb")
    val allergyOptions = listOf("Nuts", "Dairy", "Gluten", "Eggs", "Shellfish", "Soy")
    val cookingLevels = listOf("Beginner", "Intermediate", "Advanced")
    val preferenceOptions = listOf("Organic", "Local", "Budget-friendly", "Quick meals", "Healthy")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with progress
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Setup Your Profile",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { step.toFloat() / 6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Step $step of 6",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                when (step) {
                    1 -> FormStep1DietType(dietOptions, dietType) { dietType = it }
                    2 -> FormStep2Allergies(allergyOptions, allergies) { allergies = it }
                    3 -> FormStep3CookingLevel(cookingLevels, cookingLevel) { cookingLevel = it }
                    4 -> FormStep4Budget(budget) { budget = it }
                    5 -> FormStep5Meals(mealsPerDay) { mealsPerDay = it }
                    6 -> FormStep6Preferences(preferenceOptions, preferences) { preferences = it }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back")
                        }
                    }

                    Button(
                        onClick = {
                            if (step < 6) {
                                step++
                            } else {
                                val habits = UserHabits(
                                    dietType = dietType,
                                    allergies = allergies.toList(),
                                    cookingLevel = cookingLevel,
                                    budget = budget.toDoubleOrNull() ?: 0.0,
                                    mealsPerDay = mealsPerDay.toIntOrNull() ?: 3,
                                    preferences = preferences.toList()
                                )
                                onComplete(habits)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                    ) {
                        Text(if (step < 6) "Next" else "Complete")
                    }
                }
            }
        }
    }
}

@Composable
fun FormStep1DietType(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    FormStepContainer(
        title = "What's your diet type?",
        subtitle = "Choose your preferred dietary pattern"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                SelectableCard(
                    text = option,
                    isSelected = selected == option,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
fun FormStep2Allergies(options: List<String>, selected: Set<String>, onSelect: (Set<String>) -> Unit) {
    FormStepContainer(
        title = "Any allergies?",
        subtitle = "Select all that apply (or skip if none)"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                SelectableCheckbox(
                    text = option,
                    isSelected = option in selected,
                    onClick = {
                        val updated = selected.toMutableSet()
                        if (option in updated) updated.remove(option) else updated.add(option)
                        onSelect(updated)
                    }
                )
            }
        }
    }
}

@Composable
fun FormStep3CookingLevel(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    FormStepContainer(
        title = "What's your cooking skill?",
        subtitle = "This helps us suggest appropriate recipes"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                SelectableCard(
                    text = option,
                    isSelected = selected == option,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
fun FormStep4Budget(value: String, onValueChange: (String) -> Unit) {
    FormStepContainer(
        title = "Weekly Budget",
        subtitle = "In Tunisian Dinars (TND)"
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Budget (TND)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFF16A34A)
                )
            }
        )
    }
}

@Composable
fun FormStep5Meals(value: String, onValueChange: (String) -> Unit) {
    FormStepContainer(
        title = "Meals per day",
        subtitle = "How many meals do you typically eat?"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                SelectableCard(
                    text = i.toString(),
                    isSelected = value == i.toString(),
                    onClick = { onValueChange(i.toString()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FormStep6Preferences(options: List<String>, selected: Set<String>, onSelect: (Set<String>) -> Unit) {
    FormStepContainer(
        title = "Your preferences",
        subtitle = "Select what matters to you"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                SelectableCheckbox(
                    text = option,
                    isSelected = option in selected,
                    onClick = {
                        val updated = selected.toMutableSet()
                        if (option in updated) updated.remove(option) else updated.add(option)
                        onSelect(updated)
                    }
                )
            }
        }
    }
}

@Composable
fun FormStepContainer(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun SelectableCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF16A34A) else Color.White
        ),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF1A1C1E)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SelectableCheckbox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF16A34A))
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF1A1C1E)
        )
    }
}
