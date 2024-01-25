package tw.com.deathhit.feature.set_language.view_holder

import androidx.recyclerview.widget.RecyclerView
import tw.com.deathhit.domain.model.LanguageDO
import tw.com.deathhit.feature.set_language.databinding.ItemLanguageBinding

class LanguageViewHolder(val binding: ItemLanguageBinding, var item: LanguageDO? = null) :
    RecyclerView.ViewHolder(binding.root)