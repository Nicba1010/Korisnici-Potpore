package dev.banic.korisnicipotpore.usecases

data class Optional<T>(val value: T?) {
    companion object {
        inline fun <reified U> absent(): Optional<U> = Optional(null)
    }
}


inline fun <T, U> Optional<T>.map(map: (T) -> U?): Optional<U> = Optional(
    this.value?.let(map)
)

inline fun <reified T> T?.toOptional() = Optional(this)