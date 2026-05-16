package com.example.nutrismart.domain.generator

import com.example.nutrismart.domain.model.Recipe

class DietRecipeProvider {
    private val recipesByDiet = mapOf(
        "Vegan" to listOf(
            Recipe(
                id = "v1",
                title = "Vegan Chickpea Curry",
                description = "Healthy and filling chickpea curry",
                ingredients = "Chickpeas\nRice\nOnion\nTomato\nGarlic\nOlive oil\nSpices",
                steps = "Cook onion and garlic\nAdd tomato and chickpeas\nAdd spices\nSimmer\nServe with rice",
                time = 25,
                calories = 450,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v2",
                title = "Avocado Toast",
                description = "Quick and easy breakfast",
                ingredients = "Bread\nAvocado\nLemon\nSalt\nPepper",
                steps = "Toast bread\nMash avocado with lemon\nSpread on toast\nSeason",
                time = 10,
                calories = 250,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v3",
                title = "Lentil Soup",
                description = "Hearty and nutritious soup",
                ingredients = "Lentils\nCarrots\nOnion\nCelery\nVegetable broth\nSpices",
                steps = "Sauté veggies\nAdd lentils and broth\nSimmer until soft\nBlend partially",
                time = 40,
                calories = 320,
                budget = "Low",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v4",
                title = "Tofu Scramble",
                description = "A great vegan alternative to eggs",
                ingredients = "Tofu\nSpinach\nTurmeric\nOnion\nBell Pepper",
                steps = "Crumble tofu\nSauté with veggies\nAdd turmeric and salt\nCook until warm",
                time = 15,
                calories = 280,
                budget = "Mid",
                dietCategory = "Vegan"
            ),
            Recipe(
                id = "v5",
                title = "Quinoa Salad",
                description = "Fresh and light lunch option",
                ingredients = "Quinoa\nCucumber\nTomato\nParsley\nLemon juice\nOlive oil",
                steps = "Cook quinoa\nChop veggies\nMix all ingredients\nChill before serving",
                time = 20,
                calories = 300,
                budget = "Mid",
                dietCategory = "Vegan"
            )
        ),
        "Vegetarian" to listOf(
            Recipe(
                id = "vg1",
                title = "Cheese Omelette",
                description = "Classic cheesy breakfast",
                ingredients = "Eggs\nCheese\nButter\nSalt\nPepper",
                steps = "Beat eggs\nMelt butter in pan\nPour eggs and add cheese\nFold and serve",
                time = 10,
                calories = 350,
                budget = "Low",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg2",
                title = "Vegetable Pasta",
                description = "Simple pasta with fresh veggies",
                ingredients = "Pasta\nTomato sauce\nZucchini\nBell Pepper\nParmesan",
                steps = "Boil pasta\nSauté veggies\nMix with sauce\nTop with cheese",
                time = 20,
                calories = 420,
                budget = "Low",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg3",
                title = "Mushroom Risotto",
                description = "Creamy and comforting risotto",
                ingredients = "Rice\nMushrooms\nVegetable broth\nOnion\nButter\nParmesan",
                steps = "Sauté onion and mushrooms\nAdd rice and broth slowly\nStir until creamy\nAdd butter and cheese",
                time = 45,
                calories = 500,
                budget = "Mid",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg4",
                title = "Caprese Sandwich",
                description = "Fresh Italian-style sandwich",
                ingredients = "Bread\nMozzarella\nTomato\nBasil\nPesto",
                steps = "Slice tomato and cheese\nSpread pesto on bread\nAssemble sandwich\nToast if desired",
                time = 10,
                calories = 380,
                budget = "Mid",
                dietCategory = "Vegetarian"
            ),
            Recipe(
                id = "vg5",
                title = "Spinach Quiche",
                description = "Savory spinach and egg pie",
                ingredients = "Eggs\nSpinach\nMilk\nCheese\nPie crust",
                steps = "Whisk eggs and milk\nPlace spinach in crust\nPour egg mixture\nBake at 180C for 35 mins",
                time = 50,
                calories = 450,
                budget = "High",
                dietCategory = "Vegetarian"
            )
        ),
        "Pescatarian" to listOf(
            Recipe(
                id = "p1",
                title = "Tuna Sandwich",
                description = "Quick and satisfying lunch",
                ingredients = "Bread\nTuna\nMayo\nOnion\nLettuce",
                steps = "Mix tuna with mayo and onion\nSpread on bread\nAdd lettuce\nServe",
                time = 10,
                calories = 340,
                budget = "Low",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p2",
                title = "Grilled Salmon",
                description = "Healthy omega-3 rich dinner",
                ingredients = "Salmon\nLemon\nAsparagus\nOlive oil\nHerbs",
                steps = "Season salmon\nGrill with asparagus\nDrizzle lemon juice\nServe",
                time = 25,
                calories = 400,
                budget = "High",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p3",
                title = "Shrimp Tacos",
                description = "Flavourful seafood tacos",
                ingredients = "Shrimp\nTortillas\nCabbage\nLime\nCrema",
                steps = "Sauté shrimp with spices\nWarm tortillas\nAssemble with cabbage\nAdd lime and crema",
                time = 20,
                calories = 380,
                budget = "Mid",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p4",
                title = "Fish Curry",
                description = "Spicy white fish curry",
                ingredients = "White fish\nCoconut milk\nCurry paste\nGinger\nRice",
                steps = "Sauté curry paste and ginger\nAdd coconut milk\nAdd fish chunks\nSimmer and serve with rice",
                time = 30,
                calories = 450,
                budget = "Mid",
                dietCategory = "Pescatarian"
            ),
            Recipe(
                id = "p5",
                title = "Sardine Pasta",
                description = "Easy pantry-based meal",
                ingredients = "Pasta\nSardines\nGarlic\nChili flakes\nParsley",
                steps = "Boil pasta\nSauté garlic and chili\nAdd sardines and pasta\nToss with parsley",
                time = 15,
                calories = 480,
                budget = "Low",
                dietCategory = "Pescatarian"
            )
        ),
        "High-Protein" to listOf(
            Recipe(
                id = "hp1",
                title = "Chicken Protein Bowl",
                description = "Perfect for post-workout",
                ingredients = "Chicken breast\nQuinoa\nBroccoli\nGreek yogurt sauce",
                steps = "Grill chicken breast\nCook quinoa and broccoli\nCombine in bowl\nTop with sauce",
                time = 30,
                calories = 520,
                budget = "Mid",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp2",
                title = "Steak and Eggs",
                description = "Protein-heavy breakfast or dinner",
                ingredients = "Steak\nEggs\nPotatoes\nButter",
                steps = "Sear steak to desired doneness\nFry eggs in same pan\nServe with roasted potatoes",
                time = 25,
                calories = 650,
                budget = "High",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp3",
                title = "Greek Yogurt Parfait",
                description = "Fast and high in protein",
                ingredients = "Greek yogurt\nWhey protein\nBerries\nAlmonds",
                steps = "Mix yogurt with protein\nLayer with berries\nTop with almonds\nServe chilled",
                time = 5,
                calories = 350,
                budget = "Mid",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp4",
                title = "Turkey Wrap",
                description = "Lean protein lunch",
                ingredients = "Turkey breast\nWhole wheat tortilla\nHummus\nSpinach",
                steps = "Spread hummus on tortilla\nAdd turkey and spinach\nRoll tightly\nServe",
                time = 10,
                calories = 380,
                budget = "Low",
                dietCategory = "High-Protein"
            ),
            Recipe(
                id = "hp5",
                title = "Cottage Cheese Pancakes",
                description = "Delicious protein pancakes",
                ingredients = "Cottage cheese\nEggs\nOats\nVanilla",
                steps = "Blend all ingredients\nCook on griddle\nServe with fruit\nEnjoy",
                time = 15,
                calories = 420,
                budget = "Low",
                dietCategory = "High-Protein"
            )
        ),
        "Keto" to listOf(
            Recipe(
                id = "k1",
                title = "Bacon and Avocado",
                description = "High-fat keto staple",
                ingredients = "Bacon\nAvocado\nEggs",
                steps = "Fry bacon until crispy\nSlice avocado\nServe together with fried eggs",
                time = 15,
                calories = 550,
                budget = "Mid",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k2",
                title = "Bulletproof Coffee",
                description = "Classic keto fat bomb drink",
                ingredients = "Coffee\nButter\nMCT Oil",
                steps = "Brew coffee\nBlend with butter and oil\nDrink warm",
                time = 5,
                calories = 250,
                budget = "Mid",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k3",
                title = "Cheeseburger (No Bun)",
                description = "Simple keto dinner",
                ingredients = "Beef patty\nCheese\nLettuce wraps\nPickles",
                steps = "Grill beef patty\nTop with cheese\nWrap in lettuce\nAdd pickles",
                time = 20,
                calories = 500,
                budget = "Low",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k4",
                title = "Egg Muffins",
                description = "Great for keto meal prep",
                ingredients = "Eggs\nBacon\nSpinach\nCheese",
                steps = "Whisk eggs with fillings\nPour into muffin tin\nBake at 180C for 20 mins",
                time = 30,
                calories = 320,
                budget = "Low",
                dietCategory = "Keto"
            ),
            Recipe(
                id = "k5",
                title = "Salmon with Asparagus",
                description = "Elegant keto dinner",
                ingredients = "Salmon\nAsparagus\nButter\nLemon",
                steps = "Roast salmon and asparagus\nTop with lots of butter\nAdd squeeze of lemon",
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
                description = "Low-calorie filling salad",
                ingredients = "Chicken breast\nMixed greens\nCucumber\nVinegar",
                steps = "Grill chicken\nToss greens and cucumber\nDress with vinegar\nServe",
                time = 20,
                calories = 300,
                budget = "Low",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl2",
                title = "Zucchini Noodles",
                description = "Pasta alternative for weight loss",
                ingredients = "Zucchini\nTomato sauce\nTurkey meatballs",
                steps = "Spiralize zucchini\nCook meatballs in sauce\nCombine and serve",
                time = 25,
                calories = 350,
                budget = "Mid",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl3",
                title = "White Fish with Veggies",
                description = "Lean and healthy dinner",
                ingredients = "Cod\nGreen beans\nLemon\nHerbs",
                steps = "Steam fish and beans\nSeason with herbs\nAdd lemon juice",
                time = 15,
                calories = 280,
                budget = "Mid",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl4",
                title = "Vegetable Stir Fry",
                description = "Fast and low calorie",
                ingredients = "Tofu\nBroccoli\nCarrots\nSoy sauce",
                steps = "Sauté tofu and veggies\nAdd splash of soy sauce\nCook until tender",
                time = 15,
                calories = 260,
                budget = "Low",
                dietCategory = "Weight Loss"
            ),
            Recipe(
                id = "wl5",
                title = "Berry Smoothie",
                description = "Nutritious low-calorie snack",
                ingredients = "Skim milk\nMixed berries\nChia seeds",
                steps = "Blend all ingredients\nServe immediately",
                time = 5,
                calories = 220,
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
