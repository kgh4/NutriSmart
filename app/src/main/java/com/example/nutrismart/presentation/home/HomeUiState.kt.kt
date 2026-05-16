package com.example.nutrismart.presentation.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val featuredRecipes: List<String> = emptyList(),
    val error: String? = null
)
