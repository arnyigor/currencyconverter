package com.arny.helpers.interfaces

import android.view.View
import android.widget.AdapterView

interface _OnItemSelected: AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}