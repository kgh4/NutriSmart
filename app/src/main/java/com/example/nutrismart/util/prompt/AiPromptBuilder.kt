package com.example.nutrismart.util.prompt

import com.example.nutrismart.domain.ai.model.SmartAiRequest

/**
 * Handles the logic of converting user state into a strict, structured AI prompt.
 */
object AiPromptBuilder {
    fun buildRecipePrompt(request: SmartAiRequest): String {
        val ingredientContext = when {
            request.leftoverItems.isNotEmpty() -> "FOCUS on using these leftovers: ${request.leftoverItems.joinToString(", ")}."
            request.availableIngredients.isNotEmpty() -> "USE these ingredients as a base: ${request.availableIngredients.joinToString(", ")}."
            else -> "CHOOSE fresh seasonal ingredients available in a Mediterranean market."
        }

        val allergyContext = if (request.allergies.isNotEmpty()) {
            "CRITICAL: STRICTLY EXCLUDE these allergens: ${request.allergies.joinToString(", ")}."
        } else ""

        val localization = if (request.preferTunisian) {
            "CONTEXT: The user is in Tunisia. Prefer Tunisian or Mediterranean style flavors (Harissa, Cumin, Saffron, Olive Oil)."
        } else ""

        return """
            SYSTEM: You are NutriSmart AI, a professional chef and clinical nutritionist.
            GOAL: Generate ONE healthy, practical recipe matching the user's constraints.
            
            CONSTRAINTS:
            - Mood: ${request.mood}
            - Diet: ${request.dietCategory}
            - Budget: ${request.budgetLevel}
            - Time: ${request.maxTimeMinutes} min max
            - Meal: ${request.mealType}
            - Skill: ${request.cookingSkill}
            - $ingredientContext
            - $allergyContext
            - $localization
            
            OUTPUT FORMAT: Return ONLY a valid JSON object with this exact structure:
            {
              "title": "Clear Name",
              "description": "Catchy 1-sentence hook",
              "ingredients": ["qty item", "qty item"],
              "steps": ["Step 1", "Step 2"],
              "mealType": "${request.mealType}",
              "calories": 450,
              "timeMinutes": 25,
              "budgetLevel": "${request.budgetLevel}",
              "dietCategory": "${request.dietCategory}",
              "estimatedCost": 4.5,
              "difficulty": "Easy",
              "substitutions": {"item": "replacement"},
              "whyItFits": "Explain how this matches mood/diet",
              "shoppingItems": ["item to buy"],
              "warnings": ["Warning text"]
            }
            
            Ensure steps are logical for a home cook. If ingredients are missing from the available list, add them to 'shoppingItems'.
        """.trimIndent()
    }
}
