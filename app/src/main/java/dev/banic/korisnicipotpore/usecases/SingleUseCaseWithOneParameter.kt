package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Single

interface SingleUseCaseWithOneParameter<T, U> {
    fun execute(p1: U): Single<T>
}