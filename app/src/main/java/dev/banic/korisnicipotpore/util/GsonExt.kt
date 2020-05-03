package dev.banic.korisnicipotpore.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.banic.korisnicipotpore.usecases.Optional

val DEFAULT_GSON: Gson = Gson()

inline fun <reified T> makeTypeToken(): TypeToken<T> = object : TypeToken<T>() {}
inline fun <reified T> T?.toJson(gson: Gson = DEFAULT_GSON) = gson.toJson(this)