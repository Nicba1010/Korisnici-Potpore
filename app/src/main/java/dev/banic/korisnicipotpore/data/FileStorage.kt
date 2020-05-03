package dev.banic.korisnicipotpore.data

interface FileStorage {
    fun readFile(filename: String): String?
    fun writeFile(filename: String, data: String)
    fun removeFile(filename: String): Boolean
}