package com.arny.currencyconvertor.data.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("/api/v7/currencies")
    fun getAllCurrencies(@QueryMap params: Map<String, String>): Observable<ResponseBody>

    @GET("/api/v7/convert")
    fun apiConvert(@QueryMap params: Map<String, String?>): Observable<ResponseBody>
}
