package dev.banic.korisnicipotpore.util

import dev.banic.korisnicipotpore.usecases.Optional
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

inline fun <reified T> T.toSingle(): Single<T> = Single.just(this)

inline fun <reified T> Single<T>.convertToOnErrorOptional(): Single<Optional<T>> {
    return this.flatMap(
        { data: T ->
            Single.just(Optional(data))
        }, { t: Throwable ->
            Single.just(Optional.absent())
        }
    )
}

inline fun <reified T> Flowable<Optional<T>>.filterOutAbsents(): Flowable<T> {
    return this.filter { it.value != null }.map { it.value!! }
}