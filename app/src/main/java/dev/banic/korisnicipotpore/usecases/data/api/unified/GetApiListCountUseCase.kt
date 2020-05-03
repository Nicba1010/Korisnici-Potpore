package dev.banic.korisnicipotpore.usecases.data.api.unified

import dev.banic.korisnicipotpore.usecases.FlowableUseCaseWithNoParameters
import dev.banic.korisnicipotpore.usecases.data.api.local.GetLocalApiListCountUseCase
import dev.banic.korisnicipotpore.usecases.data.api.remote.GetRemoteApiListCountUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class GetApiListCountUseCase(
    private val getLocalApiListCountUseCase: GetLocalApiListCountUseCase,
    private val getRemoteApiListCountUseCase: GetRemoteApiListCountUseCase
) : FlowableUseCaseWithNoParameters<Int> {
    override fun execute(): Flowable<Int> {
        val localCount: Single<Int> = getLocalApiListCountUseCase.execute()

        val remoteCount: Single<Int> = localCount.flatMap { count ->
            getRemoteApiListCountUseCase.execute(count).doOnError {  }
        }

        return Single.merge(localCount, remoteCount)
    }
}