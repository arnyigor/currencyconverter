package com.arny.currencyconvertor.view

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arny.currencyconvertor.MainApp
import com.arny.domain.models.Currency
import com.arny.helpers.utils.CompositeDisposableComponent
import com.arny.helpers.utils.parseDouble
import io.reactivex.disposables.CompositeDisposable

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), CompositeDisposableComponent {
    private var currencies: ArrayList<Currency> = arrayListOf()
    override val compositeDisposable = CompositeDisposable()
    private var mainInteractor = MainApp.di.mainInteractor

    override fun detachView(view: MainView?) {
        super.detachView(view)
        resetCompositeDisposable()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadCurrencies()
    }

    private fun loadCurrencies() {
        mainInteractor.getCurrencies()
                .map { list ->
                    ArrayList(list.sortedBy {
                        it.code
                    })
                }
                .observeSubscribeAdd({
                    this.currencies = it
                    viewState?.updateAdapters(it)
                })
    }

    fun convert(fromCurrency: Currency?, toCurrency: Currency?, fromValue: String? ) {
        if (!mainInteractor.isInternetAvalable()) {
            viewState?.showAlertSnack("Нет интеренет соединения")
        }
        if (fromValue.isNullOrBlank()) {
            viewState?.showAlertSnack("Введите первое значение")
            return
        }
        val from = fromValue.parseDouble()
        if (from==null) {
            viewState?.showAlertSnack("Неверное значение")
            return
        }
        if (fromCurrency!=null && toCurrency!=null) {
            val codeFrom = fromCurrency.code
            val codeTo = toCurrency.code
            mainInteractor.convert(fromCurrency, toCurrency,from)
                    .map { "$from $codeFrom-> $it $codeTo" }
                    .observeSubscribeAdd({
                        viewState?.showResult(it)
                    })
        }else{
            viewState?.showAlertSnack("Данные о выбранных валютах отсутствуют")
        }
    }

    fun loadCurrenciesToAdapters() {
        if (currencies.isEmpty()) {
            loadCurrencies()
        }else{
            viewState?.updateAdapters(currencies)
        }
    }

}