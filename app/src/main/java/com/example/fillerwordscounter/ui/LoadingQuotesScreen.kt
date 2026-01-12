package com.example.fillerwordscounter.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.components.CharacterMood
import com.example.fillerwordscounter.ui.components.CuteCharacter
import com.example.fillerwordscounter.ui.components.SoftBlobs
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LoadingQuotesScreen(
    onDone: () -> Unit,
) {
    val allQuotes = remember {
        listOf(
            "Carve every word before you let it fall.\n— Oliver Wendell Holmes Sr.",
            "Let every word tell.\n— William Strunk Jr.",
            "Never use two words when one will do.\n— Thomas Jefferson",
            "Eliminate the unnecessary so that the necessary may speak.\n— Hans Hofmann",
            "Brevity is the soul of wit.\n— William Shakespeare"
        )
    }

    // Pick exactly 2 quotes per run
    val selectedQuotes = remember {
        allQuotes.shuffled(Random(System.currentTimeMillis())).take(2)
    }

    var idx by remember { mutableIntStateOf(0) }

    // Show quote 1, then quote 2, then navigate. No looping.
    LaunchedEffect(Unit) {
        delay(2200)
        idx = 1
        delay(2200)
        onDone()
    }

    val bg = Color(0xFFF7F1E8)

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            SoftBlobs()

            // This Column controls vertical placement (weights work here)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Increase this weight to push quote LOWER; decrease to move it UP
                Spacer(modifier = Modifier.weight(1.05f))

                // Stable-height quote area to prevent first-frame jump
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(
                        targetState = selectedQuotes[idx],
                        animationSpec = tween(650),
                        label = "quoteCrossfade"
                    ) { quote ->
                        Text(
                            text = quote,
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF1F1F1F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
                AestheticLoadingRing()

                Spacer(modifier = Modifier.weight(0.95f))
            }

            // Big character peeking from bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = 55.dp), // push down so only top shows
                contentAlignment = Alignment.BottomCenter
            ) {
                CuteCharacter(
                    mood = CharacterMood.Calm,
                    modifier = Modifier.size(420.dp)
                )
            }
        }
    }
}