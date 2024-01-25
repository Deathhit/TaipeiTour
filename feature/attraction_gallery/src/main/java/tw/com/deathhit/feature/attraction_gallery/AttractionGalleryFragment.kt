package tw.com.deathhit.feature.attraction_gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_gallery.databinding.FragmentAttractionGalleryBinding

@AndroidEntryPoint
class AttractionGalleryFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentAttractionGalleryBinding? = null

    private val viewModel: AttractionGalleryViewModel by viewModels()

    private val attractionImageListAdapter get() = _attractionImageListAdapter!!
    private var _attractionImageListAdapter: AttractionImageListAdapter? = null

    private val onGoBackListener = View.OnClickListener {
        viewModel.goBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentAttractionGalleryBinding.inflate(inflater, container, false)
            .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            adapter = createAttractionListAdapter().also { _attractionImageListAdapter = it }
            setHasFixedSize(true)
        }

        bindViewModelState()
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            toolbar.setNavigationOnClickListener(onGoBackListener)
        }
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            toolbar.setNavigationOnClickListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _attractionImageListAdapter = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    AttractionGalleryViewModel.State.Action.GoBack -> callback?.onGoBack()
                                    is AttractionGalleryViewModel.State.Action.GoToImageViewerScreen -> callback?.onGoToImageViewerScreen(
                                        action.imageUrl
                                    )
                                }

                                viewModel.onAction(action)
                            }
                        }
                }

                launch {
                    viewModel.attractionFlow.filterNotNull().collectLatest {
                        binding.toolbar.title = it.name
                    }
                }

                launch {
                    viewModel.attractionImageListFlow.collectLatest {
                        attractionImageListAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun createAttractionListAdapter() =
        AttractionImageListAdapter(glideRequestManager = Glide.with(this)) {
            viewModel.goToImageViewerScreen(it.imageUrl ?: "")
        }

    companion object {
        fun create(attractionId: String) = AttractionGalleryFragment().apply {
            arguments = AttractionGalleryViewModel.createArgs(attractionId = attractionId)
        }
    }

    interface Callback {
        fun onGoBack()
        fun onGoToImageViewerScreen(imageUrl: String)
    }
}