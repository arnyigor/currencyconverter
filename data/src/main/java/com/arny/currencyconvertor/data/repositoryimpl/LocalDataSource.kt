package com.arny.currencyconvertor.data.repositoryimpl

import com.arny.domain.Constants
import com.arny.domain.models.Currency
import com.arny.domain.repositories.BaseRepository
import com.arny.domain.repositories.CurrencyDataSource
import com.arny.helpers.utils.fromCallable
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import io.reactivex.Observable

class LocalDataSource(private val baseRepository: BaseRepository) : CurrencyDataSource {

    override fun getAllCurrencies(): Observable<ArrayList<Currency>> {
        return fromCallable {
            val string = baseRepository.getPrefString(Constants.PREFS.ALL_CURRENCY)
            if (string.isNullOrBlank()) {
                arrayListOf()
            } else {
                val gson = GsonBuilder()
                        .registerTypeAdapter(ArrayList::class.java, JsonDeserializer { json, _, _ ->
                            val list = arrayListOf<Currency>()
                            if (json.isJsonArray) {
                                for (element in json.asJsonArray) {
                                    val obj = element.asJsonObject
                                    val code = obj.get("code")?.asString
                                    val name = obj.get("name")?.asString
                                    val symbol = obj.get("symbol")?.asString
                                    list.add(Currency(code, name, symbol))
                                }
                            }
                            return@JsonDeserializer list
                        }).create()
                gson.fromJson<ArrayList<Currency>>(string, ArrayList::class.java)
            }
        }
    }

    override fun saveCurencies(json: String?) {
        baseRepository.setPrefString(Constants.PREFS.ALL_CURRENCY, json)
    }
}