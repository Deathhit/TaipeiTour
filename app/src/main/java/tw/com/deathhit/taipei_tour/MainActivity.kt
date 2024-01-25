package tw.com.deathhit.taipei_tour

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_detail.AttractionDetailFragment
import tw.com.deathhit.feature.attraction_gallery.AttractionGalleryFragment
import tw.com.deathhit.feature.attraction_list.AttractionListFragment
import tw.com.deathhit.feature.image_viewer.ImageViewerFragment
import tw.com.deathhit.taipei_tour.databinding.ActivityMainBinding
import tw.com.deathhit.taipei_tour.model.MainScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        configureFragmentCallbacks()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        savedInstanceState ?: run {
            viewModel.goToInitialScreen()
        }

        bindViewModelState()
    }

    private fun bindViewModelState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                    .collectLatest { actions ->
                        actions.forEach { action ->
                            when (action) {
                                MainActivityViewModel.State.Action.GoBack -> onBackPressedDispatcher.onBackPressed()

                                is MainActivityViewModel.State.Action.GoToInitialScreen -> goToInitialScreen(
                                    screen = action.screen
                                )

                                is MainActivityViewModel.State.Action.GoToScreen -> goToScreen(
                                    screen = action.screen
                                )
                            }

                            viewModel.onAction(action)
                        }
                    }
            }
        }
    }

    private fun configureFragmentCallbacks() {
        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is AttractionDetailFragment -> fragment.callback =
                    object : AttractionDetailFragment.Callback {
                        override fun onGoBack() {
                            viewModel.goBack()
                        }

                        override fun onGoToGalleryScreen(attractionId: String) {
                            viewModel.goToGalleryScreen(attractionId = attractionId)
                        }

                    }

                is AttractionGalleryFragment -> fragment.callback =
                    object : AttractionGalleryFragment.Callback {
                        override fun onGoBack() {
                            viewModel.goBack()
                        }

                        override fun onGoToImageViewerScreen(imageUrl: String) {
                            viewModel.goToImageViewer(imageUrl = imageUrl)
                        }
                    }

                is AttractionListFragment -> fragment.callback =
                    object : AttractionListFragment.Callback {
                        override fun onGoToAttractionDetailsScreen(attractionId: String) {
                            viewModel.goToAttractionDetailsScreen(attractionId = attractionId)
                        }
                    }

                is ImageViewerFragment -> fragment.callback =
                    object : ImageViewerFragment.Callback {
                        override fun onGoBack() {
                            viewModel.goBack()
                        }
                    }
            }
        }
    }

    private fun goToInitialScreen(screen: MainScreen) {
        supportFragmentManager.commit {
            val containerId = binding.container.id

            setReorderingAllowed(true)

            replace(containerId, screen.toFragment(), TAG_MAIN)
        }
    }

    private fun goToScreen(screen: MainScreen) {
        supportFragmentManager.commit {
            val containerId = binding.container.id

            setReorderingAllowed(true)

            setCustomAnimations(
                tw.com.deathhit.core.app_ui.R.anim.slide_in_left,
                tw.com.deathhit.core.app_ui.R.anim.slide_out_right
            )

            replace(containerId, screen.toFragment(), TAG_MAIN)

            addToBackStack(null)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val TAG_MAIN = "$TAG.TAG_MAIN"

        private fun MainScreen.toFragment(): Fragment = when (this) {
            is MainScreen.AttractionDetail -> AttractionDetailFragment.create(attractionId = attractionId)
            MainScreen.AttractionList -> AttractionListFragment.create()
            is MainScreen.Gallery -> AttractionGalleryFragment.create(attractionId = attractionId)
            is MainScreen.ImageViewer -> ImageViewerFragment.create(imageUrl = imageUrl)
        }
    }
}