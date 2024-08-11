package tw.com.deathhit.feature.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tw.com.deathhit.feature.attraction_list.AttractionListFragment
import tw.com.deathhit.feature.event_list.EventListFragment

class NavigationTabAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            POS_ATTRACTION_LIST -> AttractionListFragment.create()
            POS_EVENT_LIST -> EventListFragment.create()
            else -> throw RuntimeException("Unexpected position of $position!")
        }

    override fun getItemCount(): Int = 2
}