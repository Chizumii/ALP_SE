package com.example.alp_se

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.alp_se.navigation.AppRouting
import com.example.alp_se.ui.theme.ALP_SETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ALP_SETheme {
                AppRouting()
            }
        }
    }
}
