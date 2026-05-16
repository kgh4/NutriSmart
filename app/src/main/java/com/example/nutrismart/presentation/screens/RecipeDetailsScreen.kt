package com.example.nutrismart.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.RecipeDetailsViewModel
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.presentation.home.BottomNavigationBar
import com.example.nutrismart.util.RecipeImageMapper.getRecipeImage

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    appViewModel: AppViewModel,
    recipeId: String,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToShopping: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Scaffold(
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF16A34A))
            } else if (uiState.error != null) {
                Text(text = uiState.error ?: "Error", modifier = Modifier.align(Alignment.Center))
            } else {
                uiState.recipe?.let { recipe ->
                    RecipeDetailsContent(recipe, appViewModel, onBackClick)
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsContent(recipe: Recipe, appViewModel: AppViewModel, onBackClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Image(
                    painter = painterResource(id = getRecipeImage(recipe.id)),
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingButton(icon = Icons.Default.ArrowBack, onClick = onBackClick)
                    
                    val isFav = appViewModel.isFavorite(recipe.id)
                    FloatingButton(
                        icon = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        iconColor = if (isFav) Color.Red else Color(0xFF1A1C1E),
                        onClick = { appViewModel.toggleFavorite(recipe) }
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Schedule,
                        iconColor = Color(0xFF16A34A),
                        value = "${recipe.time}",
                        label = "minutes"
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AttachMoney,
                        iconColor = Color(0xFFF97316),
                        value = recipe.budget,
                        label = "budget"
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocalFireDepartment,
                        iconColor = Color(0xFFEF4444),
                        value = "${recipe.calories}",
                        label = "calories"
                    )
                }

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagChip(recipe.dietCategory)
                    TagChip("healthy")
                    TagChip("quick")
                }

                Spacer(Modifier.height(24.dp))

                NutritionCard(recipe)

                Spacer(Modifier.height(24.dp))

                SectionTitle("Ingredients")
                recipe.ingredients.split("\n", ",").filter { it.isNotBlank() }.forEach { ingredient ->
                    IngredientItem(ingredient.trim())
                }

                Spacer(Modifier.height(24.dp))

                SectionTitle("Steps")
                recipe.steps.split("\n").filter { it.isNotBlank() }.forEachIndexed { index, step ->
                    StepItem(index + 1, step.trim())
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun FloatingButton(icon: ImageVector, iconColor: Color = Color(0xFF1A1C1E), onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White,
        modifier = Modifier
            .size(44.dp)
            .shadow(4.dp, CircleShape)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier, icon: ImageVector, iconColor: Color, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun TagChip(tag: String) {
    Surface(
        color = Color(0xFFE6F7ED),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = tag.lowercase(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            fontSize = 14.sp,
            color = Color(0xFF16A34A),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NutritionCard(recipe: Recipe) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Nutrition Info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutritionItem("Protein", "${(recipe.calories * 0.05).toInt()}g")
                NutritionItem("Carbs", "${(recipe.calories * 0.1).toInt()}g")
                NutritionItem("Fat", "${(recipe.calories * 0.03).toInt()}g")
            }
        }
    }
}

@Composable
fun NutritionItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
    }
}

@Composable
fun IngredientItem(ingredient: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF16A34A))
        )
        Spacer(Modifier.width(12.dp))
        Text(text = ingredient, fontSize = 16.sp, color = Color(0xFF4B5563))
    }
}

@Composable
fun StepItem(number: Int, step: String) {
    Row(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = "$number.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF16A34A),
            modifier = Modifier.width(28.dp)
        )
        Text(
            text = step,
            fontSize = 16.sp,
            color = Color(0xFF4B5563),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1A1C1E),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}
