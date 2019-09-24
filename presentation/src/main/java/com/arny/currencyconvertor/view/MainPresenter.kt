package com.arny.currencyconvertor.view

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arny.currencyconvertor.MainApp
import com.arny.domain.models.Currency
import com.arny.helpers.utils.CompositeDisposableComponent
import com.arny.helpers.utils.fromCallable
import com.arny.helpers.utils.parseDouble
import io.reactivex.disposables.CompositeDisposable

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), CompositeDisposableComponent {
    private var stateUI: Int = 0
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

    fun initUIState() {
        fromCallable { mainInteractor.getUIState() }
                .observeSubscribeAdd({
                    this.stateUI = it
                    invalidateUI()
                })
    }

    private fun invalidateUI() {
        when (stateUI) {
            0 -> {
                viewState?.showButton(true)
                viewState?.showEditConvertTo(false)
            }
            1 -> {
                viewState?.showButton(false)
                viewState?.showEditConvertTo(false)
                viewState?.initEditFromTextObservable()
            }
            2 -> {
                viewState?.showButton(false)
                viewState?.initEditFromTextObservable()
                viewState?.initEditToTextObservable()
                viewState?.showEditConvertTo(true)
            }
        }
        viewState?.invalidateMenu(stateUI)
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

    fun convert(fromCurrency: Currency?, toCurrency: Currency?, fromValue: String?, toValue: String?, direction: Int) {
        if (!mainInteractor.isInternetAvalable()) {
            viewState?.showAlertSnack("Нет интеренет соединения")
            return
        }
        when (stateUI) {
            0,1 -> {
                if (fromValue.isNullOrBlank()) {
                    viewState?.showResult("")
                    return
                }
                val from = fromValue.parseDouble()
                if (from == null) {
                    viewState?.showAlertSnack("Неверное значение")
                    return
                }
                if (fromCurrency != null && toCurrency != null) {
                    val codeFrom = fromCurrency.code
                    val codeTo = toCurrency.code
                    viewState?.blockUI()
                    viewState?.showProgressButtonConvert()
                    mainInteractor.convert(fromCurrency, toCurrency, from)
                            .map { "$from $codeFrom-> $it $codeTo" }
                            .observeSubscribeAdd({
                                viewState?.hideProgressButtonConvert()
                                viewState?.unblockUI()
                                viewState?.showResult(it)
                            })
                } else {
                    viewState?.showAlertSnack("Данные о выбранных валютах отсутствуют")
                }
            }
            2 -> {
                when (direction) {
                    0 -> {
                        if (fromValue.isNullOrBlank()) {
                            viewState?.setToValue("")
                            return
                        }
                        val from = fromValue.parseDouble()
                        if (from == null) {
                            viewState?.showAlertSnack("Неверное значение")
                            return
                        }

                        if (fromCurrency != null && toCurrency != null) {
                            viewState?.blockUI()
                            mainInteractor.convert(fromCurrency, toCurrency, from)
                                    .observeSubscribeAdd({
                                        viewState?.unblockUI()
                                        viewState?.setToValue(it.toString())
                                    })
                        } else {
                            viewState?.showAlertSnack("Данные о выбранных валютах отсутствуют")
                        }
                    }
                    1 -> {
                        if (toValue.isNullOrBlank()) {
                            viewState?.setFromValue("")
                            return
                        }
                        val from = toValue.parseDouble()
                        if (from == null) {
                            viewState?.showAlertSnack("Неверное значение")
                            return
                        }
                        if (fromCurrency != null && toCurrency != null) {
                            viewState?.blockUI()
                            mainInteractor.convert(toCurrency, fromCurrency, from)
                                    .observeSubscribeAdd({
                                        viewState?.unblockUI()
                                        viewState?.setFromValue(it.toString())
                                    })
                        } else {
                            viewState?.showAlertSnack("Данные о выбранных валютах отсутствуют")
                        }
                    }
                }
            }
        }
    }

    fun loadCurrenciesToAdapters() {
        if (currencies.isEmpty()) {
            loadCurrencies()
        } else {
            viewState?.updateAdapters(currencies)
        }
    }

    fun changeUIState(i: Int) {
        mainInteractor.changeUIState(i)
        initUIState()
    }


}