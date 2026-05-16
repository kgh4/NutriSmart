package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.R
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.WeeklyPlannerViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.util.RecipeImageMapper.getRecipeImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyPlannerScreen(
    viewModel: WeeklyPlannerViewModel,
    appViewModel: AppViewModel,
    profileId: String,
    onRecipeClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    
    val days = listOf(
        DayInfo("Apr 14", "Mon", "Monday"),
        DayInfo("Apr 15", "Tue", "Tuesday"),
        DayInfo("Apr 16", "Wed", "Wednesday"),
        DayInfo("Apr 17", "Thu", "Thursday"),
        DayInfo("Apr 18", "Fri", "Friday"),
        DayInfo("Apr 19", "Sat", "Saturday"),
        DayInfo("Apr 20", "Sun", "Sunday")
    )

    LaunchedEffect(Unit) {
        if (uiState.mealPlan == null) {
            viewModel.generateWeeklyMealPlan(profileId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weekly Planner", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color(0xFFF9FAFB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                itemsIndexed(days) { index, dayInfo ->
                    DaySelectorItem(
                        dayInfo = dayInfo,
                        isSelected = selectedDayIndex == index,
                        onClick = { selectedDayIndex = index }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF16A34A))
                }
            } else {
                val currentDayPlan = uiState.mealPlan?.days?.getOrNull(selectedDayIndex)

                if (currentDayPlan != null) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = days[selectedDayIndex].fullDayName,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = days[selectedDayIndex].date,
                                    fontSize = 16.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                            Button(
                                onClick = { appViewModel.selectedDayPlan = currentDayPlan },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Use This Day Plan")
                            }
                        }
                    }

                    if (appViewModel.selectedDayPlan == currentDayPlan) {
                        Text(
                            text = "Day Plan Selected!",
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            currentDayPlan.breakfast.recipe?.let {
                                MealCard(
                                    type = "Breakfast",
                                    recipe = it,
                                    chipColor = Color(0xFFFBBF24),
                                    onClick = { onRecipeClick(it.id) }
                                )
                            }
                        }
                        item {
                            currentDayPlan.lunch.recipe?.let {
                                MealCard(
                                    type = "Lunch",
                                    recipe = it,
                                    chipColor = Color(0xFF16A34A),
                                    onClick = { onRecipeClick(it.id) }
                                )
                            }
                        }
                        item {
                            currentDayPlan.dinner.recipe?.let {
                                MealCard(
                                    type = "Dinner",
                                    recipe = it,
                                    chipColor = Color(0xFFF97316),
                                    onClick = { onRecipeClick(it.id) }
                                )
                            }
                        }
                        item {
                            currentDayPlan.snack.recipe?.let {
                                MealCard(
                                    type = "Snacks",
                                    recipe = it,
                                    chipColor = Color(0xFFA855F7),
                                    onClick = { onRecipeClick(it.id) }
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.error ?: "No meal plan found.")
                    }
                }
            }
        }
    }
}

@Composable
fun DaySelectorItem(
    dayInfo: DayInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF16A34A) else Color(0xFFF3F4F6)
        ),
        modifier = Modifier
            .width(70.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayInfo.date,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF6B7280)
            )
            Text(
                text = dayInfo.shortDayName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF1F2937)
            )
        }
    }
}

@Composable
fun MealCard(
    type: String,
    recipe: Recipe,
    chipColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = chipColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = type,
                    color = chipColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = getRecipeImage(recipe.id)),
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = recipe.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "${recipe.time} min • ${recipe.calories} cal • ${recipe.budget} Budget",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

data class DayInfo(val date: String, val shortDayName: String, val fullDayName: String)
