package com.gdavidpb.test.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gdavidpb.test.ui.viewholders.BaseViewHolder

abstract class BaseAdapter<T : Any>(
    private val comparator: Comparator<T>
) : ListAdapter<T, BaseViewHolder<T>>(DiffCallback(comparator)) {

    private val currentList = mutableListOf<T>()

    override fun getCurrentList(): MutableList<T> {
        return currentList
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = getItem(position)

        return holder.bindView(item)
    }

    override fun submitList(list: List<T>?) {
        currentList.clear()

        if (list != null) currentList.addAll(list)

        super.submitList(list)
    }

    open fun isEmpty() = currentList.isEmpty()

    open fun updateItem(item: T, update: T.() -> T) {
        val position = currentList.indexOfFirst { comparator.compare(it, item) == 0 }

        currentList[position] = currentList[position].update()

        super.submitList(currentList)
    }

    open fun removeItem(item: T) {
        currentList.remove(item)

        super.submitList(currentList)
    }

    class DiffCallback<Q>(
        private val comparator: Comparator<Q>
    ) : DiffUtil.ItemCallback<Q>() {
        override fun areItemsTheSame(oldItem: Q, newItem: Q): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Q, newItem: Q): Boolean {
            return comparator.compare(oldItem, newItem) == 0
        }
    }
}