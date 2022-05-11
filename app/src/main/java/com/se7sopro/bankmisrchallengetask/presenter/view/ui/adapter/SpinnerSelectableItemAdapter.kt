/*
 *
 *  * Created by Biro 
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 9/30/20 10:23 AM
 *
 */

package com.se7sopro.bankmisrchallengetask.presenter.view.ui.adapter

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.StringRes
import com.se7sopro.bankmisrchallengetask.R
import com.se7sopro.bankmisrchallengetask.databinding.SpinnerItemBinding
import com.se7sopro.bankmisrchallengetask.databinding.SpinnerSelectableItemHintBinding
import com.se7sopro.bankmisrchallengetask.utils.listeners.SelectableItem


class SpinnerSelectableItemAdapter(
    context: Context, list: MutableList<SelectableItem>, @StringRes private val hintString: Int
) : ArrayAdapter<SelectableItem>(context, 0, list) {

    private val layoutInflater = LayoutInflater.from(context)

    companion object {
        const val HINT = 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        when (position) {
            HINT -> {
                val view = convertView ?: layoutInflater.inflate(
                    R.layout.spinner_selectable_item_hint,
                    parent,
                    false
                )
                with(SpinnerSelectableItemHintBinding.bind(view)) {
                    hint.text = context.getString(hintString)
                }
                return view
            }
            else -> {
                val view = convertView ?: layoutInflater.inflate(
                    R.layout.spinner_item,
                    parent,
                    false
                )
                getItem(position)?.let { item ->
                    bindItem(view, item)
                }
                return view
            }

        }

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        when (position) {
            HINT -> {
                view = layoutInflater.inflate(R.layout.spinner_selectable_item_hint, parent, false)
                view.setOnClickListener {
                    val root = parent.rootView
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                }
                with(SpinnerSelectableItemHintBinding.bind(view)) {
                    hint.text = context.getString(hintString)
                }
            }
            else -> {
                view = layoutInflater.inflate(R.layout.spinner_item, parent, false)
                getItem(position)?.let { item ->
                    bindItem(view, item)
                }
            }
        }
        return view
    }

    private fun bindItem(view: View, item: SelectableItem) {
        /*with(SpinnerItemBinding.bind(view)) {
            tvSpinner.text = item.name
        }*/
        val textViewAddressLine = view.findViewById<TextView>(R.id.tv_spinner)
        textViewAddressLine?.text = item.name
    }

    override fun getItem(position: Int): SelectableItem? =
        if (position == 0) null else super.getItem(position - 1)

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

}