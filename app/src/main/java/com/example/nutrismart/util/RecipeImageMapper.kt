package com.example.nutrismart.util

import com.example.nutrismart.R

object RecipeImageMapper {
    fun getRecipeImage(id: String): Int {
        return when (id) {
            // Vegan
            "v1" -> R.drawable.vegan_chickpea_curry // Lablabi
            "v2" -> R.drawable.greek_salad // Slata Mechouia
            "v3" -> R.drawable.mediterranean_couscous_bowl // Vegan Couscous
            "v4" -> R.drawable.lentil_soup 
            "v5" -> R.drawable.quinoa_salad 

            // Vegetarian
            "vg1" -> R.drawable.egg_and_spinach_bowl // Shakshuka
            "vg2" -> R.drawable.spinach_lasagna // Tajine
            "vg3" -> R.drawable.vegetable_pasta // Makrouna
            "vg4" -> R.drawable.cheese_omelette 
            "vg5" -> R.drawable.homemade_veggie_soup // Risotto

            // Pescatarian
            "p1" -> R.drawable.steamed_fish_and_rice // Couscous Fish
            "p2" -> R.drawable.shrimp_pasta // Ojja Shrimp
            "p3" -> R.drawable.tuna_bagel_sandwich // Tuna Brik
            "p4" -> R.drawable.garlic_butter_salmon 
            "p5" -> R.drawable.fresh_tuna_salad 

            // High Protein
            "hp1" -> R.drawable.lamb_couscous 
            "hp2" -> R.drawable.rice_chicken_bowl // Mosli Chicken
            "hp3" -> R.drawable.beef_and_vegetable_bowl // Kamounia
            "hp4" -> R.drawable.chicken_rice_and_broccoli 
            "hp5" -> R.drawable.steak_and_rice_bowl // Steak & Eggs

            // Keto
            "k1" -> R.drawable.egg_and_beef_burrito // Ojja Merguez
            "k2" -> R.drawable.baked_fish_with_herbs // Mosli Fish
            "k3" -> R.drawable.greek_salad // Tunisian Salad
            "k4" -> R.drawable.bacon_egg_muffins 
            "k5" -> R.drawable.garlic_butter_salmon // Salmon Asparagus

            // Weight Loss
            "wl1" -> R.drawable.grilled_chicken_salad 
            "wl2" -> R.drawable.lentil_soup // Bissara
            "wl3" -> R.drawable.steamed_fish_and_rice 
            "wl4" -> R.drawable.greek_salad 
            "wl5" -> R.drawable.protein_smoothie_bowl

            else -> if (id.startsWith("ai_")) R.drawable.mixed_protein_bowl else R.drawable.vegetable_soup
        }
    }
}
