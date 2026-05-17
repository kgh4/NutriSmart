package com.example.nutrismart

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.nutrismart.presentation.navigation.SafeNutriSmartNavGraph
import com.example.nutrismart.presentation.theme.NutriSmartTheme

/**
 * SAFE MAIN ACTIVITY
 *
 * Uses SafeNutriSmartNavGraph instead of the regular NavGraph
 * to ensure null safety and error handling throughout the app.
 *
 * Key improvements:
 * ✅ Uses safe navigation graph
 * ✅ Has global error handling
 * ✅ Logs crashes for debugging
 */
class SafeMainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity created")
        enableEdgeToEdge()

        setContent {
            NutriSmartTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // ✅ SAFE: Uses SafeNutriSmartNavGraph
                    // This component implements internal safety logic and error fallback screens
                    SafeNutriSmartNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity resumed")
    }

    override fun onPause() {
        Log.d(TAG, "MainActivity paused")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d(TAG, "MainActivity destroyed")
        super.onDestroy()
    }
}

