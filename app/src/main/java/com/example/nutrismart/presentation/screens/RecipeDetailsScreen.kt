package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.RecipeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    recipeId: String,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "Error")
            }
        } else {
            uiState.recipe?.let { recipe ->
                RecipeDetailsContent(recipe, padding)
            }
        }
    }
}

@Composable
fun RecipeDetailsContent(recipe: Recipe, padding: PaddingValues) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // 1. TOP SECTION - Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("Recipe Image Placeholder", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 2. RECIPE TITLE
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(16.dp))

            // 3. INFO CARDS (ROW)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoCard(Modifier.weight(1f), "${recipe.time} mins", "Time")
                InfoCard(Modifier.weight(1f), recipe.budget, "Budget")
                InfoCard(Modifier.weight(1f), "${recipe.calories} kcal", "Calories")
            }

            Spacer(Modifier.height(16.dp))

            // 4. TAGS
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip(recipe.dietCategory)
            }

            Spacer(Modifier.height(24.dp))

            // 5. NUTRITION SECTION (Placeholder static as requested by previous design if needed, or based on calories)
            Text("Nutrition", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NutritionItem("Protein", "${(recipe.calories * 0.05).toInt()}g")
                    NutritionItem("Carbs", "${(recipe.calories * 0.1).toInt()}g")
                    NutritionItem("Fat", "${(recipe.calories * 0.03).toInt()}g")
                }
            }

            Spacer(Modifier.height(24.dp))

            // 6. INGREDIENTS SECTION
            SectionTitle("Ingredients")
            recipe.ingredients.split("\n").filter { it.isNotBlank() }.forEach { ingredient ->
                Text("• ${ingredient.trim()}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 4.dp))
            }

            Spacer(Modifier.height(24.dp))

            // 7. STEPS SECTION
            SectionTitle("Steps")
            recipe.steps.split("\n").filter { it.isNotBlank() }.forEachIndexed { index, step ->
                Row(Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "${index + 1}.",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(24.dp)
                    )
                    Text(text = step.trim(), style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun TagChip(tag: String) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = tag,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NutritionItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
