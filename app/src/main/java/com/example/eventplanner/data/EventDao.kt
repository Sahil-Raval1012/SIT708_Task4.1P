package com.example.eventplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*

// Subtask 2: Room @Dao — all database operations defined here
@Dao
interface EventDao {

    // Subtask 1 - READ: Returns all events sorted by date ascending (upcoming first)
    @Query("SELECT * FROM events ORDER BY dateTimeMillis ASC")
    fun getAllEvents(): LiveData<List<Event>>

    // Subtask 1 - CREATE: Insert a new event
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    // Subtask 1 - UPDATE: Modify an existing event by matching its primary key
    @Update
    suspend fun updateEvent(event: Event)

    // Subtask 1 - DELETE: Remove a specific event
    @Delete
    suspend fun deleteEvent(event: Event)

    // Used by AddEditEventFragment to load an event for editing
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): Event?
}
