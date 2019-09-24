package com.arny.currencyconvertor.view

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arny.currencyconvertor.R
import com.arny.domain.models.Currency
import com.arny.helpers.interfaces._OnItemSelected
import com.arny.helpers.utils.*
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : MvpAppCompatActivity(), MainView, CompositeDisposableComponent {
    override val compositeDisposable = CompositeDisposable()
    private var userIsInteracting: Boolean = false
    private var currencyFromAdapter: CurrencyAdapter? = null
    private var currencyToAdapter: CurrencyAdapter? = null
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter
    private var uiState = 0

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdapter()
        btn_convert.setOnClickListener {
            convert(0)
        }
        bindProgressButton(btn_convert)
        btn_convert.attachTextChangeAnimator()
        mainPresenter.loadCurrenciesToAdapters()
        mainPresenter.initUIState()
    }

    override fun initEditFromTextObservable() {
        observeEditText(edt_convert_from_value)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOnMain()
                .switchMap { fromCallable { it } }
                .subscribe({
                    convert(0)
                }, {
                    it.printStackTrace()
                })
                .addTo(compositeDisposable)
    }

    override fun initEditToTextObservable() {
        observeEditText(edt_convert_to_value)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOnMain()
                .switchMap { fromCallable { it } }
                .subscribe({
                    convert(1)
                }, {
                    it.printStackTrace()
                })
                .addTo(compositeDisposable)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (uiState) {
            0 -> {
                menu?.findItem(R.id.action_state_1)?.isChecked = true
            }
            1 -> {
                menu?.findItem(R.id.action_state_2)?.isChecked = true
            }
            2 -> {
                menu?.findItem(R.id.action_state_3)?.isChecked = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_state_1 -> {
                mainPresenter.changeUIState(0)
                true
            }
            R.id.action_state_2 -> {
                mainPresenter.changeUIState(1)
                true
            }
            R.id.action_state_3 -> {
                mainPresenter.changeUIState(2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun invalidateMenu(it: Int) {
        uiState = it
        invalidateOptionsMenu()
    }

    private fun convert(direction: Int) {
        val fromCurrency = currencyFromAdapter?.getItem(spin_convert_from.selectedItemPosition)
        val toCurrency = currencyToAdapter?.getItem(spin_convert_to.selectedItemPosition)
        val fromValue = edt_convert_from_value.text.toString()
        val toValue = edt_convert_to_value.text.toString()
        mainPresenter.convert(fromCurrency, toCurrency, fromValue,toValue,direction)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIsInteracting = true
    }

    private fun initAdapter() {
        currencyFromAdapter = CurrencyAdapter(this)
        currencyToAdapter = CurrencyAdapter(this)
        spin_convert_from.adapter = currencyFromAdapter
        spin_convert_to.adapter = currencyToAdapter
        spin_convert_from.onItemSelectedListener = object : _OnItemSelected {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (userIsInteracting) {
                    convert(0)
                }
            }
        }
        spin_convert_to.onItemSelectedListener = object : _OnItemSelected {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (userIsInteracting) {
                    convert(0)
                }
            }
        }
    }

    override fun showEditConvertTo(vis: Boolean) {
        edt_convert_to_value.setVisible(vis)
    }

    override fun updateAdapters(list: ArrayList<Currency>) {
        currencyFromAdapter?.addAll(list)
        currencyToAdapter?.addAll(list)
    }

    override fun showAlertSnack(s: String) {
        root_view.showSnackBar(s, 3000)
    }

    override fun blockUI() {
        spin_convert_from.isEnabled = false
        spin_convert_to.isEnabled = false
    }

    override fun setFromValue(value: String) {
        edt_convert_from_value.setText(value)
    }

    override fun showProgressButtonConvert() {
        btn_convert.showProgress {
            buttonTextRes = null
            progressColor = Color.BLACK
        }
    }

    override fun hideProgressButtonConvert() {
        btn_convert.hideProgress(getString(R.string.convert))
    }

    override fun showResult(res: String?) {
        tv_result.text = res
    }

    override fun unblockUI() {
        spin_convert_from.isEnabled = false
        spin_convert_to.isEnabled = false
    }

    override fun showButton(vis: Boolean) {
        btn_convert.setVisible(vis)
    }

    override fun setToValue(value: String) {
        edt_convert_to_value.setText(value)
    }
}
