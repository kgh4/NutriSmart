package com.example.nutrismart.presentation.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.presentation.viewmodel.OnboardingViewModel
import com.example.nutrismart.presentation.viewmodel.ViewModelFactory

@Composable
fun UserHabitsFormScreen(
    appViewModel: AppViewModel,
    onComplete: (Map<String, String>) -> Unit
) {
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = ViewModelFactory)
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 4

    // Form State
    var dietCategory by remember { mutableStateOf("Balanced") }
    var cookingSkill by remember { mutableStateOf("Beginner") }
    var budget by remember { mutableStateOf("50") }
    var maxTime by remember { mutableStateOf("30") }

    if (onboardingViewModel.isComplete) {
        LaunchedEffect(Unit) {
            onComplete(mapOf("diet" to dietCategory))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header with Progress
            OnboardingHeader(currentStep, totalSteps)

            Spacer(modifier = Modifier.height(32.dp))

            // Step Content
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    1 -> DietSelectionStep(dietCategory) { dietCategory = it }
                    2 -> CookingSkillStep(cookingSkill) { cookingSkill = it }
                    3 -> BudgetStep(budget) { budget = it }
                    4 -> TimeStep(maxTime) { maxTime = it }
                }
            }

            // Navigation Buttons
            OnboardingNavigation(
                currentStep = currentStep,
                totalSteps = totalSteps,
                isLoading = onboardingViewModel.isLoading,
                onBack = { if (currentStep > 1) currentStep-- },
                onNext = {
                    if (currentStep < totalSteps) {
                        currentStep++
                    } else {
                        onboardingViewModel.completeOnboarding(
                            name = appViewModel.userName,
                            email = appViewModel.userEmail,
                            dietCategory = dietCategory,
                            cookingSkill = cookingSkill,
                            budget = budget.toDoubleOrNull() ?: 50.0,
                            maxTime = maxTime.toIntOrNull() ?: 30
                        )
                    }
                }
            )
        }

        if (onboardingViewModel.error != null) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { /* Clear error if needed */ }) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            ) {
                Text(onboardingViewModel.error ?: "An error occurred")
            }
        }
    }
}

@Composable
fun OnboardingHeader(currentStep: Int, totalSteps: Int) {
    Column {
        Text(
            text = "Step $currentStep of $totalSteps",
            color = Color(0xFF16A34A),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { currentStep.toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF16A34A),
            trackColor = Color(0xFFE5E7EB)
        )
    }
}

@Composable
fun DietSelectionStep(selected: String, onSelect: (String) -> Unit) {
    Column {
        Text("What's your diet preference?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("We'll tailor your recipes based on this.", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        val options = listOf(
            OptionItem("Balanced", "Everything in moderation", Icons.Default.Restaurant),
            OptionItem("Vegetarian", "No meat, but eggs & dairy okay", Icons.Default.Eco),
            OptionItem("Vegan", "Plant-based only", Icons.Default.Park),
            OptionItem("Keto", "High fat, low carb", Icons.Default.Fireplace)
        )

        options.forEach { option ->
            SelectionCard(
                title = option.title,
                subtitle = option.subtitle,
                icon = option.icon,
                isSelected = selected == option.title,
                onClick = { onSelect(option.title) }
            )
        }
    }
}

@Composable
fun CookingSkillStep(selected: String, onSelect: (String) -> Unit) {
    Column {
        Text("How's your cooking?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Be honest! We have recipes for everyone.", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        val options = listOf(
            OptionItem("Beginner", "I'm just starting out", Icons.Default.Kitchen),
            OptionItem("Intermediate", "I know my way around", Icons.Default.OutdoorGrill),
            OptionItem("Expert", "I'm basically a chef", Icons.Default.Star)
        )

        options.forEach { option ->
            SelectionCard(
                title = option.title,
                subtitle = option.subtitle,
                icon = option.icon,
                isSelected = selected == option.title,
                onClick = { onSelect(option.title) }
            )
        }
    }
}

@Composable
fun BudgetStep(value: String, onValueChange: (String) -> Unit) {
    Column {
        Text("Weekly Budget (TND)", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Helps us find cost-effective meals.", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Amount in TND") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF16A34A),
                focusedLabelColor = Color(0xFF16A34A)
            )
        )
    }
}

@Composable
fun TimeStep(value: String, onValueChange: (String) -> Unit) {
    Column {
        Text("Max Cooking Time", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("How long do you usually have for a meal?", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Minutes") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF16A34A),
                focusedLabelColor = Color(0xFF16A34A)
            )
        )
    }
}

@Composable
fun SelectionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF16A34A).copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF16A34A)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF16A34A) else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isSelected) Color(0xFF16A34A) else Color.Black
                )
                Text(text = subtitle, color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
            }
        }
    }
}

@Composable
fun OnboardingNavigation(
    currentStep: Int,
    totalSteps: Int,
    isLoading: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep > 1) {
            TextButton(onClick = onBack) {
                Text("Back", color = Color.Gray)
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .height(56.dp)
                .width(160.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(if (currentStep == totalSteps) "Finish" else "Next")
            }
        }
    }
}

data class OptionItem(val title: String, val subtitle: String, val icon: ImageVector)
