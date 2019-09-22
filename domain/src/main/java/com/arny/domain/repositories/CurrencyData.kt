package com.arny.domain.repositories

import com.arny.domain.models.Currency
import com.arny.domain.models.CurrencyConvertion
import io.reactivex.Observable

interface CurrencyData {
    fun getAllCurrencies(): Observable<ArrayList<Currency>>
    fun convert(from: String?, to: String?): Observable<CurrencyConvertion>
}