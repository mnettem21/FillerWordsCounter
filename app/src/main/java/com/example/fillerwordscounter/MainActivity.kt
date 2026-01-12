package com.example.fillerwordscounter

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.fillerwordscounter.service.ListeningService
import androidx.activity.viewModels
import com.example.fillerwordscounter.ui.TodayViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.fillerwordscounter.ui.AppNav
import com.example.fillerwordscounter.ui.theme.FillerWordsCounterTheme

class MainActivity : ComponentActivity() {

    private val todayVm: TodayViewModel by viewModels()
    private var isListening by mutableStateOf(false)

    private val requestMicPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startListeningService()
                isListening = true
            }
        }

    private fun startListeningService() {
        val intent = Intent(this, ListeningService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android 13+ needs notification permission for the foreground notification to show.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            FillerWordsCounterTheme {
                AppNav(
                    todayVm = todayVm,
                    isListening = isListening,
                    onStart = { requestMicPermission.launch(android.Manifest.permission.RECORD_AUDIO) },
                    onStop = {
                        val intent = Intent(this, com.example.fillerwordscounter.service.ListeningService::class.java)
                        stopService(intent)
                        isListening = false
                    }
                )
            }
        }
    }
}