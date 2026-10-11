package com.termix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.termix.core.proot.ProotPrefs
import com.termix.ui.onboarding.OnboardingScreen
import com.termix.ui.terminal.TerminalScreen
import com.termix.ui.theme.TermixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = ProotPrefs(applicationContext)

        setContent {
            TermixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val installed by prefs.isInstalled.collectAsState(initial = false)

                    if (!installed) {
                        OnboardingScreen(
                            onInstalled = { /* DataStore will update flow */ }
                        )
                    } else {
                        TerminalScreen()
                    }
                }
            }
        }
    }
}
