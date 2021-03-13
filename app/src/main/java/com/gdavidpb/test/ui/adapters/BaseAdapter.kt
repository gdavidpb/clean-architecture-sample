package com.gdavidpb.test.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gdavidpb.test.ui.viewholders.BaseViewHolder
import java.util.Collections.synchronizedList

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected val currentList: MutableList<T> = synchronizedList(mutableListOf<T>())

    abstract fun provideComparator(): Comparator<T>

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = currentList[position]

        holder.bindView(item)
    }

    open fun isEmpty(): Boolean {
        return currentList.isEmpty()
    }

    open fun submitList(list: List<T>) {
        val diffUtil = ListDiffUtil(oldList = currentList, newList = list, comparator = provideComparator())
        val diffResult = DiffUtil.calculateDiff(diffUtil, true)

        diffResult.dispatchUpdatesTo(this)

        currentList.clear()
        currentList.addAll(list)
    }

    open fun getItem(position: Int): T {
        return currentList[position]
    }

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

        val comparator = provideComparator()

        return currentList.indexOfFirst { comparator.compare(it, item) == 0 }
    }

    private class ListDiffUtil<Q>(
        private val oldList: List<Q>,
        private val newList: List<Q>,
        private val comparator: Comparator<Q>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return comparator.compare(oldItem, newItem) == 0
        }
    }
}