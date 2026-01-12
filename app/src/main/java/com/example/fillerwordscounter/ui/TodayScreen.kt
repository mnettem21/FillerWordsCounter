package com.example.fillerwordscounter.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.components.*
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.ui.theme.AppColors
import java.text.DecimalFormat

@Composable
fun TodayScreen(viewModel: TodayViewModel) {
    val state by viewModel.uiState.collectAsState()

    Surface(color = AppColors.Cream, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SoftBlobs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Text
                )

                // Word filter chips
                WordFilterChips(
                    selectedFilter = state.wordFilter,
                    onFilterSelected = { viewModel.setWordFilter(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // KPI Cards
                AnimatedContent(
                    targetState = state,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "todayKpis"
                ) { currentState ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Total words today
                        KpiCard(
                            title = "Total words today",
                            value = currentState.totalWords.toString()
                        )

                        // Total filler words today
                        KpiCard(
                            title = "Total filler words today",
                            value = currentState.totalFillerWords.toString(),
                            subtitle = when (currentState.wordFilter) {
                                WordFilter.All -> "All fillers"
                                WordFilter.Like -> "Like"
                                WordFilter.Um -> "Um"
                                WordFilter.Basically -> "Basically"
                            }
                        )

                        // Filler percentage
                        val percentageFormat = DecimalFormat("#0.0")
                        KpiCard(
                            title = "Filler percentage",
                            value = "${percentageFormat.format(currentState.fillerPercentage)}%",
                            subtitle = "of total words"
                        )

                        // Daily average filler count
                        KpiCard(
                            title = "Daily average",
                            value = DecimalFormat("#0.0").format(currentState.dailyAverageFillerCount),
                            subtitle = "Average fillers per day (all-time)"
                        )

                        // Delta vs yesterday
                        currentState.deltaVsYesterday?.let { delta ->
                            val deltaText = if (delta >= 0) {
                                "+${DecimalFormat("#0.0").format(delta)}%"
                            } else {
                                "${DecimalFormat("#0.0").format(delta)}%"
                            }
                            val deltaSubtitle = if (delta >= 0) {
                                "Increase vs yesterday"
                            } else {
                                "Decrease vs yesterday"
                            }
                            KpiCard(
                                title = "Change from yesterday",
                                value = deltaText,
                                subtitle = deltaSubtitle
                            )
                        } ?: run {
                            KpiCard(
                                title = "Change from yesterday",
                                value = "—",
                                subtitle = "No data available"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Per-word mini stats
                Text(
                    text = "Per-word breakdown",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.Text
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniStat(
                        title = "Like",
                        value = state.likeCount,
                        modifier = Modifier.weight(1f)
                    )
                    MiniStat(
                        title = "Um",
                        value = state.umCount,
                        modifier = Modifier.weight(1f)
                    )
                    MiniStat(
                        title = "Basically",
                        value = state.basicallyCount,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
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
            Text(title, color = AppColors.Muted, style = MaterialTheme.typography.bodySmall)
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Text
            )
        }
    }
}
