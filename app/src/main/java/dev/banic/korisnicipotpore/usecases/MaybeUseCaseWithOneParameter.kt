package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface MaybeUseCaseWithOneParameter<T, U> {
    fun execute(p1: U): Maybe<T>
}