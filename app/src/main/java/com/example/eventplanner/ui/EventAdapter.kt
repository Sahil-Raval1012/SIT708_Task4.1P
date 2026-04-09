package com.example.eventplanner.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(
    private val onEditClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvTitle.text = event.title
            binding.tvCategory.text = event.category
            binding.tvLocation.text = if (event.location.isNotBlank()) " ${event.location}" else " No location"
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault())
            binding.tvDateTime.text = " ${sdf.format(Date(event.dateTimeMillis))}"
            binding.btnEdit.setOnClickListener { onEditClick(event) }
            binding.btnDelete.setOnClickListener { onDeleteClick(event) }
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
