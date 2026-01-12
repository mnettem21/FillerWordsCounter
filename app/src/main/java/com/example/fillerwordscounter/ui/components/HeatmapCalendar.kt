package com.example.fillerwordscounter.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.data.DailyWordStats
import com.example.fillerwordscounter.ui.filters.RangeFilter
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.ui.theme.AppColors
import com.example.fillerwordscounter.utils.DateUtils
import kotlin.math.max

@Composable
fun HeatmapCalendar(
    stats: List<DailyWordStats>,
    wordFilter: WordFilter,
    rangeFilter: RangeFilter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardAlt),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Get date range
            val (startDate, endDate) = when (rangeFilter) {
                RangeFilter.Week -> DateUtils.getWeekRange()
                RangeFilter.Month -> DateUtils.getMonthRange()
                RangeFilter.Year -> DateUtils.getYearRange()
                RangeFilter.AllTime -> DateUtils.getAllTimeRange()
            }

            val daysInRange = DateUtils.getDaysInRange(startDate, endDate)
            val statsMap = stats.associateBy { it.date }

            // Calculate max value for normalization
            val maxValue = max(
                1f,
                stats.maxOfOrNull { stat ->
                    when (wordFilter) {
                        WordFilter.All -> stat.likeCount + stat.umCount + stat.basicallyCount
                        WordFilter.Like -> stat.likeCount
                        WordFilter.Um -> stat.umCount
                        WordFilter.Basically -> stat.basicallyCount
                    }
                }?.toFloat() ?: 1f
            )

            // Determine grid dimensions
            val daysToShow = daysInRange.size
            val cols = when {
                daysToShow <= 7 -> daysToShow
                daysToShow <= 30 -> 7
                else -> 14 // For year view, show 14 columns (about 2 weeks per row)
            }
            val rows = (daysToShow + cols - 1) / cols

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((rows * 32 + (rows - 1) * 8).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cellSize = (size.width - (cols - 1) * 8f) / cols
                    val spacing = 8f

                    daysInRange.forEachIndexed { index, date ->
                        val row = index / cols
                        val col = index % cols

                        val x = col * (cellSize + spacing)
                        val y = row * (cellSize + spacing)

                        val stat = statsMap[date]
                        val fillerCount = stat?.let {
                            when (wordFilter) {
                                WordFilter.All -> it.likeCount + it.umCount + it.basicallyCount
                                WordFilter.Like -> it.likeCount
                                WordFilter.Um -> it.umCount
                                WordFilter.Basically -> it.basicallyCount
                            }
                        } ?: 0

                        val intensity = (fillerCount.toFloat() / maxValue).coerceIn(0f, 1f)

                        // Warm gradient from cream to orange
                        val color = when {
                            intensity == 0f -> AppColors.Card
                            intensity < 0.33f -> Color(
                                red = 1f,
                                green = 0.9f + (intensity * 0.1f),
                                blue = 0.8f + (intensity * 0.1f)
                            )
                            intensity < 0.66f -> Color(
                                red = 1f,
                                green = 0.7f + ((intensity - 0.33f) * 0.2f),
                                blue = 0.5f + ((intensity - 0.33f) * 0.2f)
                            )
                            else -> AppColors.Orange.copy(alpha = 0.6f + (intensity - 0.66f) * 0.4f)
                        }

                        drawRoundRect(
                            color = color,
                            topLeft = Offset(x, y),
                            size = Size(cellSize, cellSize),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                    }
                }
            }
        }
    }
}
