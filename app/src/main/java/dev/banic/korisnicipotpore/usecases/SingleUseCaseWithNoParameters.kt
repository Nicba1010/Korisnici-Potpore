package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Single

interface SingleUseCaseWithNoParameters<T> {
    fun execute(): Single<T>
}