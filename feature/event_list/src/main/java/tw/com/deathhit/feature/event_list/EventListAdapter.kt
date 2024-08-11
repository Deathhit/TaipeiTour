package tw.com.deathhit.feature.event_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import tw.com.deathhit.domain.model.EventDO
import tw.com.deathhit.feature.event_list.databinding.ItemEventBinding
import tw.com.deathhit.feature.event_list.view_holder.EventViewHolder

class EventListAdapter(
    private val onClickItemListener: (item: EventDO) -> Unit
) : PagingDataAdapter<EventDO, EventViewHolder>(comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                item?.let(onClickItemListener)
            }
        }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.item = getItem(position)

        holder.item?.let { item ->
            bindEventDescription(holder, item)
            bindEventTitle(holder, item)
        }
    }

    private fun bindEventDescription(holder: EventViewHolder, item: EventDO) {
        holder.binding.textViewDescription.text = item.description
    }

    private fun bindEventTitle(holder: EventViewHolder, item: EventDO) {
        holder.binding.textViewTitle.text = item.title
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<EventDO>() {
            override fun areItemsTheSame(oldItem: EventDO, newItem: EventDO): Boolean =
                oldItem.eventId == newItem.eventId

            override fun areContentsTheSame(oldItem: EventDO, newItem: EventDO): Boolean =
                oldItem == newItem
        }
    }
}