package tw.com.deathhit.feature.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.attraction_list.AttractionListFragment
import tw.com.deathhit.feature.event_list.EventListFragment
import tw.com.deathhit.feature.navigation.databinding.FragmentNavigationBinding
import tw.com.deathhit.feature.set_day_night.SetDayNightFragment
import tw.com.deathhit.feature.set_language.SetLanguageFragment

@AndroidEntryPoint
class NavigationFragment : Fragment() {
    var callback: Callback? = null

    private val binding get() = _binding!!
    private var _binding: FragmentNavigationBinding? = null

    private val viewModel: NavigationViewModel by viewModels()

    private val tabLayoutMediator get() = _tabLayoutMediator!!
    private var _tabLayoutMediator: TabLayoutMediator? = null

    private val setDayNightFragment get() = childFragmentManager.findFragmentByTag(TAG_SET_DAY_NIGHT) as SetDayNightFragment?
    private val setLanguageFragment get() = childFragmentManager.findFragmentByTag(TAG_SET_LANGUAGE) as SetLanguageFragment?

    private val onClickMenuItemListener = Toolbar.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_setLanguage -> {
                viewModel.setLanguage()

                true
            }

            R.id.action_setDayNight -> {
                viewModel.setDayNight()

                true
            }

            else -> false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        configureFragmentCallbacks()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentNavigationBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.toolbar) {
            title = getString(context.applicationInfo.labelRes)
        }

        with(binding.viewPager) {
            adapter = createNavigationTabAdapter()
        }

        configureEdgeToEdge()
        configureTabLayoutAndViewPager()

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
        tabLayoutMediator.detach()

        binding.viewPager.adapter = null

        _binding = null
        _tabLayoutMediator = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    is NavigationViewModel.State.Action.GoToAttractionDetailScreen -> callback?.onGoToAttractionDetailScreen(
                                        attractionId = action.attractionId
                                    )

                                    is NavigationViewModel.State.Action.GoToEventWebsite -> callback?.onGoToEventWebsite(
                                        startUrl = action.startUrl,
                                        title = action.title
                                    )

                                    NavigationViewModel.State.Action.SetDayNight -> setDayNightFragment
                                        ?: run {
                                            SetDayNightFragment.create()
                                                .show(childFragmentManager, TAG_SET_DAY_NIGHT)
                                        }

                                    NavigationViewModel.State.Action.SetLanguage -> setLanguageFragment
                                        ?: run {
                                            SetLanguageFragment.create()
                                                .show(childFragmentManager, TAG_SET_LANGUAGE)
                                        }
                                }

                                viewModel.onAction(action)
                            }
                        }
                }
            }
        }
    }

    private fun configureEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { root, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )

            root.updatePadding(
                left = insets.left,
                right = insets.right,
                top = insets.top
            )

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun configureFragmentCallbacks() {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is AttractionListFragment -> fragment.callback =
                    object : AttractionListFragment.Callback {
                        override fun onGoToAttractionDetailScreen(attractionId: String) {
                            viewModel.goToAttractionDetailScreen(attractionId = attractionId)
                        }
                    }

                is EventListFragment -> fragment.callback = object : EventListFragment.Callback {
                    override fun onGoToEventWebsite(startUrl: String, title: String) {
                        viewModel.goToEventWebsite(startUrl = startUrl, title = title)
                    }

                }
            }
        }
    }

    private fun configureTabLayoutAndViewPager() {
        with(binding) {
            _tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    POS_ATTRACTION_LIST -> {
                        tab.text = getString(R.string.navigation_tab_attraction_list)
                    }

                    POS_EVENT_LIST -> {
                        tab.text = getString(R.string.navigation_tab_event_list)
                    }
                }
            }.apply {
                attach()
            }
        }
    }

    private fun createNavigationTabAdapter() = NavigationTabAdapter(childFragmentManager, lifecycle)

    companion object {
        private const val TAG = "NavigationFragment"
        private const val TAG_SET_DAY_NIGHT = "$TAG.SET_DAY_NIGHT"
        private const val TAG_SET_LANGUAGE = "$TAG.SET_LANGUAGE"

        fun create() = NavigationFragment()
    }

    interface Callback {
        fun onGoToAttractionDetailScreen(attractionId: String)
        fun onGoToEventWebsite(startUrl: String, title: String)
    }
}