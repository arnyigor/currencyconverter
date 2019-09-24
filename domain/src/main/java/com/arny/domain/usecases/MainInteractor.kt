package com.arny.domain.usecases

import com.arny.domain.Constants
import com.arny.domain.models.Currency
import com.arny.domain.repositories.BaseRepository
import com.arny.domain.repositories.CurrencyData
import com.arny.domain.repositories.CurrencyDataSource
import com.arny.helpers.utils.MathUtils
import com.arny.helpers.utils.toJson
import io.reactivex.Observable

class MainInteractor(
        private val remoteData: CurrencyData,
        private val baseRepository: BaseRepository,
        private val localData: CurrencyDataSource
) {

    fun isInternetAvalable(): Boolean {
        return baseRepository.isConnected()
    }

    fun getUIState(): Int {
        return baseRepository.getPrefInt(Constants.PREFS.UI_STATE)
    }

    fun getCurrencies(): Observable<ArrayList<Currency>> {
        val remoteCurrencies = remoteData.getAllCurrencies()
        if (baseRepository.isConnected()) {
            return remoteCurrencies.doOnNext {
                localData.saveCurencies(it.toJson())
            }
        }
        return localData.getAllCurrencies()
    }

    fun convert(fromCurrency: Currency, toCurrency: Currency, from: Double): Observable<Double> {
        val codeFrom = fromCurrency.code
        val codeTo = toCurrency.code
        return remoteData.convert(codeFrom, codeTo)
                .map { MathUtils.round(from * (it.coefficient ?: 0.0), 2) }
    }

    fun changeUIState(i: Int) {
        baseRepository.setPrefInt(Constants.PREFS.UI_STATE,i)
    }

}