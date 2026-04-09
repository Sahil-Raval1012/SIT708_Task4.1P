package com.example.eventplanner.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.eventplanner.R
import com.example.eventplanner.databinding.FragmentEventListBinding
import com.example.eventplanner.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar
class EventListFragment : Fragment() {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EventAdapter(
            onEditClick = { event ->
                val action = EventListFragmentDirections
                    .actionEventListFragmentToAddEditEventFragment(event.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { event ->
                viewModel.delete(event)
                Snackbar.make(
                    binding.root,
                    "\"${event.title}\" deleted successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerView.adapter = adapter
        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            binding.tvEmptyState.visibility =
                if (events.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.fabAddEvent.setOnClickListener {
            val action = EventListFragmentDirections
                .actionEventListFragmentToAddEditEventFragment(-1) // -1 means new event
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
