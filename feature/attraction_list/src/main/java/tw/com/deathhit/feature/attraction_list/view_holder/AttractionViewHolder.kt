package tw.com.deathhit.feature.attraction_list.view_holder

import androidx.recyclerview.widget.RecyclerView
import tw.com.deathhit.domain.model.AttractionDO
import tw.com.deathhit.feature.attraction_list.databinding.ItemAttractionBinding

class AttractionViewHolder(val binding: ItemAttractionBinding, var item: AttractionDO? = null) :
    RecyclerView.ViewHolder(binding.root)