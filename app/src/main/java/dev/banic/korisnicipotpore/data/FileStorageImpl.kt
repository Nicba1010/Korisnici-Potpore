package dev.banic.korisnicipotpore.data

import android.content.Context
import java.io.FileNotFoundException


class FileStorageImpl(val context: Context) : FileStorage {
    override fun readFile(filename: String): String? {
        return try {
            context.openFileInput(filename).use { fis ->
                fis.reader().use { isr ->
                    isr.readText()
                }
            }
        } catch (e: FileNotFoundException) {
            null
        }
    }

    override fun writeFile(filename: String, data: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
            fos.writer().use { osw ->
                osw.write(data)
            }
        }
    }

    override fun removeFile(filename: String): Boolean {
        return context.deleteFile(filename)
    }
}