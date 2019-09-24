package com.arny.currencyconvertor.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arny.domain.models.Currency
import java.util.*

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun updateAdapters(list: ArrayList<Currency>)
    fun showAlertSnack(s: String)
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showResult(res: String?)
    fun showButton(vis: Boolean)
    fun invalidateMenu(it: Int)
    fun showEditConvertTo(vis: Boolean)
    fun hideProgressButtonConvert()
    fun showProgressButtonConvert()
    fun blockUI()
    fun initEditFromTextObservable()
    fun setToValue(value: String)
    fun initEditToTextObservable()
    fun setFromValue(value: String)
    fun unblockUI()
}