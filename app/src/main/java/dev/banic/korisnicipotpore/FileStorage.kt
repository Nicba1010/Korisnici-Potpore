package dev.banic.korisnicipotpore

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.banic.korisnicipotpore.Api.CompanyPaymentData
import java.io.FileNotFoundException

object FileStorage {
    private val GSON: Gson = GsonBuilder().create()

    fun storeData(context: Context, data: CompanyPaymentData) {
        context.openFileOutput("data.json", Context.MODE_PRIVATE).use { fos ->
            fos.writer().use { osw ->
                osw.write(GSON.toJson(data))
            }
        }
    }

    fun readData(context: Context): CompanyPaymentData? {
        return try {
            context.openFileInput("data.json").use { fis ->
                fis.reader().use { isr ->
                    GSON.fromJson(isr, CompanyPaymentData::class.java)
                }
            }
        } catch (e: FileNotFoundException) {
            null
        }
    }
}