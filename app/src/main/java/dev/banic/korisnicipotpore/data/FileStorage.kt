package dev.banic.korisnicipotpore.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import java.io.FileNotFoundException

typealias AllCompanyData = Map<Int, Map<Int, CompanyPaymentData>>
typealias MutableAllCompanyData = MutableMap<Int, MutableMap<Int, CompanyPaymentData>>

object FileStorage {
    private val GSON: Gson = GsonBuilder().create()

    fun storeData(context: Context, data: CompanyPaymentData, year: Int, month: Int) {
        storeAllData(
            context,
            readAllData(context)
                .apply {
                    getOrPut(year, { mutableMapOf() })[month] = data
                }
        )
    }

    fun readData(context: Context, year: Int, month: Int): CompanyPaymentData? {
        return readAllData(context)[year]?.get(month)
    }

    @Suppress("RemoveExplicitTypeArguments")
    fun readAllData(context: Context): MutableAllCompanyData {
        return try {
            context.openFileInput("all-data.json").use { fis ->
                fis.reader().use { isr ->
                    GSON.fromJson<AllCompanyData>(
                        isr,
                        object : TypeToken<AllCompanyData>() {}.type
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            mapOf<Int, Map<Int, CompanyPaymentData>>()
        }.mapValues {
            it.value.toMutableMap()
        }.toMutableMap()
    }

    fun storeAllData(context: Context, data: AllCompanyData) {
        context.openFileOutput("all-data.json", Context.MODE_PRIVATE).use { fos ->
            fos.writer().use { osw ->
                osw.write(GSON.toJson(data))
            }
        }
    }
}