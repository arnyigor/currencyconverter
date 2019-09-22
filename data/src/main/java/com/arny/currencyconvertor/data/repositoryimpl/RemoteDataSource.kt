package com.arny.currencyconvertor.data.repositoryimpl

import com.arny.currencyconvertor.data.api.ApiService
import com.arny.domain.Constants
import com.arny.domain.models.Currency
import com.arny.domain.models.CurrencyConvertion
import com.arny.domain.repositories.CurrencyData
import com.arny.sentry.data.api.ApiProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import io.reactivex.Observable
import okhttp3.logging.HttpLoggingInterceptor

class RemoteDataSource : CurrencyData {

    override fun getAllCurrencies(): Observable<ArrayList<Currency>> {
        val params = hashMapOf("apiKey" to Constants.API.API_KEY)
        return ApiProvider.provideApi(
                Constants.API.BASE_URL,
                ApiService::class.java,
                httpLevel = HttpLoggingInterceptor.Level.HEADERS
        )
                .getAllCurrencies(params)
                .map {
                    val toString = it.string()
                    val gson = GsonBuilder()
                            .registerTypeAdapter(ArrayList::class.java, JsonDeserializer { json, _, _ ->
                                val list = arrayListOf<Currency>()
                                if (json.isJsonObject) {
                                    val asJsonObject = json.asJsonObject
                                    val resultJson = asJsonObject.get("results")
                                    if (resultJson.isJsonObject) {
                                        val jsonObject = resultJson.asJsonObject
                                        for (mutableEntry in jsonObject.entrySet()) {
                                            val element = mutableEntry.value
                                            if (element.isJsonObject) {
                                                val jsonElement = element.asJsonObject
                                                val name = jsonElement.get("currencyName")?.asString
                                                val symbol = jsonElement.get("currencySymbol")?.asString
                                                val id = jsonElement.get("id")?.asString
                                                list.add(Currency(id, name, symbol))
                                            }
                                        }
                                    }
                                }
                                return@JsonDeserializer list
                            }).create()
                    val arrayList = gson.fromJson<ArrayList<Currency>>(toString, ArrayList::class.java)
                    arrayList
                }
    }

    override fun convert(from: String?, to: String?): Observable<CurrencyConvertion> {
        val query = "${from}_${to}"
        val params = hashMapOf(
                "apiKey" to Constants.API.API_KEY,
                "q" to query,
                "compact" to "ultra"
        )
        return ApiProvider.provideApi(
                Constants.API.BASE_URL,
                ApiService::class.java,
                httpLevel = HttpLoggingInterceptor.Level.HEADERS
        )
                .apiConvert(params)
                .map {
                    val response = it.string()
                    val gson = GsonBuilder()
                            .registerTypeAdapter(CurrencyConvertion::class.java, JsonDeserializer { json, _, _ ->
                                if (json.isJsonObject) {
                                    for (entry in json.asJsonObject.entrySet()) {
                                        val coefficient = entry?.value?.asDouble
                                        val convertion = CurrencyConvertion(from, to, coefficient)
                                        return@JsonDeserializer convertion
                                    }
                                }
                                return@JsonDeserializer null
                            }).create()
                    val currencyConvertion = gson.fromJson<CurrencyConvertion?>(response, CurrencyConvertion::class.java)
                    currencyConvertion
                }
    }
}