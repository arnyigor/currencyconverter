package com.arny.currencyconvertor.view

import android.content.Context
import com.arny.domain.models.Currency
import com.arny.helpers.utils.AbstractArrayAdapter

class CurrencyAdapter(context: Context) : AbstractArrayAdapter<Currency>(context, android.R.layout.simple_spinner_dropdown_item) {
    override fun getItemTitle(item: Currency?): String {
        return item?.code ?: item?.name ?: "No code"
    }
}