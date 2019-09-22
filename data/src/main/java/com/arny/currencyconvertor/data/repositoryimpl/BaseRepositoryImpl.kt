package com.arny.currencyconvertor.data.repositoryimpl

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import com.arny.domain.repositories.BaseRepository
import com.arny.helpers.utils.Prefs
import com.arny.helpers.utils.Utility

class BaseRepositoryImpl(private val ctx: Context) : BaseRepository {
    override fun getContext(): Context {
        return ctx
    }

    override fun getPrefString(key: String, default: String?): String? {
        return Prefs.getInstance(getContext()).get(key) ?: default
    }

    override fun getPrefInt(key: String, default: Int): Int {
        return Prefs.getInstance(getContext()).get<Int>(key) ?: default
    }

    override fun getPrefLong(key: String, default: Long): Long {
        return Prefs.getInstance(getContext()).get<Long>(key) ?: default
    }

    override fun setPrefBoolean(key: String, value: Boolean) {
        Prefs.getInstance(getContext()).put(key, value)
    }

    override fun setPref(key: String, value: Any) {
        Prefs.getInstance(getContext()).put(key, value)
    }

    override fun setPrefInt(key: String, value: Int) {
        Prefs.getInstance(getContext()).put(key, value)
    }

    override fun setPrefLong(key: String, value: Long) {
        Prefs.getInstance(getContext()).put(key, value)
    }

    override fun getPrefBoolean(key: String, default: Boolean): Boolean {
        return Prefs.getInstance(getContext()).get<Boolean>(key) ?: false
    }

    override fun removePref(vararg key: String) {
        Prefs.getInstance(getContext()).remove(*key)
    }

    override fun setPrefString(key: String?, value: String?) {
        Prefs.getInstance(getContext()).put(key, value)
    }

    override fun isConnected(): Boolean {
        return Utility.isConnected(getContext())
    }

    override fun getString(@StringRes res: Int): String {
        return getContext().getString(res) ?: ""
    }

    override fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(getContext(), id)
    }

    override fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(getContext(), id)
    }
}