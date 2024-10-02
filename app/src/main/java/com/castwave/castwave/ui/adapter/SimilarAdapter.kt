package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.databinding.ItemBookGroupBinding


class SimilarAdapter : BaseAdapter<ItemBookGroupBinding, HashMap<String, List<Book>>>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemBookGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemBookGroupBinding>, position: Int) {
        with(holder) {
        }
    }
}
