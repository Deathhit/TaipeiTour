package tw.com.deathhit.taipei_tour

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_detail.AttractionDetailFragment
import tw.com.deathhit.feature.attraction_gallery.AttractionGalleryFragment
import tw.com.deathhit.feature.image_viewer.ImageViewerFragment
import tw.com.deathhit.feature.navigation.NavigationFragment
import tw.com.deathhit.taipei_tour.databinding.ActivityMainBinding
import tw.com.deathhit.taipei_tour.model.MainScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    private val navController by lazy {
        findNavController(R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configureFragmentCallbacks()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
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
        supportFragmentManager.addFragmentOnAttachListener { _, hostFragment ->
            when (hostFragment) {
                is NavHostFragment -> hostFragment.childFragmentManager.addFragmentOnAttachListener { _, fragment ->
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

                        is ImageViewerFragment -> fragment.callback =
                            object : ImageViewerFragment.Callback {
                                override fun onGoBack() {
                                    viewModel.goBack()
                                }
                            }

                        is NavigationFragment -> fragment.callback = object : NavigationFragment.Callback {
                            override fun onGoToAttractionDetailScreen(attractionId: String) {
                                viewModel.goToAttractionDetailScreen(attractionId = attractionId)
                            }

                            override fun onGoToEventDetailScreen(eventId: String) {
                                viewModel.goToEventDetailScreen(eventId = eventId)
                            }

                        }
                    }
                }
            }
        }
    }

    private fun goToScreen(screen: MainScreen) {
        when (screen) {
            is MainScreen.AttractionDetail -> navController.navigate(
                R.id.action_attractionDetail,
                AttractionDetailFragment.createArgs(attractionId = screen.attractionId)
            )

            is MainScreen.EventDetail -> TODO()

            is MainScreen.Gallery -> navController.navigate(
                R.id.action_attractionGallery,
                AttractionGalleryFragment.createArgs(attractionId = screen.attractionId)
            )

            is MainScreen.ImageViewer -> navController.navigate(
                R.id.action_imageViewer,
                ImageViewerFragment.createArgs(imageUrl = screen.imageUrl)
            )
        }
    }
}