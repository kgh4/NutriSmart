package com.example.nutrismart.presentation.screens

import androidx.compose.animation.animateContentSize
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
import com.example.nutrismart.domain.model.FoodDatabase
import com.example.nutrismart.domain.model.FoodItem
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.presentation.viewmodel.ShoppingListViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel

data class CartItem(
    val foodItem: FoodItem,
    var quantity: Double = 1.0
) {
    val totalPrice: Double
        get() = foodItem.pricePerKg * quantity
}

@Composable
fun EnhancedShoppingListScreen(
    viewModel: ShoppingListViewModel,
    appViewModel: AppViewModel,
    mealPlanId: String
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var selectedQuantity by remember { mutableStateOf(1.0) }
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }

    val suggestions = if (searchQuery.isNotEmpty()) {
        FoodDatabase.searchFood(searchQuery)
    } else {
        emptyList()
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
                ShoppingListHeader(
                    totalItems = uiState.items.size,
                    checkedItems = uiState.items.count { it.checked },
                    totalPrice = calculateTotalPrice(cartItems)
                )
            }

            item {
                SearchAndAddSection(
                    searchQuery = searchQuery,
                    onSearchChange = {
                        searchQuery = it
                        showSuggestions = it.isNotEmpty()
                    },
                    suggestions = suggestions,
                    onSuggestionSelected = {
                        selectedFood = it
                        selectedQuantity = 1.0
                    }
                )
            }

            if (selectedFood != null) {
                item {
                    FoodDetailCard(
                        foodItem = selectedFood!!,
                        quantity = selectedQuantity,
                        onQuantityChange = { selectedQuantity = it },
                        onAddToCart = {
                            val cartItem = CartItem(selectedFood!!, selectedQuantity)
                            cartItems = cartItems + cartItem
                            searchQuery = ""
                            selectedFood = null
                            showSuggestions = false
                        },
                        onDismiss = { selectedFood = null }
                    )
                }
            }

            if (cartItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Cart (${cartItems.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(cartItems) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onQuantityChange = {
                            cartItems = cartItems.map {
                                if (it.foodItem.id == cartItem.foodItem.id) it.copy(quantity = it.quantity + 1)
                                else it
                            }
                        },
                        onRemove = {
                            cartItems = cartItems.filter { it.foodItem.id != cartItem.foodItem.id }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ShoppingListHeader(
    totalItems: Int,
    checkedItems: Int,
    totalPrice: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Shopping List",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("${checkedItems}/$totalItems", "Items")
                StatItem("%.2f TND".format(totalPrice), "Total")
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
    }
}

@Composable
fun SearchAndAddSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    suggestions: List<FoodItem>,
    onSuggestionSelected: (FoodItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
    ) {
        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            label = { Text("Search food items...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF16A34A)
                )
            }
        )

        // Suggestions
        if (suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Suggestions",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                suggestions.forEach { item ->
                    SuggestionChip(
                        foodItem = item,
                        onClick = { onSuggestionSelected(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(
    foodItem: FoodItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = foodItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "${foodItem.calories} cal | ${foodItem.pricePerKg} TND/kg",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun FoodDetailCard(
    foodItem: FoodItem,
    quantity: Double,
    onQuantityChange: (Double) -> Unit,
    onAddToCart: () -> Unit,
    onDismiss: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodItem.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = foodItem.category,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
                }
            }

            // Info
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoBadge("${foodItem.calories}\nCAL")
                InfoBadge("${foodItem.pricePerKg}\nTND/kg")
            }

            // Benefits
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Benefits",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                foodItem.benefits.forEach { benefit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(benefit, fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }

            Divider()

            // Quantity Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Quantity (kg)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1C1E)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (quantity > 0.5) onQuantityChange(quantity - 0.5) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, tint = Color(0xFF16A34A))
                    }
                    Text(
                        text = String.format("%.1f", quantity),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = Color(0xFF1A1C1E)
                    )
                    IconButton(
                        onClick = { onQuantityChange(quantity + 0.5) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF16A34A))
                    }
                }
                Text(
                    text = "Price: %.2f TND".format(foodItem.pricePerKg * quantity),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16A34A)
                )
            }

            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Cart")
            }
        }
    }
}

@Composable
fun InfoBadge(text: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF16A34A),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onQuantityChange: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.foodItem.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "%.1f kg × %.2f TND = %.2f TND".format(
                        cartItem.quantity,
                        cartItem.foodItem.pricePerKg,
                        cartItem.totalPrice
                    ),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "%.1f kg".format(cartItem.quantity),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF16A34A)
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                }
            }
        }
    }
}

fun calculateTotalPrice(cartItems: List<CartItem>): Double {
    return cartItems.sumOf { it.totalPrice }
}
