package com.example.nutrismart.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToDailyIdeas: () -> Unit,
    onNavigateToLeftovers: () -> Unit,
    onNavigateToShoppingList: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("NutriSmart") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to NutriSmart!",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            HomeButton(text = "Weekly Meal Planner", onClick = onNavigateToPlanner)
            HomeButton(text = "Daily Meal Ideas", onClick = onNavigateToDailyIdeas)
            HomeButton(text = "Leftover Remix", onClick = onNavigateToLeftovers)
            HomeButton(text = "Shopping List", onClick = onNavigateToShoppingList)
            HomeButton(text = "My Profile", onClick = onNavigateToProfile)
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Text(text = text)
    }
}
