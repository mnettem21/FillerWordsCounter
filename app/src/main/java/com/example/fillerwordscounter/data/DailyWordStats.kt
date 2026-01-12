package com.example.fillerwordscounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_word_stats")
data class DailyWordStats(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val totalWords: Int,
    val likeCount: Int,
    val umCount: Int,
    val basicallyCount: Int,
    val updatedAtEpochMs: Long
)