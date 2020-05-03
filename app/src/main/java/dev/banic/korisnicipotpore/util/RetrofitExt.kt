package dev.banic.korisnicipotpore.util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


inline fun <reified T> Call<T>.enqueue(
    crossinline onResponse: (Call<T>, Response<T>) -> Unit,
    crossinline onFailure: (Call<T>, Throwable) -> Unit
) {
    return this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponse(call, response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(call, t)
        }
    })
}