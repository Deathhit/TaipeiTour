package tw.com.deathhit.feature.attraction_detail

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_detail.databinding.FragmentAttractionDetailBinding

@AndroidEntryPoint
class AttractionDetailFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentAttractionDetailBinding? = null

    private val viewModel: AttractionDetailViewModel by viewModels()

    private val onGoBackListener = View.OnClickListener {
        viewModel.goBack()
    }
    private val onClickMenuItemListener = Toolbar.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_gallery -> {
                viewModel.goToGalleryScreen()

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
        FragmentAttractionDetailBinding.inflate(inflater, container, false)
            .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.textViewWebsiteUrl) {
            movementMethod = LinkMovementMethod.getInstance()
        }

        bindViewModelState()
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            toolbar.setNavigationOnClickListener(onGoBackListener)
            toolbar.setOnMenuItemClickListener(onClickMenuItemListener)
        }
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            toolbar.setNavigationOnClickListener(null)
            toolbar.setOnMenuItemClickListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                sequenceOf(
                                    when (action) {
                                        is AttractionDetailViewModel.State.Action.GoBack -> callback?.onGoBack()
                                        is AttractionDetailViewModel.State.Action.GoToGalleryScreen -> callback?.onGoToGalleryScreen(
                                            attractionId = action.attractionId
                                        )
                                    }
                                )

                                viewModel.onAction(action)
                            }
                        }
                }

                launch {
                    viewModel.attractionFlow.filterNotNull().distinctUntilChanged()
                        .collectLatest { attraction ->
                            with(binding) {
                                imageViewAttraction.let {
                                    Glide.with(it).load(attraction.imageUrl).into(it)
                                }
                                textViewAddress.text = attraction.address
                                textViewIntroduction.text = Html.fromHtml(
                                    attraction.introduction,
                                    Html.FROM_HTML_MODE_LEGACY
                                )
                                textViewUpdateTime.text = attraction.updateTimeText
                                textViewWebsiteUrl.text = attraction.websiteUrl
                                toolbar.title = attraction.name
                            }
                        }
                }
            }
        }
    }

    companion object {
        fun create(attractionId: String) = AttractionDetailFragment().apply {
            arguments = AttractionDetailViewModel.createArgs(attractionId = attractionId)
        }
    }

    interface Callback {
        fun onGoBack()
        fun onGoToGalleryScreen(attractionId: String)
    }
}