package tw.com.deathhit.feature.set_language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tw.com.deathhit.domain.model.LanguageDO
import tw.com.deathhit.feature.set_language.databinding.ItemLanguageBinding
import tw.com.deathhit.feature.set_language.view_holder.LanguageViewHolder

class LanguageListAdapter(
    private val onClickItemListener: (item: LanguageDO) -> Unit
) : ListAdapter<LanguageDO, LanguageViewHolder>(comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder =
        LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                item?.let(onClickItemListener)
            }
        }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.item = getItem(position)

        holder.item?.let { item ->
            bindIsSelected(holder, item)
            bindLanguageName(holder, item)
        }
    }

    private fun bindIsSelected(holder: LanguageViewHolder, item: LanguageDO) {
        holder.binding.cardView.isChecked = item.isSelected
    }

    private fun bindLanguageName(holder: LanguageViewHolder, item: LanguageDO) {
        holder.binding.textViewName.text = item.language.toDisplayText()
    }

    companion object {
        private val comparator = object : DiffUtil.ItemCallback<LanguageDO>() {
            override fun areItemsTheSame(oldItem: LanguageDO, newItem: LanguageDO): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: LanguageDO, newItem: LanguageDO): Boolean =
                oldItem == newItem
        }
    }
}