package com.gdavidpb.test.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var _item: T? = null

    fun getItem(): T? = _item

    open fun bindView(item: T) {
        _item = item
    }
}