package com.example.fillerwordscounter.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.components.CharacterMood
import com.example.fillerwordscounter.ui.components.CuteCharacter
import com.example.fillerwordscounter.ui.components.SoftBlobs
import com.example.fillerwordscounter.ui.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    isListening: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onGoToStats: () -> Unit
) {
    var mood by remember { mutableStateOf(CharacterMood.Calm) }
    var sparkleTrigger by remember { mutableIntStateOf(0) }

    // Prevent first composition from triggering stop->celebrate
    var prevListening by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(isListening) {
        when {
            // First composition: ALWAYS calm
            prevListening == null -> {
                mood = CharacterMood.Calm
            }

            // Just started
            prevListening == false && isListening -> {
                mood = CharacterMood.Listening
            }

            // Just stopped
            prevListening == true && !isListening -> {
                mood = CharacterMood.Celebrating
                delay(2200) // slower return to calm
                mood = CharacterMood.Calm
            }

            // Staying idle
            !isListening -> {
                mood = CharacterMood.Calm
            }
        }

        prevListening = isListening
    }

    Surface(color = AppColors.Cream, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SoftBlobs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(0.6f))

                // Character + sparkles + smooth transitions between moods
                Box(contentAlignment = Alignment.TopCenter) {
                    AnimatedContent(
                        targetState = mood,
                        transitionSpec = {
                            (fadeIn(tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220))) togetherWith
                                    (fadeOut(tween(220)) + scaleOut(targetScale = 1.02f, animationSpec = tween(220)))
                        },
                        label = "moodTransition"
                    ) { m ->
                        CuteCharacter(
                            mood = m,
                            modifier = Modifier.size(320.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Status (you can also animate this if you want, but this is already clean)
                Text(
                    text = if (isListening) "Listening…" else "Idle",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F1F1F)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = if (isListening)
                        "I’m catching your filler words."
                    else
                        "Tap start when you’re ready.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = AppColors.Muted
                )

                Spacer(Modifier.weight(0.5f))

                Button(
                    onClick = {
                        if (isListening) {
                            onStop()
                        } else {
                            sparkleTrigger += 1
                            onStart()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Orange)
                ) {
                    Text(
                        text = if (isListening) "Stop Listening" else "Start Listening",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F1F1F)
                    )
                }

                Spacer(Modifier.height(18.dp))
            }
        }
    }
}