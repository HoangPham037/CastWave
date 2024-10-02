package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.PodCourseItemClickListener
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Mentor
import com.castwave.castwave.databinding.LayoutMentorRecommendBinding
import com.castwave.castwave.utils.extension.clickWithDebounce

class MentorRecommendAdapter : BaseAdapter<LayoutMentorRecommendBinding, Mentor>() {

    private lateinit var listner: PodCourseItemClickListener
    fun setListener(addListener: PodCourseItemClickListener) {
        this.listner = addListener
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            LayoutMentorRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<LayoutMentorRecommendBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder) {
            binding.tvNameMentor.text = item.name
            binding.tvDescMentor.text = item.desc
            binding.imgAvatar.setImageResource(item.image)
            binding.tvPlay.clickWithDebounce {
                listner.onGoVideoInfoMentor()
            }
        }
    }

}