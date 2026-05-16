package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.presentation.viewmodel.WeeklyPlannerViewModel
import com.example.nutrismart.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyPlannerScreen(
    viewModel: WeeklyPlannerViewModel,
    profileId: String,
    onRecipeClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weekly Planner") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.generateWeeklyMealPlan(profileId) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isLoading) "Generating..." else "Generate New Weekly Plan")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                uiState.mealPlan?.let { plan ->
                    if (!uiState.isPlanActive) {
                        Button(
                            onClick = { viewModel.selectPlan(plan) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Text("Use This Plan")
                        }
                    } else {
                        Text(
                            "Plan Activated!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    MealPlanList(plan, onRecipeClick)
                } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No meal plan generated yet.")
                }
            }
        }
    }
}

@Composable
fun MealPlanList(plan: WeeklyMealPlan, onRecipeClick: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(plan.days) { day ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = day.dayOfWeek, style = MaterialTheme.typography.titleMedium)
                    MealRow("Breakfast", day.breakfast.recipe, onRecipeClick)
                    MealRow("Lunch", day.lunch.recipe, onRecipeClick)
                    MealRow("Dinner", day.dinner.recipe, onRecipeClick)
                    MealRow("Snack", day.snack.recipe, onRecipeClick)
                    Text(
                        text = "Estimated Daily Cost: ${CurrencyFormatter.formatTnd(day.dailyCost)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Total Weekly Cost: ${CurrencyFormatter.formatTnd(plan.totalCost)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun MealRow(label: String, recipe: com.example.nutrismart.domain.model.Recipe?, onRecipeClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: ${recipe?.title ?: "None"}", modifier = Modifier.weight(1f))
        if (recipe != null) {
            TextButton(onClick = { onRecipeClick(recipe.id) }) {
                Text("Details")
            }
        }
    }
}
