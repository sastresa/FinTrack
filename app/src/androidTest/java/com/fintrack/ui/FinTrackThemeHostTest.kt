package com.fintrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.fintrack.domain.model.Settings
import com.fintrack.ui.theme.FinTrackThemeHost
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FinTrackThemeHostTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun hostUsesDarkThemeFromSettingsFlow() {
        val settings = MutableStateFlow(Settings(darkModeEnabled = true))

        composeRule.setContent {
            FinTrackThemeHost(settings = settings) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                        .testTag("theme-surface"),
                )
            }
        }

        val image = composeRule.onNodeWithTag("theme-surface").captureToImage()
        val center = image.toPixelMap()[image.width / 2, image.height / 2]

        assertEquals(Color(0xFF101513), center)
    }
}
