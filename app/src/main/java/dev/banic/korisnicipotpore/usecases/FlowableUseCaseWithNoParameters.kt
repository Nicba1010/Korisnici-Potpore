package dev.banic.korisnicipotpore.usecases

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface FlowableUseCaseWithNoParameters<T> {
    fun execute(): Flowable<T>
}