package com.example.nutrismart.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nutrismart.data.local.dao.*
import com.example.nutrismart.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        RecipeEntity::class,
        FavoriteEntity::class,
        MealPlanEntity::class,
        UserProfileEntity::class,
        WeeklyMealPlanEntity::class,
        ShoppingListEntity::class,
        LeftoverInputEntity::class,
        LeftoverRecipeResultEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NutriSmartDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun weeklyMealPlanDao(): WeeklyMealPlanDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun leftoverInputDao(): LeftoverInputDao
    abstract fun leftoverRecipeResultDao(): LeftoverRecipeResultDao

    companion object {
        @Volatile
        private var INSTANCE: NutriSmartDatabase? = null

        fun getDatabase(context: Context): NutriSmartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriSmartDatabase::class.java,
                    "nutrismart_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
