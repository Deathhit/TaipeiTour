package tw.com.deathhit.feature.attraction_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.core.app_ui.LoadStateFooterAdapter
import tw.com.deathhit.feature.attraction_list.databinding.FragmentAttractionListBinding

@AndroidEntryPoint
class AttractionListFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentAttractionListBinding? = null

    private val viewModel: AttractionListViewModel by viewModels()

    private val attractionListAdapter get() = _attractionListAdapter!!
    private var _attractionListAdapter: AttractionListAdapter? = null

    private val onSwipeRefreshListener =
        SwipeRefreshLayout.OnRefreshListener { attractionListAdapter.refresh() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentAttractionListBinding.inflate(inflater, container, false)
            .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            adapter = createAttractionListAdapter().also { _attractionListAdapter = it }
                .withLoadStateFooter(LoadStateFooterAdapter())
            setHasFixedSize(true)
        }

        bindLoadState()

        bindViewModelState()
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener(onSwipeRefreshListener)
        }
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _attractionListAdapter = null
    }

    private fun bindLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            attractionListAdapter.loadStateFlow.distinctUntilChanged().collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    is AttractionListViewModel.State.Action.GoToAttractionDetailScreen -> callback?.onGoToAttractionDetailScreen(
                                        action.attractionId
                                    )
                                }

                                viewModel.onAction(action)
                            }
                        }
                }

                launch {
                    viewModel.attractionPagingDataFlow.collectLatest {
                        attractionListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun createAttractionListAdapter() =
        AttractionListAdapter(glideRequestManager = Glide.with(this)) {
            viewModel.goToAttractionDetailScreen(it.attractionId)
        }

    companion object {
        fun create() = AttractionListFragment()
    }

    interface Callback {
        fun onGoToAttractionDetailScreen(attractionId: String)
    }
}