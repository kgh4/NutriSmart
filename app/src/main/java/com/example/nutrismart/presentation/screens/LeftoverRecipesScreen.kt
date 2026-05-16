package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.presentation.viewmodel.LeftoverRecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftoverRecipesScreen(
    viewModel: LeftoverRecipesViewModel,
    onRecipeClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Leftover Remix") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onQueryChanged(it) },
                label = { Text("What's in your fridge? (e.g. eggs, rice)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.searchRecipes() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.searchQuery.isNotBlank() && !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Searching..." else "Find Recipes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    uiState.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }

                    if (uiState.recipes.isEmpty() && uiState.error == null) {
                        Text(
                            text = "Enter ingredients to see magic happen!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.recipes) { recipe ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onRecipeClick(recipe.id) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                                        Text(text = recipe.description, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
