package com.arny.currencyconvertor.data.api

import com.google.gson.annotations.SerializedName

class CurrencyResponse {
    @SerializedName("currencyName")
    var name: String? = null
    @SerializedName("currencySymbol")
    var symbol: String? = null
    @SerializedName("id")
    var id: String? = null
}