package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.presentation.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel,
    mealPlanId: String
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadShoppingList()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shopping List") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                if (uiState.items.isEmpty()) {
                    Text(
                        text = "Your shopping list is empty.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.items) { item ->
                            ShoppingItemRow(
                                item = item,
                                onCheckedChange = { checked ->
                                    viewModel.toggleItem(item.id, checked)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (item.checked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
