package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.DailyIdeasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyIdeasScreen(
    viewModel: DailyIdeasViewModel,
    profileId: String,
    onRecipeClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadIdeas(profileId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daily Meal Ideas") })
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
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }

                if (uiState.ideas.isEmpty() && uiState.error == null) {
                    Text(
                        text = "No ideas found. Try updating your profile.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.ideas) { recipe ->
                            RecipeIdeaCard(recipe, onRecipeClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeIdeaCard(recipe: Recipe, onRecipeClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onRecipeClick(recipe.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${recipe.time} mins", style = MaterialTheme.typography.bodySmall)
                Text(text = "Budget: ${recipe.budget}", style = MaterialTheme.typography.bodySmall)
                Text(text = "${recipe.calories} kcal", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
