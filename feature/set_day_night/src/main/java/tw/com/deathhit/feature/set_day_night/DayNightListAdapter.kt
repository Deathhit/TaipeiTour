package tw.com.deathhit.feature.set_day_night

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tw.com.deathhit.feature.set_day_night.databinding.ItemDayNightBinding
import tw.com.deathhit.feature.set_day_night.model.DayNightItem
import tw.com.deathhit.feature.set_day_night.view_holder.DayNightViewHolder

class DayNightListAdapter(
    private val onClickItemListener: (item: DayNightItem) -> Unit
) : ListAdapter<DayNightItem, DayNightViewHolder>(comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayNightViewHolder =
        DayNightViewHolder(
            ItemDayNightBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                item?.let(onClickItemListener)
            }
        }

    override fun onBindViewHolder(holder: DayNightViewHolder, position: Int) {
        holder.item = getItem(position)

        holder.item?.let { item ->
            bindIsSelected(holder, item)
            bindDayNightName(holder, item)
        }
    }

    private fun bindIsSelected(holder: DayNightViewHolder, item: DayNightItem) {
        holder.binding.cardView.isChecked = item.isSelected
    }

    private fun bindDayNightName(holder: DayNightViewHolder, item: DayNightItem) {
        holder.binding.textViewName.text = item.mode.name
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<DayNightItem>() {
            override fun areItemsTheSame(oldItem: DayNightItem, newItem: DayNightItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: DayNightItem, newItem: DayNightItem): Boolean =
                oldItem == newItem
        }
    }
}