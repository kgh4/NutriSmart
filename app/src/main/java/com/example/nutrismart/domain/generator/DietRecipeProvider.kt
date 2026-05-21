package com.example.nutrismart.domain.generator

import com.example.nutrismart.domain.model.Recipe

class DietRecipeProvider {
    private val recipesByDiet = mapOf(
        "Vegan" to listOf(
            Recipe(
                id = "v1",
                title = "Lablabi (Tunisian Chickpea Soup)",
                description = "Traditional spicy and warm Tunisian street food.",
                ingredients = "Stale bread chunks\nCooked chickpeas\nGarlic\nCumin\nHarissa\nOlive oil\nLemon juice\nWater",
                steps = "Place bread in a bowl\nAdd chickpeas and hot water\nMix in garlic, cumin, harissa\nDrizzle olive oil and lemon juice",
                mealType = "Lunch",
                time = 15,
                calories = 450,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v2",
                title = "Slata Mechouia (Grilled Salad)",
                description = "Classic Tunisian grilled vegetable salad.",
                ingredients = "Green peppers\nTomatoes\nOnions\nGarlic\nOlive oil\nCaraway seeds (Tabel)",
                steps = "Grill vegetables until charred\nPeel and mash them\nSeason with caraway and olive oil\nServe cold",
                mealType = "Appetizer",
                time = 30,
                calories = 180,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v3",
                title = "Vegan Couscous with Vegetables",
                description = "Nutritious Tunisian couscous without meat.",
                ingredients = "Couscous\nCarrots\nZucchini\nPotatoes\nPumpkin\nChickpeas\nTomato paste\nHarissa",
                steps = "Steam couscous\nCook vegetables in a spicy tomato broth\nPour broth over couscous and garnish with veggies",
                mealType = "Lunch",
                time = 45,
                calories = 520,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v4",
                title = "Lentil Soup",
                description = "Hearty and nutritious international favorite.",
                ingredients = "Lentils\nCarrots\nOnion\nCelery\nVegetable broth\nSpices",
                steps = "Sauté veggies\nAdd lentils and broth\nSimmer until soft\nBlend partially",
                mealType = "Dinner",
                time = 40,
                calories = 320,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v5",
                title = "Quinoa and Cucumber Salad",
                description = "Fresh and light international lunch option.",
                ingredients = "Quinoa\nCucumber\nTomato\nParsley\nLemon juice\nOlive oil",
                steps = "Cook quinoa\nChop veggies\nMix all ingredients\nChill before serving",
                mealType = "Lunch",
                time = 20,
                calories = 300,
                budget = "Mid",
                dietCategory = "Vegan"
            )
        ),
        "Vegetarian" to listOf(
            Recipe(
                id = "vg1",
                title = "Shakshuka (Tunisian Style)",
                description = "Eggs poached in a spicy tomato and pepper sauce.",
                ingredients = "Eggs\nTomatoes\nGreen peppers\nOnion\nGarlic\nHarissa\nSpices",
                steps = "Sauté onion and peppers\nAdd tomatoes and harissa\nSimmer until thick\nCrack eggs on top and cook until set",
                mealType = "Breakfast",
                time = 20,
                calories = 350,
                budget = "Low",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg2",
                title = "Tunisian Tajine with Cheese",
                description = "A savory baked omelette-like dish.",
                ingredients = "Eggs\nPotatoes\nCheese\nParsley\nTurmeric\nBaking powder",
                steps = "Dice and fry potatoes\nMix eggs, cheese, parsley, and potatoes\nBake in the oven until firm",
                mealType = "Lunch",
                time = 40,
                calories = 480,
                budget = "Mid",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg3",
                title = "Vegetable Pasta (Makrouna)",
                description = "Spicy Tunisian pasta with seasonal veggies.",
                ingredients = "Pasta\nTomato paste\nHarissa\nPeas\nPotatoes\nOnion",
                steps = "Boil pasta\nPrepare a spicy red sauce with veggies\nMix pasta with sauce",
                mealType = "Dinner",
                time = 30,
                calories = 450,
                budget = "Low",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg4",
                title = "Cheese Omelette",
                description = "Classic quick international breakfast.",
                ingredients = "Eggs\nCheese\nButter\nSalt\nPepper",
                steps = "Beat eggs\nMelt butter in pan\nPour eggs and add cheese\nFold and serve",
                mealType = "Breakfast",
                time = 10,
                calories = 350,
                budget = "Low",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg5",
                title = "Mushroom Risotto",
                description = "Creamy and comforting international risotto.",
                ingredients = "Rice\nMushrooms\nVegetable broth\nOnion\nButter\nParmesan",
                steps = "Sauté onion and mushrooms\nAdd rice and broth slowly\nStir until creamy\nAdd butter and cheese",
                mealType = "Dinner",
                time = 45,
                calories = 500,
                budget = "Mid",
                dietCategory = "Vegetarian"
            )
        ),
        "Pescatarian" to listOf(
            Recipe(
                id = "p1",
                title = "Tunisian Couscous with Fish",
                description = "Famous coastal Tunisian specialty.",
                ingredients = "Couscous\nWhite fish (Dorade or Loup)\nPumpkin\nPotatoes\nQuince (Sfarjel)\nTomato sauce",
                steps = "Steam couscous\nCook fish and vegetables in spicy sauce\nCombine and serve",
                mealType = "Lunch",
                time = 60,
                calories = 580,
                budget = "High",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p2",
                title = "Ojja with Shrimp",
                description = "Spicy tomato sauce with eggs and shrimp.",
                ingredients = "Shrimp\nEggs\nTomatoes\nGarlic\nHarissa\nPeppers",
                steps = "Cook shrimp in spicy sauce\nCrack eggs over the top\nServe with bread",
                mealType = "Dinner",
                time = 25,
                calories = 420,
                budget = "High",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p3",
                title = "Tuna Brik",
                description = "Crispy pastry with tuna, egg, and parsley.",
                ingredients = "Malsouka (Pastry leaves)\nTuna\nEgg\nParsley\nCapers\nOnion",
                steps = "Fold filling into pastry leaf\nFry in hot oil until golden\nServe with lemon",
                mealType = "Appetizer",
                time = 20,
                calories = 320,
                budget = "Mid",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p4",
                title = "Grilled Salmon with Veggies",
                description = "Healthy omega-3 rich international dinner.",
                ingredients = "Salmon\nLemon\nAsparagus\nOlive oil\nHerbs",
                steps = "Season salmon\nGrill with asparagus\nDrizzle lemon juice\nServe",
                mealType = "Dinner",
                time = 25,
                calories = 400,
                budget = "High",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p5",
                title = "Tuna Salad (Slata Tounisia)",
                description = "Freshly chopped Tunisian salad with tuna.",
                ingredients = "Cucumber\nTomato\nOnion\nPeppers\nTuna\nOlive oil\nLemon",
                steps = "Finely chop all veggies\nAdd canned tuna\nSeason with olive oil and lemon",
                mealType = "Lunch",
                time = 15,
                calories = 280,
                budget = "Low",
                dietCategory = "Pescatarian"
            )
        ),
        "High-Protein" to listOf(
            Recipe(
                id = "hp1",
                title = "Lamb Couscous",
                description = "Classic festive Tunisian meal with high protein.",
                ingredients = "Lamb meat\nCouscous\nChickpeas\nOnion\nTomato paste\nAssorted veggies",
                steps = "Slow cook lamb in a rich broth\nSteam couscous\nAssemble together with veggies",
                mealType = "Lunch",
                time = 90,
                calories = 750,
                budget = "High",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp2",
                title = "Mosli Chicken (Roasted Chicken)",
                description = "Tunisian oven-roasted chicken with potatoes.",
                ingredients = "Chicken pieces\nPotatoes\nOnion\nTurmeric\nSaffron\nOlive oil",
                steps = "Marinate chicken and potatoes with turmeric\nRoast in the oven until crispy",
                mealType = "Dinner",
                time = 50,
                calories = 600,
                budget = "Mid",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp3",
                title = "Kamounia (Beef and Liver Stew)",
                description = "Rich Tunisian stew with a cumin base.",
                ingredients = "Beef chunks\nLiver\nTomato paste\nGarlic\nHarissa\nLots of Cumin",
                steps = "Sauté meat and liver\nAdd sauce ingredients\nSimmer until thickened\nAdd cumin at the very end",
                mealType = "Lunch",
                time = 45,
                calories = 550,
                budget = "Mid",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp4",
                title = "Grilled Chicken Breast with Quinoa",
                description = "Modern high-protein international bowl.",
                ingredients = "Chicken breast\nQuinoa\nBroccoli\nLemon dressing",
                steps = "Grill chicken breast\nCook quinoa and steam broccoli\nMix together with dressing",
                mealType = "Dinner",
                time = 30,
                calories = 520,
                budget = "Mid",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp5",
                title = "Steak and Eggs",
                description = "Power-packed international breakfast or dinner.",
                ingredients = "Steak\nEggs\nSpinach",
                steps = "Sear steak\nFry eggs in same pan\nServe with wilted spinach",
                mealType = "Lunch",
                time = 20,
                calories = 650,
                budget = "High",
                dietCategory = "High-Protein"
            )
        ),
        "Keto" to listOf(
            Recipe(
                id = "k1",
                title = "Ojja with Merguez (Keto Friendly)",
                description = "Spicy sausages and eggs in tomato sauce.",
                ingredients = "Merguez sausages\nEggs\nTomatoes\nGarlic\nHarissa\nOlive oil",
                steps = "Grill Merguez\nPrepare sauce in the same pan\nCrack eggs and cook until firm",
                mealType = "Dinner",
                time = 25,
                calories = 580,
                budget = "Mid",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k2",
                title = "Mosli Fish (Roasted Fish)",
                description = "Roasted fish with lemon and light spices.",
                ingredients = "Whole fish (Dorade)\nLemon slices\nOlive oil\nParsley\nGarlic",
                steps = "Stuff fish with garlic and lemon\nRoast with olive oil until cooked through",
                mealType = "Dinner",
                time = 35,
                calories = 450,
                budget = "High",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k3",
                title = "Tunisian Salad (No Chickpeas)",
                description = "Freshly chopped vegetables with olive oil.",
                ingredients = "Cucumber\nTomato\nPeppers\nOnion\nOlive oil\nDried mint",
                steps = "Chop veggies finely\nDress with plenty of olive oil and lemon",
                mealType = "Lunch",
                time = 15,
                calories = 220,
                budget = "Low",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k4",
                title = "Bacon and Avocado Bowl",
                description = "Classic high-fat international keto staple.",
                ingredients = "Bacon\nAvocado\nEggs",
                steps = "Fry bacon\nServe with sliced avocado and eggs",
                mealType = "Breakfast",
                time = 15,
                calories = 550,
                budget = "Mid",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k5",
                title = "Salmon with Asparagus",
                description = "Nutrient-dense international keto dinner.",
                ingredients = "Salmon\nAsparagus\nButter\nLemon",
                steps = "Roast salmon and asparagus with butter\nFinish with lemon",
                mealType = "Dinner",
                time = 25,
                calories = 480,
                budget = "High",
                dietCategory = "Keto"
            )
        ),
        "Weight Loss" to listOf(
            Recipe(
                id = "wl1",
                title = "Grilled Chicken Salad",
                description = "Lean protein with fresh Tunisian veggies.",
                ingredients = "Chicken breast\nCucumber\nTomato\nOnion\nVinegar\nLemon",
                steps = "Grill chicken\nChop salad veggies\nMix and serve",
                mealType = "Lunch",
                time = 25,
                calories = 320,
                budget = "Low",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl2",
                title = "Broad Bean Soup (Bissara)",
                description = "Traditional light and healthy soup.",
                ingredients = "Dry broad beans\nGarlic\nCumin\nOlive oil\nWater",
                steps = "Boil beans until soft\nPuree with garlic and cumin\nServe with a drop of olive oil",
                mealType = "Dinner",
                time = 40,
                calories = 280,
                budget = "Low",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl3",
                title = "Steamed Fish and Vegetables",
                description = "Lightest way to enjoy fresh Mediterranean fish.",
                ingredients = "White fish\nZucchini\nCarrots\nLemon\nHerbs",
                steps = "Steam fish and veggies together\nSeason with lemon and fresh herbs",
                mealType = "Dinner",
                time = 20,
                calories = 290,
                budget = "Mid",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl4",
                title = "Slata Mechouia (Grilled Salad)",
                description = "Low-calorie Tunisian grilled vegetable dish.",
                ingredients = "Green peppers\nTomatoes\nOnion\nGarlic\nLemon",
                steps = "Grill and mash vegetables\nServe without bread to reduce calories",
                mealType = "Appetizer",
                time = 30,
                calories = 150,
                budget = "Low",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl5",
                title = "Berry Smoothie",
                description = "Fast and low-calorie international snack.",
                ingredients = "Skim milk\nBerries\nChia seeds",
                steps = "Blend and drink",
                mealType = "Snack",
                time = 5,
                calories = 210,
                budget = "Low",
                dietCategory = "Weight Loss"
            )
        )
    )

    private val allRecipes = recipesByDiet.values.flatten()

    fun getRecipes(dietCategory: String): List<Recipe> {
        val filtered = recipesByDiet[dietCategory] ?: emptyList()
        return if (filtered.isNotEmpty()) filtered else allRecipes
    }

    fun getRecipeById(id: String): Recipe? {
        return allRecipes.find { it.id == id }
    }
}
