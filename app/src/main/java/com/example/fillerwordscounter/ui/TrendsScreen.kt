package com.example.fillerwordscounter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fillerwordscounter.ui.components.SoftBlobs
import com.example.fillerwordscounter.ui.theme.AppColors

@Composable
fun TrendsScreen() {
    Surface(color = AppColors.Cream, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            SoftBlobs()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Trends", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Coming next: 7-day chart + monthly heatmap.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Muted
                )
            }
        }
    }
}