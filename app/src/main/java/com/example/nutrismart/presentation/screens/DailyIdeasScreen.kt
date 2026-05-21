package com.example.nutrismart.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.presentation.home.BottomNavigationBar
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.presentation.viewmodel.DailyIdeasViewModel
import com.example.nutrismart.util.RecipeImageMapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyIdeasScreen(
    viewModel: DailyIdeasViewModel,
    appViewModel: AppViewModel,
    onRecipeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToShopping: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedMood = appViewModel.selectedMood
    val currentUser by appViewModel.currentUser.collectAsState()

    LaunchedEffect(selectedMood, currentUser) {
        viewModel.generateDailyIdeas(selectedMood, currentUser)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${selectedMood.emoji} ", fontSize = 24.sp)
                        Text("${selectedMood.displayName} Ideas", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onNavigateToHome,
                onPlannerClick = onNavigateToPlanner,
                onShoppingClick = onNavigateToShopping,
                onProfileClick = onNavigateToProfile
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. ORANGE HEADER CARD
            item {
                HeaderCard(selectedMood.emoji, selectedMood.displayName)
            }

            // 2. GET NEW IDEAS BUTTON
            item {
                OutlinedButton(
                    onClick = { viewModel.generateDailyIdeas(selectedMood, currentUser) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF16A34A)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF16A34A))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Refresh ${selectedMood.emoji} Ideas", fontWeight = FontWeight.Bold)
                }
            }

            // 3. RECIPE CARDS
            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color(0xFF16A34A))
                            Spacer(Modifier.height(8.dp))
                            Text("Gemini is cooking up ideas...", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            } else if (uiState.error != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFB91C1C)
                        )
                    }
                }
            } else {
                items(uiState.ideas) { dailyIdea ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        RecipeCard(dailyIdea = dailyIdea, onClick = { onRecipeClick(dailyIdea.recipe.id) })
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HeaderCard(emoji: String, moodName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6F00))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "$emoji $moodName Mode",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Perfect meals for you!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "AI-curated based on how you feel.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RecipeCard(dailyIdea: DailyIdea, onClick: () -> Unit) {
    val recipe = dailyIdea.recipe
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image at TOP
            Image(
                painter = painterResource(id = RecipeImageMapper.getRecipeImage(recipe.id)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dailyIdea.moodTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    
                    // Small green circle badge
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE6F7ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$", color = Color(0xFF16A34A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${recipe.time} min", fontSize = 13.sp, color = Color.Gray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${recipe.budget} Budget", fontSize = 13.sp, color = Color.Gray)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Tags Section
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DailyIdeaTagChip(recipe.dietCategory)
                    DailyIdeaTagChip("healthy")
                    DailyIdeaTagChip("quick")
                }
            }
        }
    }
}

@Composable
fun DailyIdeaTagChip(text: String) {
    Surface(
        color = Color(0xFFF3F4F6),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text.lowercase(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}
