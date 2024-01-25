package tw.com.deathhit.feature.attraction_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tw.com.deathhit.domain.model.AttractionDO
import tw.com.deathhit.feature.attraction_list.databinding.ItemAttractionBinding
import tw.com.deathhit.feature.attraction_list.view_holder.AttractionViewHolder

class AttractionListAdapter(
    private val glideRequestManager: RequestManager,
    private val onClickItemListener: (item: AttractionDO) -> Unit
) : PagingDataAdapter<AttractionDO, AttractionViewHolder>(comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder =
        AttractionViewHolder(
            ItemAttractionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                item?.let(onClickItemListener)
            }
        }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.item = getItem(position)

        holder.item?.let { item ->
            bindImageUrl(holder, item)
            bindAttractionIntroduction(holder, item)
            bindAttractionName(holder, item)
        }
    }

    override fun onViewRecycled(holder: AttractionViewHolder) {
        super.onViewRecycled(holder)
        recycleImage(holder)
    }

    private fun bindImageUrl(holder: AttractionViewHolder, item: AttractionDO) {
        glideRequestManager.load(item.imageUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imageViewAttraction)
    }

    private fun bindAttractionIntroduction(holder: AttractionViewHolder, item: AttractionDO) {
        holder.binding.textViewIntroduction.text = item.introduction
    }

    private fun bindAttractionName(holder: AttractionViewHolder, item: AttractionDO) {
        holder.binding.textViewName.text = item.name
    }

    private fun recycleImage(holder: AttractionViewHolder) {
        glideRequestManager.clear(holder.binding.imageViewAttraction)
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<AttractionDO>() {
            override fun areItemsTheSame(oldItem: AttractionDO, newItem: AttractionDO): Boolean =
                oldItem.attractionId == newItem.attractionId

            override fun areContentsTheSame(oldItem: AttractionDO, newItem: AttractionDO): Boolean =
                oldItem == newItem
        }
    }
}