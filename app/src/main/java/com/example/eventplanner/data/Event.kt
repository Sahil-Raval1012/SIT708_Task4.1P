package com.example.eventplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Subtask 2: Room @Entity maps this class to a database table
// Subtask 1: Contains all required fields — Title, Category, Location, Date/Time
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,       // Subtask 1: Title field
    val category: String,    // Subtask 1: Category (Work, Social, Travel, etc.)
    val location: String,    // Subtask 1: Location field
    val dateTimeMillis: Long // Subtask 1: Date/Time stored as epoch ms for easy sorting
)
