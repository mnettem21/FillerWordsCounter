package com.example.fillerwordscounter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.components.SoftBlobs
import com.example.fillerwordscounter.ui.theme.AppColors

@Composable
fun StatsScreen(vm: TodayViewModel) {
    val state by vm.uiState.collectAsState()

    Surface(color = AppColors.Cream, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SoftBlobs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F1F1F)
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardAlt),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Total words", color = AppColors.Muted)
                        Text(
                            text = state.totalWords.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniStat(title = "Like", value = state.likeCount, modifier = Modifier.weight(1f))
                    MiniStat(title = "Um", value = state.umCount, modifier = Modifier.weight(1f))
                    MiniStat(title = "Basically", value = state.basicallyCount, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = "We only store counts, not transcripts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.Muted
                )
            }
        }
    }
}

@Composable
private fun MiniStat(title: String, value: Int, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, color = AppColors.Muted)
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}