package com.example.nutrismart.presentation.home

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.util.RecipeImageMapper

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToDailyIdeas: () -> Unit,
    onNavigateToLeftovers: () -> Unit,
    onNavigateToShoppingList: () -> Unit,
    onRecipeClick: (String) -> Unit
) {
    val activePlan = appViewModel.selectedDayPlan

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = { },
                onPlannerClick = onNavigateToPlanner,
                onShoppingClick = onNavigateToShoppingList,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
        ) {
            item {
                HeaderSection(
                    userName = appViewModel.userName,
                    selectedMood = appViewModel.selectedMood,
                    onMoodSelect = { appViewModel.updateMood(it) }
                )
            }

            // NEW: Active Plan Section
            if (activePlan != null) {
                item {
                    ActivePlanSection(
                        dayPlan = activePlan,
                        onRecipeClick = onRecipeClick
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = if (activePlan != null) 24.dp else 0.dp)
                        .offset(y = if (activePlan != null) 0.dp else (-40).dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        title = "Weekly Plan",
                        subtitle = "Plan your meals for the week",
                        icon = Icons.Default.CalendarMonth,
                        iconColor = Color(0xFF12B347),
                        iconBgColor = Color(0xFFE6F7ED),
                        onClick = onNavigateToPlanner
                    )
                    ActionCard(
                        title = "Daily Ideas",
                        subtitle = "Get fresh meal suggestions",
                        icon = Icons.Default.Lightbulb,
                        iconColor = Color(0xFFF39C12),
                        iconBgColor = Color(0xFFFFF3E6),
                        onClick = onNavigateToDailyIdeas
                    )
                    ActionCard(
                        title = "Leftover Remix",
                        subtitle = "Turn leftovers into meals",
                        icon = Icons.Default.Sync,
                        iconColor = Color(0xFF9B59B6),
                        iconBgColor = Color(0xFFF3E6FF),
                        onClick = onNavigateToLeftovers
                    )
                }
            }

            item {
                Text(
                    text = "Previously Liked Recipes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp)
                )
            }

            if (appViewModel.savedRecipes.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No saved recipes yet", color = Color.Gray)
                    }
                }
            } else {
                items(appViewModel.savedRecipes) { recipe ->
                    RecipeCard(recipe, onRecipeClick)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ActivePlanSection(
    dayPlan: com.example.nutrismart.domain.model.DayMealPlan,
    onRecipeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Active Plan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Surface(
                color = Color(0xFF12B347).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = dayPlan.dayOfWeek,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color(0xFF12B347),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Show Breakfast, Lunch, Dinner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dayPlan.breakfast.recipe?.let { recipe ->
                ActiveMealCard(
                    modifier = Modifier.weight(1f),
                    label = "Breakfast",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
            dayPlan.lunch.recipe?.let { recipe ->
                ActiveMealCard(
                    modifier = Modifier.weight(1f),
                    label = "Lunch",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
            dayPlan.dinner.recipe?.let { recipe ->
                ActiveMealCard(
                    modifier = Modifier.weight(1f),
                    label = "Dinner",
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }
    }
}

@Composable
fun ActiveMealCard(
    modifier: Modifier = Modifier,
    label: String,
    recipe: com.example.nutrismart.domain.model.Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = RecipeImageMapper.getRecipeImage(recipe.id)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = recipe.title,
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = Color(0xFF1A1C1E)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(
    userName: String,
    selectedMood: MoodType,
    onMoodSelect: (MoodType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color(0xFF12B347))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hi $userName!",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "What's cooking today?",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = selectedMood,
                transitionSpec = {
                    fadeIn() + slideInVertically() togetherWith fadeOut() + slideOutVertically()
                },
                label = "MoodTitle"
            ) { mood ->
                Text(
                    text = "${mood.emoji} Feeling ${mood.displayName}?",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            // Mood Selector
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                MoodType.values().take(3).forEachIndexed { index, mood ->
                    SegmentedButton(
                        selected = selectedMood == mood,
                        onClick = { onMoodSelect(mood) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color.White.copy(alpha = 0.3f),
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.Transparent,
                            inactiveContentColor = Color.White.copy(alpha = 0.7f),
                            activeBorderColor = Color.White,
                            inactiveBorderColor = Color.White.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("${mood.emoji} ${mood.displayName}", fontSize = 10.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                MoodType.values().takeLast(3).forEachIndexed { index, mood ->
                    SegmentedButton(
                        selected = selectedMood == mood,
                        onClick = { onMoodSelect(mood) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color.White.copy(alpha = 0.3f),
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.Transparent,
                            inactiveContentColor = Color.White.copy(alpha = 0.7f),
                            activeBorderColor = Color.White,
                            inactiveBorderColor = Color.White.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("${mood.emoji} ${mood.displayName}", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick(recipe.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = RecipeImageMapper.getRecipeImage(recipe.id)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1C1E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${recipe.time} min", fontSize = 12.sp, color = Color.Gray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = recipe.budget, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onPlannerClick: () -> Unit,
    onShoppingClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF12B347),
                selectedTextColor = Color(0xFF12B347),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onPlannerClick,
            icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Planner") },
            label = { Text("Planner", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onShoppingClick,
            icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = "Shopping") },
            label = { Text("Shopping", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
