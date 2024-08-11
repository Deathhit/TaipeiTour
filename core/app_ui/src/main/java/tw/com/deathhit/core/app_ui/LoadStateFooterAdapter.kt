package tw.com.deathhit.core.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import tw.com.deathhit.core.app_ui.databinding.ItemLoadStateFooterBinding
import tw.com.deathhit.core.app_ui.view_holder.LoadStateFooterViewHolder

class LoadStateFooterAdapter : LoadStateAdapter<LoadStateFooterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateFooterViewHolder =
        LoadStateFooterViewHolder(
            ItemLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        with(holder.binding.progressBar) {
            isVisible = loadState is LoadState.Loading
        }
    }
}