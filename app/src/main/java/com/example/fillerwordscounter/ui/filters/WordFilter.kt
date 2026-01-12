package com.example.fillerwordscounter.ui.filters

enum class WordFilter(val label: String) {
    All("All"),
    Like("Like"),
    Um("Um"),
    Basically("Basically")
}

enum class RangeFilter(val label: String) {
    Week("Week"),
    Month("Month"),
    Year("Year"),
    AllTime("All Time")
}
