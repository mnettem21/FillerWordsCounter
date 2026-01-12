package com.example.fillerwordscounter.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.filters.RangeFilter
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.ui.theme.AppColors

@Composable
fun WordFilterChips(
    selectedFilter: WordFilter,
    onFilterSelected: (WordFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WordFilter.values().forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppColors.Orange.copy(alpha = 0.2f),
                    selectedLabelColor = AppColors.Orange,
                    containerColor = AppColors.Card,
                    labelColor = AppColors.Muted
                )
            )
        }
    }
}

@Composable
fun RangeFilterSegmentedControl(
    selectedRange: RangeFilter,
    onRangeSelected: (RangeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppColors.CardAlt,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RangeFilter.values().forEach { range ->
                val selected = selectedRange == range
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .toggleable(
                            value = selected,
                            role = Role.RadioButton,
                            onValueChange = { if (!selected) onRangeSelected(range) }
                        ),
                    color = if (selected) AppColors.Orange else AppColors.CardAlt,
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AnimatedContent(
                            targetState = selected,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "rangeFilter"
                        ) { isSelected ->
                            Text(
                                text = range.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) AppColors.Text else AppColors.Muted
                            )
                        }
                    }
                }
            }
        }
    }
}
