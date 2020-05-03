package dev.banic.korisnicipotpore.usecases.data

import io.reactivex.rxjava3.core.Completable

interface CompletableUseCaseWithThreeParameters<U, V, W> {
    fun execute(p1: U, p2: V, p3: W): Completable
}