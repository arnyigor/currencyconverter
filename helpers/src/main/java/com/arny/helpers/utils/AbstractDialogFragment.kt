package com.arny.helpers.utils

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class AbstractDialogFragment : DialogFragment() {
    /**
     * @return ViewGroup.LayoutParams.MATCH_PARENT || ViewGroup.LayoutParams.WRAP_CONTENT
     */
    protected open fun getDialogWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    /**
     * @return ViewGroup.LayoutParams.MATCH_PARENT || ViewGroup.LayoutParams.WRAP_CONTENT
     */
    protected fun getDialogHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    protected fun getDialogStyle(): Int? = null
    protected abstract fun getLayout(): Int
    protected abstract fun viewCreated(view: View, savedInstanceState: Bundle?)

    protected abstract fun getCanceled(): Boolean

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(getCanceled())
            dialog.setCancelable(getCanceled())
            val width = getDialogWidth()
            val height = getDialogHeight()
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = getDialogStyle()
        if (style != null) {
            setStyle(STYLE_NORMAL, style)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        super.onCreateView(inflater, parent, state)
        return activity?.layoutInflater?.inflate(getLayout(), parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated(view, savedInstanceState)
    }
}
