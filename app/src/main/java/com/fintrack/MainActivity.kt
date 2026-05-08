package com.fintrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fintrack.ui.navigation.FinTrackApp
import com.fintrack.ui.theme.FinTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as FinTrackApplication).appContainer
        setContent {
            FinTrackTheme {
                FinTrackApp(appContainer = container)
            }
        }
    }
}
