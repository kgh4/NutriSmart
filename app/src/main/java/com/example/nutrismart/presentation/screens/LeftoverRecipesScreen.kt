package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.presentation.viewmodel.LeftoverRecipesViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LeftoverRecipesScreen(
    viewModel: LeftoverRecipesViewModel,
    onRecipeClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedIngredients = viewModel.selectedIngredients

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Leftover Remix", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. GRADIENT HEADER
            item {
                GradientHeader()
            }

            // 2. INPUT SECTION
            item {
                Column {
                    Text(
                        text = "What ingredients do you have?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.currentInput,
                            onValueChange = { viewModel.onInputChanged(it) },
                            placeholder = { Text("e.g., chicken, pasta, tomatoes...", color = Color.LightGray) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        IconButton(
                            onClick = { viewModel.addIngredient(uiState.currentInput) },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF12B347))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        }
                    }
                }
            }

            // 3. SELECTED INGREDIENTS SECTION
            item {
                Column {
                    Text(
                        text = "Your Ingredients (${selectedIngredients.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedIngredients.forEach { ingredient ->
                                IngredientChip(
                                    name = ingredient,
                                    onRemove = { viewModel.removeIngredient(ingredient) }
                                )
                            }
                        }
                    }
                }
            }

            // 4. QUICK ADD SECTION
            item {
                Column {
                    Text(
                        text = "Quick Add",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val quickAdd = listOf("Eggs", "Cheese", "Onions", "Garlic", "Bread")
                        quickAdd.forEach { item ->
                            QuickAddChip(
                                name = item,
                                onClick = { viewModel.addIngredient(item) }
                            )
                        }
                    }
                }
            }

            // 5. GENERATE BUTTON
            item {
                Button(
                    onClick = { viewModel.generateRecipeIdeas() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF8E44AD), Color(0xFFE91E63))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Generate Recipe Ideas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            // 6. RESULTS SECTION
            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF8E44AD))
                    }
                }
            } else if (uiState.error != null) {
                item {
                    Text(text = uiState.error!!, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                }
            } else {
                items(uiState.recipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRecipeClick(recipe.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = recipe.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = recipe.description, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun GradientHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF8E44AD), Color(0xFF9B59B6))
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(text = "Got Leftovers?", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Turn them into something amazing!", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun IngredientChip(name: String, onRemove: () -> Unit) {
    Surface(
        color = Color(0xFFF3E6FF),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, color = Color(0xFF8E44AD), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color(0xFF8E44AD),
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun QuickAddChip(name: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Text(
            text = "+ $name",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}
