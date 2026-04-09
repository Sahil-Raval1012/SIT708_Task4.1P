package com.example.eventplanner.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventDatabase
import com.example.eventplanner.data.EventRepository
import kotlinx.coroutines.launch
class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>
    init {
        val dao = EventDatabase.getDatabase(application).eventDao()
        repository = EventRepository(dao)
        allEvents = repository.allEvents
    }
    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }
    fun update(event: Event) = viewModelScope.launch {
        repository.update(event)
    }
    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }
    suspend fun getEventById(id: Int): Event? = repository.getEventById(id)
}
