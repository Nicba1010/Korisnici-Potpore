package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Completable

interface CompletableUseCaseWithTwoParameters<U, V> {
    fun execute(p1: U, p2: V): Completable
}