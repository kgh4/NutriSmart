# 🍏 NutriSmart — Smart Nutrition Management Android App

## 📌 Overview

**NutriSmart** is a modern Android mobile application developed using **Kotlin** and **Jetpack Compose** that helps users manage their nutrition, organize meals, discover recipes, and reduce food waste intelligently.

The application combines:

- 📖 Recipe discovery
- 📅 Daily & weekly meal planning
- ❤️ Favorite recipes management
- 🤖 AI-generated meal ideas
- 🥘 Leftover ingredient recipe suggestions
- 🛒 Shopping list generation
- 💾 Offline local storage using Room Database

NutriSmart is designed for users who want a healthier, more organized, and budget-friendly food lifestyle.

---

# ✨ Features

## 🔐 Authentication & Onboarding

- User sign up & login
- Persistent sessions using SharedPreferences
- User habits & dietary preference onboarding
- Budget and cooking-time preferences

---

## 🍽 Recipe Management

- Browse recipes
- View detailed recipe information
- Save favorite recipes
- Search and filter recipes

---

## 🤖 AI-Powered Features

NutriSmart integrates AI to generate:

- Daily meal ideas
- Weekly meal plans
- Recipes from leftover ingredients

AI generation is handled through:

- `AIEngine.kt`
- `CerebrasRecipeGenerator.kt`

---

## 📅 Meal Planning

- Daily planner
- Weekly planner
- Personalized suggestions
- Shopping list generation

---

## 🥘 Leftover Recipe Finder

Users can enter available ingredients and receive recipe recommendations based on ingredient matching.

This feature helps:
- Reduce food waste
- Save money
- Reuse ingredients efficiently

---

# 🏗 Architecture

NutriSmart follows the **MVVM (Model-View-ViewModel)** architecture pattern.

```text
UI (Compose Screens)
        ↓
ViewModel
        ↓
Repository
        ↓
Room Database / AI API
```

---

# 📂 Project Structure

```text
com.example.nutrismart/
│
├── presentation/
│   ├── screens/
│   ├── navigation/
│   ├── components/
│   ├── theme/
│   └── viewmodel/
│
├── domain/
│   ├── model/
│   ├── repository/
│   ├── usecase/
│   └── ai/
│
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── db/
│   │
│   ├── repository/
│   ├── remote/
│   └── ai/
│
├── util/
│
└── di/
```

---

# 🧠 Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Jetpack Compose | Declarative UI |
| Material 3 | UI design system |
| MVVM | Architecture pattern |
| Room Database | Local persistence |
| Retrofit | API communication |
| OkHttp | Network client |
| Kotlin Coroutines | Asynchronous operations |
| StateFlow | Reactive state management |
| Kotlinx Serialization | JSON parsing |
| SharedPreferences | Session persistence |

---

# 🗄 Database

The app uses **Room Database** for local storage.

## Main Entities

- `UserEntity`
- `RecipeEntity`
- `FavoriteEntity`
- `MealPlanEntity`
- `WeeklyMealPlanEntity`
- `ShoppingListEntity`
- `UserProfileEntity`

---

# 🔄 Data Flow

```text
User Interaction
       ↓
Composable Screen
       ↓
ViewModel
       ↓
Repository
       ↓
DAO
       ↓
Room Database
```

---

# 🎨 UI & Navigation

The UI is fully built with **Jetpack Compose**.

## Main Screens

- HomeScreen
- RecipeDetailsScreen
- DailyIdeasScreen
- WeeklyPlannerScreen
- LeftoverRecipesScreen
- ShoppingListScreen
- ProfileScreen
- EnhancedAuthScreen

Navigation is handled through:

```kotlin
SafeNutriSmartNavGraph.kt
```

---

# 🤖 AI Integration

NutriSmart integrates AI using the Cerebras API.

## AI Components

### `AIEngine.kt`

Handles:
- Local recommendation logic
- Offline suggestions
- Ingredient matching

### `CerebrasRecipeGenerator.kt`

Handles:
- API communication
- Prompt generation
- JSON parsing
- AI recipe generation

---

# 🔒 Security

The project includes:

- Safe navigation argument validation
- Null-safe Kotlin code
- Centralized error handling
- API key protection through `BuildConfig`

---

# ⚡ Performance Optimizations

- Coroutines for background operations
- StateFlow reactive updates
- Compose recomposition optimization
- Room compile-time query validation
- Efficient local caching

---

# 🧪 Testing

## Tested Features

- Authentication flow
- Recipe browsing
- Favorites management
- AI recipe generation
- Weekly planner
- Leftover finder
- Shopping list generation

---

# 🚀 Build & Run

## Requirements

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK
- Gradle

---

## Clone Project

```bash
git clone https://github.com/yourusername/NutriSmart.git
```

---

## Open in Android Studio

Open the project folder in Android Studio.

---

## Build APK

```bash
./gradlew assembleDebug
```

---

## Install APK

```bash
./gradlew installDebug
```

---

# 📱 Screenshots

## Authentication Screen

![Auth Screen](screenshots/auth_screen.png)

---

## Home Screen

![Home Screen](screenshots/home_screen.png)

---

## Recipe Details

![Recipe Details](screenshots/recipe_details.png)

---

## Weekly Planner

![Weekly Planner](screenshots/weekly_planner.png)

---

## Leftover Finder

![Leftover Finder](screenshots/leftover_screen.png)

---

# 📊 Planned Improvements

- Hilt Dependency Injection
- Firebase Authentication
- Cloud synchronization
- Push notifications
- Advanced AI recommendations
- Recipe image recognition
- Paging 3 integration
- Dark mode improvements

---

# 💪 Project Strengths

✅ Modern Android architecture  
✅ Jetpack Compose UI  
✅ Reactive state management  
✅ AI-powered features  
✅ Offline-first capability  
✅ Clean separation of concerns  
✅ Scalable structure  

---

# 👨‍💻 Authors

NutriSmart Development Team

Academic Year 2024–2025

---

# 📄 License

This project is for educational and academic purposes.
