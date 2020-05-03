package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Single

interface SingleUseCaseWithFourParameters<T, U, V, W, X> {
    fun execute(p1: U, p2: V, p3: W, p4: X): Single<T>
}