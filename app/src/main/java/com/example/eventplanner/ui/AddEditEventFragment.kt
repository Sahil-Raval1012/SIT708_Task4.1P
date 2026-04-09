package com.example.eventplanner.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.FragmentAddEditEventBinding
import com.example.eventplanner.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditEventFragment : Fragment() {
    private var _binding: FragmentAddEditEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by activityViewModels()

    // Subtask 3: Safe Args — receives eventId passed from EventListFragment
    private val args: AddEditEventFragmentArgs by navArgs()

    private var selectedCalendar: Calendar = Calendar.getInstance()
    private var isDateSelected = false
    private var existingEvent: Event? = null

    // Subtask 1: All categories required
    private val categories = arrayOf("Work", "Social", "Travel", "Health", "Personal", "Other")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()

        val eventId = args.eventId

        if (eventId != -1) {

            lifecycleScope.launch {
                val event = viewModel.getEventById(eventId)
                event?.let {
                    existingEvent = it
                    populateFields(it)
                }
            }
            binding.tvFormTitle.text = "Edit Event"
            binding.btnSave.text = "Update Event"
        } else {
            binding.tvFormTitle.text = "Add New Event"
            binding.btnSave.text = "Save Event"
        }

        binding.btnPickDateTime.setOnClickListener { showDatePicker() }
        binding.btnSave.setOnClickListener { saveEvent() }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupCategorySpinner() {
        val spinnerAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as? TextView)?.apply {
                    setTextColor(Color.parseColor("#FF111111"))
                    setBackgroundColor(Color.parseColor("#FFE8E8EB"))
                    textSize = 15f
                    setPadding(42, 0, 0, 0)
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as? TextView)?.apply {
                    setTextColor(Color.parseColor("#FF111111"))
                    setBackgroundColor(Color.parseColor("#FFE8E8EB"))
                    textSize = 15f
                    setPadding(48, 32, 48, 32)
                }
                return view
            }
        }

        binding.spinnerCategory.adapter = spinnerAdapter
        // Removes the default Android hamburger/arrow drawable completely
        binding.spinnerCategory.background = ColorDrawable(Color.parseColor("#FFE8E8EB"))
    }

    private fun populateFields(event: Event) {
        binding.etTitle.setText(event.title)
        binding.etLocation.setText(event.location)
        val idx = categories.indexOf(event.category)
        if (idx >= 0) binding.spinnerCategory.setSelection(idx)
        selectedCalendar.timeInMillis = event.dateTimeMillis
        isDateSelected = true
        updateDateTimeLabel()
    }

    private fun showDatePicker() {
        val now = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, month)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, day)
                showTimePicker()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour)
                selectedCalendar.set(Calendar.MINUTE, minute)
                selectedCalendar.set(Calendar.SECOND, 0)
                isDateSelected = true
                updateDateTimeLabel()
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun updateDateTimeLabel() {
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault())
        binding.tvSelectedDateTime.text = sdf.format(selectedCalendar.time)
        binding.tvSelectedDateTime.visibility = View.VISIBLE
    }

    private fun saveEvent() {
        val title = binding.etTitle.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()


        if (title.isEmpty()) {
            Snackbar.make(binding.root, " Title cannot be empty!", Snackbar.LENGTH_SHORT).show()
            binding.etTitle.requestFocus()
            return
        }

        if (!isDateSelected) {
            Snackbar.make(binding.root, " Please select a date and time!", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (existingEvent == null && selectedCalendar.timeInMillis <= System.currentTimeMillis()) {
            Snackbar.make(binding.root, "Cannot create an event in the past!", Snackbar.LENGTH_SHORT).show()
            return
        }

        val event = Event(
            id = existingEvent?.id ?: 0,
            title = title,
            category = category,
            location = location,
            dateTimeMillis = selectedCalendar.timeInMillis
        )

        if (existingEvent == null) {
            viewModel.insert(event)
            Snackbar.make(binding.root, "✅ Event \"$title\" added!", Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.update(event)
            Snackbar.make(binding.root, "✅ Event \"$title\" updated!", Snackbar.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}