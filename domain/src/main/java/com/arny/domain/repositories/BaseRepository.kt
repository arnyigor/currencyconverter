package com.arny.domain.repositories

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

interface BaseRepository {
    fun getContext(): Context
    fun getPrefString(key: String, default: String? = null): String?
    fun getPrefInt(key: String, default: Int = 0): Int
    fun getPrefLong(key: String, default: Long = 0L): Long
    fun setPrefBoolean(key: String, value: Boolean)
    fun setPref(key: String, value: Any)
    fun setPrefInt(key: String, value: Int)
    fun setPrefLong(key: String, value: Long)
    fun getPrefBoolean(key: String, default: Boolean): Boolean
    fun removePref(vararg key: String)
    fun setPrefString(key: String?, value: String?)
    fun isConnected(): Boolean
    fun getString(@StringRes res: Int): String
    fun getColor(@ColorRes id: Int): Int
    fun getDrawable(@DrawableRes id: Int): Drawable?
}