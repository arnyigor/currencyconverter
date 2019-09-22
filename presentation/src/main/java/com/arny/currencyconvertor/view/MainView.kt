package com.arny.currencyconvertor.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arny.domain.models.Currency
import java.util.*

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainView:MvpView {
    fun updateAdapters(list: ArrayList<Currency>)
    fun showAlertSnack(s: String)
    fun showResult(res: String?)
}