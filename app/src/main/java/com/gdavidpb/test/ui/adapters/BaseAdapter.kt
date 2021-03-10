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

    override fun getItem(position: Int): T {
        return currentList[position]
    }

    override fun getItemCount(): Int {
        return currentList.size
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
        val position = getItemPosition(item)

        currentList[position] = currentList[position].update()

        notifyItemChanged(position)
    }

    open fun removeItem(item: T) {
        val position = getItemPosition(item)

        currentList.removeAt(position)

        notifyItemRemoved(position)
    }

    open fun getItemPosition(item: T): Int {
        check(hasStableIds()) { "In order to use modifiers by item you have to set up stable ids." }

        return currentList.indexOfFirst { comparator.compare(it, item) == 0 }
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