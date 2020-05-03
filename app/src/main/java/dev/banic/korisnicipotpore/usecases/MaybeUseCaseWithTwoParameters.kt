package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface MaybeUseCaseWithTwoParameters<T, U, V> {
    fun execute(p1: U, p2: V): Maybe<T>
}