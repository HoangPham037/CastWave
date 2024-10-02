package com.castwave.castwave.base.adapter

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder

abstract class BaseAdapter<V : ViewBinding, O : Any> :
    RecyclerView.Adapter<BaseViewHolder<ViewBinding>>() {

    var items: MutableList<O> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isReverse: Boolean = false

    var isClearWhenLoading = true

    var type: StatusAdapter = StatusAdapter.None()
        private set

    private var refreshLayout: SwipeRefreshLayout? = null
    fun attachWithRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        this.refreshLayout = refreshLayout
    }

    override fun getItemViewType(position: Int): Int {
        return if (isReverse) {
            if (position == 0) type.itemType else getItemViewTypeZ(position - 1)
        } else {
            if (position == itemCountZ) type.itemType else getItemViewTypeZ(position)
        }
    }

    @IntRange(from = 0, to = StatusAdapter.TYPE_TO_ITEM.toLong())
    open fun getItemViewTypeZ(position: Int): Int {
        return 0
    }

    open fun getSpanSize(pos: Int, rowStatus: Int, rowNormal: (viewType: Int) -> Int = { 1 }): Int {
        return when (getItemViewType(pos)) {
            in StatusAdapter.RANGE_TYPE -> rowStatus
            else -> rowNormal(getItemViewType(pos))
        }
    }

    override fun getItemCount(): Int {
        return itemCountZ
    }

    open val itemCountZ: Int get() = items.size

    open fun layoutLoadingVH(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding>? {
        return null
    }

    open fun layoutLoadMoreVH(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding>? {
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return onCreateViewHolderZ(parent, viewType)
    }

    abstract fun onCreateViewHolderZ(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding>

    abstract fun onBindViewHolderZ(holder: BaseViewHolder<V>, position: Int)

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        when (getItemViewType(position)) {
            in 0..StatusAdapter.TYPE_TO_ITEM -> {
                onBindViewHolderZ(
                    holder as BaseViewHolder<V>,
                    getCurrentPositionZ(position)
                )
            }
        }
    }


    fun getCurrentPositionZ(position: Int): Int = if (isReverse) position - 1 else position

    fun getPositionFromHolderZ(position: Int): Int = if (isReverse) position + 1 else position

    fun notifyItemRemovedZ(position: Int) =
        notifyItemRemoved(getPositionFromHolderZ(position))

    fun notifyItemInsertedZ(position: Int) =
        notifyItemInserted(getPositionFromHolderZ(position))

    fun notifyItemRangeInsertedZ(positionStart: Int, positionEnd: Int) = notifyItemRangeInserted(
        getPositionFromHolderZ(positionStart),
        getPositionFromHolderZ(positionEnd),
    )

    fun notifyItemRangeRemovedZ(positionStart: Int, positionEnd: Int) = notifyItemRangeRemoved(
        getPositionFromHolderZ(positionStart),
        getPositionFromHolderZ(positionEnd),
    )

    fun notifyItemRangeChangedZ(positionStart: Int, positionEnd: Int) = notifyItemRangeChanged(
        if (isReverse) positionStart + 1 else positionStart,
        if (isReverse) positionEnd + 1 else positionEnd,
    )

    fun notifyItemChangedZ(position: Int) =
        notifyItemChanged(if (isReverse) position + 1 else position)

    fun notifyItemMovedZ(positionStart: Int, positionEnd: Int) = notifyItemMoved(
        if (isReverse) positionStart + 1 else positionStart,
        if (isReverse) positionEnd + 1 else positionEnd,
    )
}