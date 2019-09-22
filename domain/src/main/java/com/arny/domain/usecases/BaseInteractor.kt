package com.arny.domain.usecases

import android.graphics.drawable.Drawable
import com.arny.domain.repositories.BaseRepository

class BaseInteractor (private val repository: BaseRepository) {

    fun getString(res: Int): String {
        return repository.getString(res)
    }

    fun getColor(id: Int): Int {
        return repository.getColor(id)
    }

    fun getDrawable(id: Int): Drawable? {
        return repository.getDrawable(id)
    }
}