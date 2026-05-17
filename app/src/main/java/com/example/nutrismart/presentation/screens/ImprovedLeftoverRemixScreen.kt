package com.example.nutrismart.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LeftoverItem(
    val id: String,
    val name: String,
    val quantity: String,
    val expiryDays: Int
)

data class RemixRecipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val cookingTime: Int,
    val difficulty: String,
    val matchPercentage: Int
)

@Composable
fun ImprovedLeftoverRemixScreen() {
    var leftovers by remember { mutableStateOf(listOf<LeftoverItem>()) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }
    var selectedLeftover by remember { mutableStateOf<LeftoverItem?>(null) }
    var suggestedRecipes by remember { mutableStateOf(listOf<RemixRecipe>()) }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                LeftoverRemixHeader()
            }

            item {
                if (showAddForm) {
                    AddLeftoverForm(
                        name = newItemName,
                        onNameChange = { newItemName = it },
                        quantity = newItemQuantity,
                        onQuantityChange = { newItemQuantity = it },
                        onAdd = {
                            if (newItemName.isNotBlank() && newItemQuantity.isNotBlank()) {
                                leftovers = leftovers + LeftoverItem(
                                    id = System.currentTimeMillis().toString(),
                                    name = newItemName,
                                    quantity = newItemQuantity,
                                    expiryDays = 3
                                )
                                newItemName = ""
                                newItemQuantity = ""
                                showAddForm = false
                            }
                        },
                        onCancel = { showAddForm = false }
                    )
                } else {
                    AddLeftoverButton { showAddForm = true }
                }
            }

            if (leftovers.isNotEmpty()) {
                item {
                    Text(
                        text = "Your Leftovers (${leftovers.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(leftovers) { leftover ->
                    LeftoverCard(
                        item = leftover,
                        isSelected = selectedLeftover?.id == leftover.id,
                        onClick = {
                            selectedLeftover = if (selectedLeftover?.id == leftover.id) null else leftover
                            if (selectedLeftover != null) {
                                // Generate recipes based on selected leftover
                                suggestedRecipes = generateRecipeSuggestions(listOf(leftover))
                            }
                        },
                        onRemove = { leftovers = leftovers.filter { it.id != leftover.id } }
                    )
                }
            }

            if (suggestedRecipes.isNotEmpty()) {
                item {
                    Text(
                        text = "Suggested Recipes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(suggestedRecipes) { recipe ->
                    RecipeSuggestionCard(recipe)
                }
            }

            if (leftovers.isEmpty()) {
                item {
                    EmptyLeftoverState()
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LeftoverRemixHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDining,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "Leftover Remix",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Turn leftovers into new meals",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun AddLeftoverButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = "Add Leftover",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "Tell us what you have left",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun AddLeftoverForm(
    name: String,
    onNameChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Add New Leftover",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Item name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                placeholder = { Text("e.g., 2 cups, 500g") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onAdd,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun LeftoverCard(
    item: LeftoverItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0FDF4) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF16A34A)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = item.quantity,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = when {
                                    item.expiryDays <= 1 -> Color(0xFFFFEBEE)
                                    item.expiryDays <= 2 -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFF0FDF4)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.expiryDays}d",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                item.expiryDays <= 1 -> Color(0xFFEF4444)
                                item.expiryDays <= 2 -> Color(0xFFF59E0B)
                                else -> Color(0xFF16A34A)
                            }
                        )
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeSuggestionCard(recipe: RemixRecipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = recipe.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = recipe.description,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${recipe.matchPercentage}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RecipeInfoChip("${recipe.cookingTime} min", Icons.Default.Schedule)
                RecipeInfoChip(recipe.difficulty, Icons.Default.LocalDining)
            }

            Text(
                text = "Ingredients",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1C1E)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                recipe.ingredients.forEach { ingredient ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF16A34A), shape = RoundedCornerShape(3.dp))
                        )
                        Text(ingredient, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Recipe")
            }
        }
    }
}

@Composable
fun RecipeInfoChip(text: String, icon: androidx.compose.material.icons.materialIcon? = null) {
    Row(
        modifier = Modifier
            .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(14.dp)
            )
        }
        Text(text, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EmptyLeftoverState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiFoodBeverage,
                contentDescription = null,
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = "No leftovers yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        Text(
            text = "Add your leftovers to get smart recipe suggestions",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

fun generateRecipeSuggestions(leftovers: List<LeftoverItem>): List<RemixRecipe> {
    return listOf(
        RemixRecipe(
            id = "1",
            name = "Mixed Vegetable Stir Fry",
            description = "Quick and delicious",
            ingredients = listOf("Your leftovers", "Soy sauce", "Oil", "Garlic"),
            cookingTime = 15,
            difficulty = "Beginner",
            matchPercentage = 95
        ),
        RemixRecipe(
            id = "2",
            name = "Leftover Casserole",
            description = "Baked comfort food",
            ingredients = listOf("Your leftovers", "Cheese", "Eggs", "Cream"),
            cookingTime = 45,
            difficulty = "Intermediate",
            matchPercentage = 85
        )
    )
}
