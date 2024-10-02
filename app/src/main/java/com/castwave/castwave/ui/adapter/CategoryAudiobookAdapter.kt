package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.CategoryBookListener
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.databinding.ItemCategoryBinding
import com.castwave.castwave.utils.extension.clickWithDebounce


class CategoryAudiobookAdapter : BaseAdapter<ItemCategoryBinding, CategoryAudiobook>() {
    private var event : ((type : Int , data : Any?) ->Unit)? = null
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemCategoryBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvNameCategory.text = item.name
            itemView.clickWithDebounce {
                listner.categoryClicked()
                event?.invoke(PodcastAdapter.TYPE_CATEGORY , item)
            }
        }
    }
    private lateinit var listner: CategoryBookListener
    fun setListener(addListener: CategoryBookListener) {
        this.listner = addListener
    }
    fun setEvent(function : (type: Int, data: Any?) -> Unit) {
        event = function
    }
}
