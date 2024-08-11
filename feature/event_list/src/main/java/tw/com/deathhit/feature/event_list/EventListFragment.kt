package tw.com.deathhit.feature.event_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.event_list.databinding.FragmentEventListBinding

@AndroidEntryPoint
class EventListFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentEventListBinding? = null

    private val viewModel: EventListViewModel by viewModels()

    private val eventListAdapter get() = _eventListAdapter!!
    private var _eventListAdapter: EventListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentEventListBinding.inflate(inflater, container, false)
            .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            adapter = createEventListAdapter().also { _eventListAdapter = it }
            setHasFixedSize(true)
        }

        bindViewModelState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _eventListAdapter = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    is EventListViewModel.State.Action.GoToEventWebsite -> callback?.onGoToEventWebsite(
                                        startUrl = action.startUrl,
                                        title = action.title
                                    )
                                }

                                viewModel.onAction(action)
                            }
                        }
                }

                launch {
                    viewModel.eventPagingDataFlow.collectLatest {
                        eventListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun createEventListAdapter() =
        EventListAdapter {
            viewModel.goToEventWebsite(startUrl = it.websiteUrl, title = it.title)
        }

    companion object {
        fun create() = EventListFragment()
    }

    interface Callback {
        fun onGoToEventWebsite(startUrl: String, title: String)
    }
}