package tw.com.deathhit.feature.event_list.view_holder

import androidx.recyclerview.widget.RecyclerView
import tw.com.deathhit.domain.model.EventDO
import tw.com.deathhit.feature.event_list.databinding.ItemEventBinding

class EventViewHolder(val binding: ItemEventBinding, var item: EventDO? = null) :
    RecyclerView.ViewHolder(binding.root)