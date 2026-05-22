from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib.units import mm
import textwrap

report = r"""
NutriSmart - Project Full Analysis

1. PROJECT OVERVIEW
- What is this app?
NutriSmart is an Android mobile application built with Jetpack Compose and Kotlin. It helps users discover, plan, and manage meals and recipes. The app provides recipe browsing, detailed recipe views, favorites, daily idea generation, weekly meal planning, shopping lists, and a "leftover" recipe matcher to use ingredients at hand. It also integrates an AI-based recipe generator component.

- What problem does it solve?
NutriSmart aims to reduce meal planning friction by offering personalized recipe suggestions based on diet, budget, and time constraints. It helps reduce food waste by suggesting recipes using leftover ingredients and supports planning weekly meals to keep within time and budget limits.

- Target users
Casual cooks, busy individuals, families, people who track diet preferences, budget-conscious users, and anyone wanting to make use of leftovers.

- Key features (based on code and UI)
  - Authentication & onboarding (EnhancedAuthScreen, UserHabitsFormScreen)
  - Local persistence with Room (users, recipes, favorites, meal plans)
  - AI recipe generation (AIEngine + CerebrasRecipeGenerator)
  - Favorites management and immediate UI feedback (AppViewModel.toggleFavorite)
  - Weekly planner and daily idea generator
  - Shopping list generation from meal plans
  - Safe navigation and null-safety patterns across the app

2. ARCHITECTURE
- Project structure (folders and roles)
  - presentation/: UI code written in Jetpack Compose, navigation, screens, and viewmodels.
  - domain/: business models, AI engine, use cases, domain-level logic.
  - data/: data layer including Room entities, DAOs, repositories, mapping, AI network service, and seed data.
  - util/: utility helpers (SafetyUtils, RecipeImageMapper, Extensions).
  - di/: simple dependency container and ViewModelFactory for wiring objects.

- Design pattern used (MVVM)
  - The project follows MVVM: Composables (View) observe ViewModel state (AppViewModel and others). ViewModels call Repositories (Model/data) which use DAOs (Room) or external services (Retrofit) to fetch or persist data.

- How components communicate
  - UI -> ViewModel: Composables call functions and observe StateFlow / mutableStateOf.
  - ViewModel -> Repository -> DAO/Remote: ViewModel delegates data operations to repositories.
  - Repositories -> Room/Network: Repositories use DAOs for database and Retrofit/OkHttp for network.
  - Example: User taps favorite icon -> UI calls AppViewModel.toggleFavorite(recipe) -> ViewModel calls favoriteRepository.saveFavorite(...) -> FavoriteDao inserts entity -> ViewModel updates local state lists -> UI recomposes.

3. TECHNOLOGIES USED
- Programming language: Kotlin
- Android components and libraries:
  - Jetpack Compose for UI
  - ViewModel, StateFlow, Coroutines for concurrency
  - Room for local storage
  - Retrofit + OkHttp + kotlinx.serialization for AI network
  - Material3 components for UI design
- Gradle setup:
  - Gradle wrapper (gradlew) builds the app. Build files define dependencies, buildConfig fields (Cerebras keys), and annotation processors for Room.

4. UI DESIGN
- Activities
  - MainActivity (Composable setContent) hosts the navigation graph SafeNutriSmartNavGraph. There is also a SplashActivity and some SafeMainActivity variants used during development.

- Layout structure
  - The UI is implemented in Compose; screens are Kotlin files, not XML layouts. Each screen is an independent composable function.

- Theme and styling
  - NutriSmartTheme applies Material3 color schemes and typography. Styling is centralized in the presentation/theme package.

- Navigation flow (screen-by-screen)
  - Start -> Auth screen (EnhancedAuthScreen)
  - If user exists -> Home screen
  - From Home: navigate to Daily Ideas, Leftovers, Meal Planner, Shopping List, Profile, Saved Recipes
  - Recipe details reachable from many screens using a recipeId nav argument. SafeNavGraph checks for null/missing arguments and shows an ErrorFallbackScreen if absent.

5. CORE FUNCTIONALITIES
- Authentication
  - Simple email-based flow stored in SharedPreferences (key: logged_in_email). ViewModel handles signIn, signUp, signOut and persists the session.

- API calls (AI)
  - `CerebrasRecipeGenerator` uses Retrofit + OkHttp to call an external AI endpoint. Responses are cleaned and parsed into AiGeneratedRecipe objects.
  - BuildConfig is used for API key, model, and base URL (safe reflection fallback was added to avoid compile-time errors when fields are missing).

- Data handling
  - Room database: defined in `NutriSmartDatabase.kt` containing entities like UserEntity, RecipeEntity, FavoriteEntity, MealPlanEntity, UserProfileEntity, WeeklyMealPlanEntity, ShoppingListEntity, LeftoverInputEntity, LeftoverRecipeResultEntity.
  - DAOs provide insert/get/delete operations and queries join favorites with recipes.
  - Repositories wrap DAOs to provide a clean API to ViewModels.

- Business logic
  - AppViewModel includes features like loadInitialData(), toggleFavorite(), generateDailyIdeas(), generateWeeklyPlan(), findRecipesByLeftovers(), and meal plan management. It centralizes null-safety and immediate UI updates.

6. SPLASH SCREEN & ICON
- Splash screen
  - Implemented via `SplashActivity.kt` or theme-based approach (check AndroidManifest to confirm which is declared as LAUNCHER). The project contains SplashActivity which would display initial loading.

- App icon
  - Adaptive launcher icons are present in mipmap/drawable folders. `RecipeImageMapper` maps recipes to local drawable resources where available.

7. DATA FLOW
- Typical flow: User action -> ViewModel -> Repository -> DAO/Network -> DB/Remote -> Repository -> ViewModel -> UI state update -> Compose recomposition.
  - Example: User toggles favorite
    1. UI: user taps heart -> calls `AppViewModel.toggleFavorite(recipe)`
    2. ViewModel: checks recipe id, performs DB operation via FavoriteRepository
    3. Repository: inserts/deletes FavoriteEntity via FavoriteDao
    4. ViewModel: updates `savedRecipes` list and `favoriteRecipeIds` StateFlow
    5. UI: observes StateFlow and updates favorite icon immediately.

8. ANDROID MANIFEST (high level)
- Typical permissions: INTERNET (used for AI/network calls). Additional permissions may be present depending on features (check `app/src/main/AndroidManifest.xml`).
- Activities declared: MainActivity, SplashActivity, possibly SafeMainActivity. The LAUNCHER activity is defined in the manifest.

9. BUILD & RUN PROCESS
- Build steps (simplified):
  1. `.\gradlew.bat assembleDebug` compiles Kotlin, runs annotation processors (Room), packages resources, and creates an APK.
  2. `.\gradlew.bat installDebug` installs the APK on a connected device.
- Gradle role: resolves dependencies, runs Kotlin compiler, configures BuildConfig constants, and executes packaging steps.

10. STRENGTHS & IMPROVEMENTS
- Strengths:
  - Clear MVVM separation and modern APIs (Compose, coroutines, Room)
  - AI integration extensible via `AIEngine` and network generators
  - Defensive programming: null-safety, try/catch, logging
  - Immediate UI updates upon DB changes

- Improvements:
  - Secure API keys and credentials properly (avoid storing secrets in code)
  - Add dependency injection (Hilt) for cleaner wiring and testability
  - Add automated tests (unit and instrumented)
  - Replace reflective BuildConfig fallback with proper buildConfig fields in module or central config
  - Fix deprecation warnings (Icons auto-mirroring, Room migration handling)

11. SLIDE-READY SUMMARY
- Slide bullets:
  - NutriSmart: Compose-based recipe planner with AI and Room DB
  - MVVM architecture: ViewModels expose StateFlow -> Compose UI
  - AI: generates recipes and daily ideas (Cerebras generator)
  - Favorites & meal planning: stored locally in Room, immediate UI updates
  - Robustness: null-safety patterns and error handling across the app
  - Next steps: secure secrets, add DI & tests, integrate crash reporting

- Spoken notes (short):
  - "NutriSmart connects UI to local storage and AI generators via clean ViewModels. Users can quickly save favorites, generate daily menu ideas, and plan weekly meals. The code emphasizes stability with safe patterns and extensive logging."

End of report.
"""


def make_pdf(path='NutriSmart_Project_Report.pdf'):
    doc = SimpleDocTemplate(path, pagesize=A4, rightMargin=20*mm, leftMargin=20*mm, topMargin=20*mm, bottomMargin=20*mm)
    styles = getSampleStyleSheet()
    normal = styles['Normal']
    heading = ParagraphStyle('Heading', parent=styles['Heading1'], fontSize=16, spaceAfter=6)
    elements = []

    for block in report.split('\n\n'):
        text = block.strip()
        if not text:
            elements.append(Spacer(1, 6))
            continue
        if text.endswith(':') and len(text.splitlines())==1:
            elements.append(Paragraph(text, heading))
        else:
            wrapped = '\n'.join([textwrap.fill(line, 100) for line in text.splitlines()])
            elements.append(Paragraph(wrapped.replace('\n','<br/>'), normal))
        elements.append(Spacer(1, 4))

    doc.build(elements)
    print('PDF generated at', path)

if __name__ == '__main__':
    make_pdf()

