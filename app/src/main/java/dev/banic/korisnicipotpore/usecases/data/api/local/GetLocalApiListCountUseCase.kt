package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithNoParameters
import io.reactivex.rxjava3.core.Single

class GetLocalApiListCountUseCase(
    private val getLocalApiDataUseCase: GetLocalApiDataUseCase
) : SingleUseCaseWithNoParameters<Int> {
    override fun execute(): Single<Int> {
        return getLocalApiDataUseCase.execute().map {
            it.size
        }
    }
}