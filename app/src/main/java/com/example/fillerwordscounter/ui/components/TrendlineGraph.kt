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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.data.DailyWordStats
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.ui.theme.AppColors
import kotlin.math.max

@Composable
fun TrendlineGraph(
    stats: List<DailyWordStats>,
    wordFilter: WordFilter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardAlt),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            if (stats.isEmpty()) {
                // Empty state
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val padding = 40f
                    val graphWidth = size.width - padding * 2
                    val graphHeight = size.height - padding * 2

                    // Extract filler counts based on filter
                    val fillerCounts = stats.map { stat ->
                        when (wordFilter) {
                            WordFilter.All -> stat.likeCount + stat.umCount + stat.basicallyCount
                            WordFilter.Like -> stat.likeCount
                            WordFilter.Um -> stat.umCount
                            WordFilter.Basically -> stat.basicallyCount
                        }
                    }

                    val maxValue = max(1f, fillerCounts.maxOrNull()?.toFloat() ?: 1f)

                    // Draw subtle grid lines
                    drawLine(
                        color = AppColors.Muted.copy(alpha = 0.2f),
                        start = Offset(padding, padding),
                        end = Offset(padding, size.height - padding),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = AppColors.Muted.copy(alpha = 0.2f),
                        start = Offset(padding, size.height - padding),
                        end = Offset(size.width - padding, size.height - padding),
                        strokeWidth = 1f
                    )

                    // Draw trendline
                    if (fillerCounts.isNotEmpty()) {
                        val path = Path()
                        val stepX = if (fillerCounts.size > 1) graphWidth / (fillerCounts.size - 1) else 0f

                        fillerCounts.forEachIndexed { index, value ->
                            val x = padding + (index * stepX)
                            val normalizedValue = value / maxValue
                            val y = size.height - padding - (normalizedValue * graphHeight)
                            val point = Offset(x, y)

                            if (index == 0) {
                                path.moveTo(point.x, point.y)
                            } else {
                                path.lineTo(point.x, point.y)
                            }
                        }

                        // Draw smooth rounded line
                        drawPath(
                            path = path,
                            color = AppColors.Orange,
                            style = Stroke(width = 3f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )

                        // Draw points
                        fillerCounts.forEachIndexed { index, value ->
                            val x = padding + (index * stepX)
                            val normalizedValue = value / maxValue
                            val y = size.height - padding - (normalizedValue * graphHeight)
                            drawCircle(
                                color = AppColors.Orange,
                                radius = 4f,
                                center = Offset(x, y)
                            )
                        }
                    }
                }
            }
        }
    }
}
