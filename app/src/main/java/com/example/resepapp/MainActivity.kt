package com.example.resepapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.resepapp.ui.navigation.AppNavigation
import com.example.resepapp.ui.theme.ResepAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResepAppTheme {
                AppNavigation()
            }
        }
    }
}
