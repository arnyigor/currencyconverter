package com.arny.currencyconvertor.view

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arny.currencyconvertor.R
import com.arny.domain.models.Currency
import com.arny.helpers.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : MvpAppCompatActivity(), MainView {
    private var currencyFromAdapter: CurrencyAdapter? = null
    private var currencyToAdapter: CurrencyAdapter? = null
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdapter()
        btn_convert.setOnClickListener {
            val fromCurrency = currencyFromAdapter?.getItem(spin_convert_from.selectedItemPosition)
            val toCurrency = currencyToAdapter?.getItem(spin_convert_to.selectedItemPosition)
            val fromValue = edt_convert_from_value.text.toString()
            mainPresenter.convert(fromCurrency, toCurrency, fromValue)
        }
        mainPresenter.loadCurrenciesToAdapters()
    }

    private fun initAdapter() {
        currencyFromAdapter = CurrencyAdapter(this)
        currencyToAdapter = CurrencyAdapter(this)
        spin_convert_from.adapter = currencyFromAdapter
        spin_convert_to.adapter = currencyToAdapter
    }

    override fun updateAdapters(list: ArrayList<Currency>) {
        currencyFromAdapter?.addAll(list)
        currencyToAdapter?.addAll(list)
    }

    override fun showAlertSnack(s: String) {
        btn_convert?.showSnackBar(s, 3000)
    }

    override fun showResult(res: String?) {
        tv_result.text = res
    }
}
