package tw.com.deathhit.feature.attraction_gallery.view_holder

import androidx.recyclerview.widget.RecyclerView
import tw.com.deathhit.domain.model.AttractionImageDO
import tw.com.deathhit.feature.attraction_gallery.databinding.ItemAttractionImageBinding

class AttractionImageViewHolder(
    val binding: ItemAttractionImageBinding,
    var item: AttractionImageDO? = null
) : RecyclerView.ViewHolder(binding.root)