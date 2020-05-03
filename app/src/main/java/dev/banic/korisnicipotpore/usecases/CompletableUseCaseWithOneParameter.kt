package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Completable

interface CompletableUseCaseWithOneParameter<U> {
    fun execute(p1: U): Completable
}