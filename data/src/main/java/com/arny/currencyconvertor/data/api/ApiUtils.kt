package com.arny.sentry.data.api

import com.arny.currencyconvertor.data.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

fun <T> fromJson(response: Any, cls: Class<*>): T {
    return fromJson(response, cls, Gson())
}

fun <T> fromJson(response: Any, cls: Class<*>, gson: Gson): T {
    val t = gson.fromJson(response.toString(), cls) as T
    return t
}

@JvmOverloads
fun toJson(obj: Any, gson: Gson = Gson()): String {
    return gson.toJson(obj)
}

fun jsonObjectFillHashMap(vararg params: JSONObject?, map: HashMap<String, Any>): HashMap<String, Any> {
    for (param in params) {
        if (param != null) {
            map.putAll(getJsonObjectToHashMap(param))
        }
    }
    return map
}

fun getRequestBody(params: HashMap<String, Any>): RequestBody {
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(params).toString())
}

fun getJsonObjectToHashMap(params: JSONObject): HashMap<String, Any> {
    val mapParams = HashMap<String, Any>()
    try {
        for ((key, value) in jsonToMap(params)) {
            mapParams[key] = value
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return mapParams
}

@Throws(JSONException::class)
fun jsonToMap(json: JSONObject): Map<String, Any> {
    var retMap: Map<String, Any> = HashMap()
    if (json !== JSONObject.NULL) {
        retMap = toMap(json)
    }
    return retMap
}

@Throws(JSONException::class)
private fun toMap(obj: JSONObject): Map<String, Any> {
    val map = HashMap<String, Any>()
    val keysItr = obj.keys()
    while (keysItr.hasNext()) {
        val key = keysItr.next()
        var value = obj.get(key)
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = toMap(value)
        }
        map[key] = value
    }
    return map
}

@Throws(JSONException::class)
private fun toList(array: JSONArray): List<Any> {
    val list = ArrayList<Any>()
    for (i in 0 until array.length()) {
        var value = array.get(i)
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = toMap(value)
        }
        list.add(value)
    }
    return list
}

fun getResponseError(throwable: Throwable): String {
    var error = ""
    var code: Int
    try {
        if (throwable is HttpException) {
            code = throwable.code()
            val result = throwable.response()?.errorBody()?.string()
            try {
                val responseError = JSONObject(result)
                code = responseError.getInt("code")
                val message = StringBuilder()
                message.append("$code;")
                if (responseError.has("error")) {
                    message.append(responseError.getString("error")).append(";")
                }
                if (responseError.has("message")) {
                    message.append("Message:${responseError.getString("message").trim()}").append(";")
                }
                if (throwable.message != null) {
                    message.append("Ошибка сервера:${throwable.message}").append(";")
                }
                error = message.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
                error = result.toString()
            }

            when (code) {
                504 -> error = "Время ожидания истекло,повторите запрос позже"
                503 -> error = "Сервис временно недоступен,повторите запрос позже"
            }
        } else {
            val message = throwable.message ?: "Ошибка запроса"
            val contains = when {
                message.contains("Unable to resolve host") -> true
                message.contains("Failed to connect") -> true
                else -> false
            }
            error = if (contains) {
                "Ошибка соединения, адрес недоступен"
            } else {
                message
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return error
}


object ApiProvider {
    fun <T> provideApi(
            baseUrl: String,
            clazz: Class<T>,
            httpLevel: HttpLoggingInterceptor.Level? = HttpLoggingInterceptor.Level.BODY,
            timeout: Long = 30L
    ): T {
        val httpClient = OkHttpClient.Builder()
        val gson = GsonBuilder().setLenient().create()
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)
        httpClient.followRedirects(true)
        httpClient.protocols(Collections.singletonList(Protocol.HTTP_1_1))
        httpClient.addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    level =
                            if (BuildConfig.DEBUG) httpLevel else HttpLoggingInterceptor.Level.NONE
                })
        httpClient.networkInterceptors().add(StethoInterceptor())
        return Retrofit.Builder()
            .baseUrl(baseUrl)
                .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(clazz)
    }
}