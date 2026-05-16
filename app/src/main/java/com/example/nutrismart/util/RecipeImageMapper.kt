package com.example.nutrismart.util

import com.example.nutrismart.R

object RecipeImageMapper {
    fun getRecipeImage(id: String): Int {
        return when (id) {
            "v1" -> R.drawable.vegan_chickpea_curry
            "v2" -> R.drawable.egg_and_avocado_toast
            "v3" -> R.drawable.vegetable_lentil_soup
            "v4" -> R.drawable.egg_and_spinach_bowl
            "v5" -> R.drawable.quinoa_salad
            "vg1" -> R.drawable.cheese_omelette
            "vg2" -> R.drawable.vegetable_pasta
            "vg3" -> R.drawable.rice_chicken_bowl
            "vg4" -> R.drawable.turkey_sandwich
            "vg5" -> R.drawable.spinach_lasagna
            "p1" -> R.drawable.tuna_bagel_sandwich
            "p2" -> R.drawable.garlic_butter_salmon
            "p3" -> R.drawable.fish_tacos
            "p4" -> R.drawable.coconut_curry_rice
            "p5" -> R.drawable.shrimp_pasta
            "hp1" -> R.drawable.chicken_rice_and_broccoli
            "hp2" -> R.drawable.steak_and_rice_bowl
            "hp3" -> R.drawable.protein_yogurt_bowl
            "hp4" -> R.drawable.turkey_sandwich
            "hp5" -> R.drawable.proteinpancakes
            "k1" -> R.drawable.bacon_egg_muffins
            "k2" -> R.drawable.protein_smoothie_bowl
            "k3" -> R.drawable.lettuce_burger_wraps
            "k4" -> R.drawable.bacon_egg_muffins
            "k5" -> R.drawable.garlic_butter_salmon
            "wl1" -> R.drawable.grilled_chicken_salad
            "wl2" -> R.drawable.zucchini_noodles_with_chicken
            "wl3" -> R.drawable.steamed_fish_and_rice
            "wl4" -> R.drawable.vegetable_stir_fry
            "wl5" -> R.drawable.protein_smoothie_bowl
            else -> R.drawable.vegetable_soup
        }
    }
}
