package tw.com.deathhit.feature.attraction_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_list.databinding.FragmentAttractionListBinding
import tw.com.deathhit.feature.set_language.SetLanguageFragment

@AndroidEntryPoint
class AttractionListFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentAttractionListBinding? = null

    private val viewModel: AttractionListViewModel by viewModels()

    private val attractionListAdapter get() = _attractionListAdapter!!
    private var _attractionListAdapter: AttractionListAdapter? = null

    private val setLanguageFragment get() = childFragmentManager.findFragmentByTag(TAG_SET_LANGUAGE) as SetLanguageFragment?

    private val onClickMenuItemListener = Toolbar.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_setLanguage -> {
                viewModel.setLanguage()

                true
            }

            else -> false
        }
    }

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
            setHasFixedSize(true)
        }

        with(binding.toolbar) {
            title = getString(context.applicationInfo.labelRes)
        }

        bindViewModelState()
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            toolbar.setOnMenuItemClickListener(onClickMenuItemListener)
        }
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            toolbar.setOnMenuItemClickListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _attractionListAdapter = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    is AttractionListViewModel.State.Action.GoToAttractionDetailScreen -> callback?.onGoToAttractionDetailsScreen(
                                        action.attractionId
                                    )

                                    AttractionListViewModel.State.Action.SetLanguage -> setLanguageFragment ?: run {
                                        SetLanguageFragment.create().show(childFragmentManager, TAG_SET_LANGUAGE)
                                    }
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
        private const val TAG = "AttractionListFragment"
        private const val TAG_SET_LANGUAGE = "$TAG.SET_LANGUAGE"

        fun create() = AttractionListFragment()
    }

    interface Callback {
        fun onGoToAttractionDetailsScreen(attractionId: String)
    }
}