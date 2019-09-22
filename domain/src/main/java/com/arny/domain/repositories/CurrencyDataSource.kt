package com.arny.domain.repositories

import com.arny.domain.models.Currency
import io.reactivex.Observable

interface CurrencyDataSource {
    fun getAllCurrencies(): Observable<ArrayList<Currency>>
    fun saveCurencies(json: String?)
}