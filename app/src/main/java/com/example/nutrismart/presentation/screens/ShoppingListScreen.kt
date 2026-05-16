package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.presentation.viewmodel.ShoppingListViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import java.util.UUID

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel,
    appViewModel: AppViewModel,
    mealPlanId: String
) {
    val plan = appViewModel.selectedDayPlan
    val uiState by viewModel.uiState.collectAsState()
    var newItemName by remember { mutableStateOf("") }

    if (plan != null) {
        LaunchedEffect(plan) {
            val ingredients = listOfNotNull(
                plan.breakfast.recipe,
                plan.lunch.recipe,
                plan.dinner.recipe,
                plan.snack.recipe
            ).flatMap { it.ingredients.split("\n") }
                .filter { it.isNotBlank() }
                .groupingBy { it.trim() }
                .eachCount()

            val shoppingItems = ingredients.map { (name, count) ->
                ShoppingItem(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    quantity = if (count > 1) "$count" else "",
                    checked = false,
                    category = "Selected Day Plan"
                )
            }
            viewModel.updateItems(shoppingItems)
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                HeaderSection(
                    checkedCount = uiState.items.count { it.checked },
                    totalCount = uiState.items.size
                )
            }

            if (plan == null) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Please select a day plan in Weekly Planner first", color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            } else {
                item {
                    AddItemRow(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        onAddClick = {
                            if (newItemName.isNotBlank()) {
                                viewModel.addItem(newItemName)
                                newItemName = ""
                            }
                        }
                    )
                }

                val groupedItems = uiState.items.groupBy { it.category }
                groupedItems.forEach { (category, items) ->
                    item {
                        Text(
                            text = category,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E),
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column {
                                items.forEachIndexed { index, item ->
                                    ShoppingItemRow(
                                        item = item,
                                        onToggle = { viewModel.toggleItem(item.id) },
                                        onDelete = { viewModel.removeItem(item.id) }
                                    )
                                    if (index < items.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(checkedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) checkedCount.toFloat() / totalCount else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A))
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 40.dp)
        ) {
            Text(
                text = "Shopping List",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$checkedCount of $totalCount items checked",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun AddItemRow(value: String, onValueChange: (String) -> Unit, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Add new item...", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16A34A))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    }
}

@Composable
fun ShoppingItemRow(item: ShoppingItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF16A34A))
        )
        
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(
                text = if (item.quantity.isNotEmpty()) "${item.name} (${item.quantity})" else item.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (item.checked) Color.Gray else Color(0xFF1A1C1E),
                    textDecoration = if (item.checked) TextDecoration.LineThrough else null
                )
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFEF4444).copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
