package com.castwave.castwave.ui.fragment.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castwave.castwave.R
import com.castwave.castwave.data.model.Topic
import com.castwave.castwave.databinding.ItemTopicBinding
import com.castwave.castwave.utils.Constants

class TopicAdapter(private val listTopic: List<Topic>) :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {
    private var listItem: ArrayList<Int> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemBinding =
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(itemBinding)
    }

    override fun getItemCount() = listTopic.size

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bin(listTopic[position])
    }

    inner class TopicViewHolder(val binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bin(topic: Topic) {
            binding.tvNameTopic.text = topic.name
            binding.ivTopic.setImageResource(topic.imageView)
            itemView.setBackgroundResource(if (listItem.indexOf(topic.id) != -Constants.INDEX_1) R.drawable.bg_intro_true else R.drawable.bgr_item_pod_course)
            itemView.setOnClickListener {
                if (listItem.indexOf(topic.id) == -Constants.INDEX_1) listItem.add(topic.id) else listItem.remove(topic.id)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}