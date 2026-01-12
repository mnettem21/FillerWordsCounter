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
import com.example.fillerwordscounter.ui.theme.AppColors
import java.text.DecimalFormat

@Composable
fun TrendsScreen(viewModel: TrendsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Surface(color = AppColors.Cream, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SoftBlobs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Trends",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Text
                )

                // Range filter segmented control
                RangeFilterSegmentedControl(
                    selectedRange = state.rangeFilter,
                    onRangeSelected = { viewModel.setRangeFilter(it) }
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
                    label = "trendsKpis"
                ) { currentState ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Total fillers in range
                        KpiCard(
                            title = "Total fillers",
                            value = currentState.totalFillersInRange.toString(),
                            subtitle = "in selected ${currentState.rangeFilter.label.lowercase()}"
                        )

                        // Average filler percentage
                        val percentageFormat = DecimalFormat("#0.0")
                        KpiCard(
                            title = "Average filler percentage",
                            value = "${percentageFormat.format(currentState.averageFillerPercentage)}%",
                            subtitle = "of total words"
                        )
                    }
                }

                // Trendline graph
                AnimatedContent(
                    targetState = state.stats,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "trendline"
                ) { stats ->
                    TrendlineGraph(
                        stats = stats,
                        wordFilter = state.wordFilter
                    )
                }

                // Heatmap calendar
                AnimatedContent(
                    targetState = state,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "heatmap"
                ) { currentState ->
                    HeatmapCalendar(
                        stats = currentState.stats,
                        wordFilter = currentState.wordFilter,
                        rangeFilter = currentState.rangeFilter
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}