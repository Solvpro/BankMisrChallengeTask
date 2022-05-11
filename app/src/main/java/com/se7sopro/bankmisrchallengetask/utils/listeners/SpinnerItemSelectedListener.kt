/*
 *
 *  * Created by Biro
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 9/24/20 10:45 AM
 *
 */

package com.se7sopro.bankmisrchallengetask.utils.listeners

import android.view.View
import android.widget.AdapterView

class SpinnerItemSelected(private val spinnerItemSelected: SpinnerItemSelectedListener) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerItemSelected.onItemSelectedListener(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

interface SpinnerItemSelectedListener {
    fun onItemSelectedListener(position: Int)
}