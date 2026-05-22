package com.example.nutrismart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install SplashScreen for Android 12+ (this should be called before super.onCreate)
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Set content view (fallback for pre-Android 12 or custom branding)
        setContentView(R.layout.activity_splash)
        
        // Transition to MainActivity after a short delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            // Avoid transition animation for a seamless look
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2000)
    }
}