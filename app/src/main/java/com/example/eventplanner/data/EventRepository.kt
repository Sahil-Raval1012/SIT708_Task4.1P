package com.example.eventplanner.data

import androidx.lifecycle.LiveData

// Repository abstracts the data layer from the ViewModel (MVVM best practice)
class EventRepository(private val eventDao: EventDao) {

    // LiveData list auto-updates UI whenever the database changes
    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun insert(event: Event) = eventDao.insertEvent(event)

    suspend fun update(event: Event) = eventDao.updateEvent(event)

    suspend fun delete(event: Event) = eventDao.deleteEvent(event)

    suspend fun getEventById(id: Int): Event? = eventDao.getEventById(id)
}
