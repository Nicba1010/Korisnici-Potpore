package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface FlowableUseCaseWithTwoParameters<T, U, V> {
    fun execute(p1: U, p2: V): Flowable<T>
}