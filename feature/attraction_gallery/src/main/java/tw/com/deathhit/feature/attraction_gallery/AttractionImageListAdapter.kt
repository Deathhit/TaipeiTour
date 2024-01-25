package tw.com.deathhit.feature.attraction_gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tw.com.deathhit.domain.model.AttractionImageDO
import tw.com.deathhit.feature.attraction_gallery.databinding.ItemAttractionImageBinding
import tw.com.deathhit.feature.attraction_gallery.view_holder.AttractionImageViewHolder

class AttractionImageListAdapter(
    private val glideRequestManager: RequestManager,
    private val onClickItemListener: (item: AttractionImageDO) -> Unit
) : ListAdapter<AttractionImageDO, AttractionImageViewHolder>(comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionImageViewHolder =
        AttractionImageViewHolder(
            ItemAttractionImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                item?.let(onClickItemListener)
            }
        }

    override fun onBindViewHolder(holder: AttractionImageViewHolder, position: Int) {
        holder.item = getItem(position)

        holder.item?.let { item ->
            bindImageUrl(holder, item)
        }
    }

    override fun onViewRecycled(holder: AttractionImageViewHolder) {
        super.onViewRecycled(holder)
        recycleImage(holder)
    }

    private fun bindImageUrl(holder: AttractionImageViewHolder, item: AttractionImageDO) {
        glideRequestManager.load(item.imageUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imageView)
    }

    private fun recycleImage(holder: AttractionImageViewHolder) {
        glideRequestManager.clear(holder.binding.imageView)
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<AttractionImageDO>() {
            override fun areItemsTheSame(
                oldItem: AttractionImageDO,
                newItem: AttractionImageDO
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AttractionImageDO,
                newItem: AttractionImageDO
            ): Boolean =
                oldItem == newItem
        }
    }
}