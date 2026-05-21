package com.example.nutrismart.presentation.screens

import androidx.compose.animation.*
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
import com.example.nutrismart.domain.model.Product
import com.example.nutrismart.presentation.viewmodel.ShoppingListViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel

@Composable
fun EnhancedShoppingListScreen(
    viewModel: ShoppingListViewModel,
    appViewModel: AppViewModel,
    mealPlanId: String,
    onNavigateToPlanner: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedWeeklyPlan = appViewModel.selectedWeeklyPlan

    LaunchedEffect(Unit) {
        snapshotFlow { appViewModel.selectedWeeklyPlan }
            .collect { plan ->
                if (plan != null) {
                    viewModel.loadShoppingList(plan.id)
                }
            }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (selectedWeeklyPlan == null) {
            NoPlanState(onNavigateToPlanner, padding)
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        HeaderSection(
                            checkedCount = uiState.items.count { it.checked },
                            totalCount = uiState.items.size
                        )
                    }

                    item {
                        SearchSection(
                            query = viewModel.searchQuery,
                            onQueryChange = { viewModel.onSearchQueryChanged(it) },
                            searchResults = uiState.searchResults,
                            onProductSelect = { viewModel.selectProduct(it) }
                        )
                    }

                    if (uiState.selectedProduct != null) {
                        item {
                            ProductAdjustmentSection(
                                product = uiState.selectedProduct!!,
                                weight = viewModel.currentWeight,
                                onWeightChange = { viewModel.updateWeight(it) },
                                onAddClick = { viewModel.addSelectedProduct() },
                                onCancel = { viewModel.selectProduct(uiState.selectedProduct!!) } 
                            )
                        }
                    }

                    if (uiState.error != null) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text(text = uiState.error ?: "", color = Color.Red)
                            }
                        }
                    } else {
                        val groupedItems = uiState.items.groupBy { it.category }
                        groupedItems.forEach { (category, items) ->
                            item {
                                Text(
                                    text = category,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(items) { item ->
                                ShoppingItemRow(
                                    item = item,
                                    onToggle = { 
                                        viewModel.toggleItem(item.id) { amount ->
                                            appViewModel.recordPurchase(amount)
                                        }
                                    },
                                    onDelete = { viewModel.removeItem(item.id) }
                                )
                            }
                        }
                    }
                    
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun NoPlanState(onNavigateToPlanner: () -> Unit, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFF16A34A).copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "No Weekly Plan Selected",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Please select a weekly plan in the planner to generate your shopping list.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNavigateToPlanner,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Weekly Planner", modifier = Modifier.padding(8.dp))
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
        Column(modifier = Modifier.padding(24.dp).padding(top = 16.dp)) {
            Text("Shopping List", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$checkedCount of $totalCount items checked", color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<Product>,
    onProductSelect: (Product) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search products (e.g. Tomato)...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF16A34A))
        )

        AnimatedVisibility(visible = searchResults.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    searchResults.forEach { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProductSelect(product) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(product.name, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.weight(1f))
                            Text("${product.pricePerUnit} TND/${product.unit}", color = Color.Gray, fontSize = 12.sp)
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductAdjustmentSection(
    product: Product,
    weight: Double,
    onWeightChange: (Double) -> Unit,
    onAddClick: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(product.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onCancel) { Icon(Icons.Default.Close, contentDescription = null) }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Weight/Quantity:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (weight > 0.1) onWeightChange(weight - 0.1) }) {
                        Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, tint = Color(0xFF16A34A))
                    }
                    Text("${String.format("%.1f", weight)} ${product.unit}", fontWeight = FontWeight.Bold)
                    IconButton(onClick = { onWeightChange(weight + 0.1) }) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Color(0xFF16A34A))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            
            val totalPrice = product.pricePerUnit * weight
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total Price:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Text("${String.format("%.2f", totalPrice)} TND", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))
            
            // Nutrition Info
            Text("Nutrition (per ${weight}${product.unit}):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShoppingNutritionItem("Cals", "${(product.calories * weight * 10).toInt()}")
                ShoppingNutritionItem("Prot", "${String.format("%.1f", product.proteins * weight * 10)}g")
                ShoppingNutritionItem("Carb", "${String.format("%.1f", product.carbs * weight * 10)}g")
                ShoppingNutritionItem("Fat", "${String.format("%.1f", product.fats * weight * 10)}g")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Text("Add to List")
            }
        }
    }
}

@Composable
fun ShoppingNutritionItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun ShoppingItemRow(item: ShoppingItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.checked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF16A34A))
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (item.checked) TextDecoration.LineThrough else null,
                    color = if (item.checked) Color.Gray else Color.Unspecified
                )
                if (item.quantity.isNotEmpty()) {
                    Text("${item.quantity} - ${String.format("%.2f", item.price)} TND", fontSize = 12.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444).copy(alpha = 0.7f))
            }
        }
    }
}
