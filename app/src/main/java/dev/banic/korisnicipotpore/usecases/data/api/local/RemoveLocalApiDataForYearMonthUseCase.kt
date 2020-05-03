package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.usecases.CompletableUseCaseWithTwoParameters
import io.reactivex.rxjava3.core.Completable

class RemoveLocalApiDataForYearMonthUseCase(
    private val getLocalApiDataUseCase: GetLocalApiDataUseCase,
    private val writeLocalApiDataUseCase: WriteLocalApiDataUseCase
) : CompletableUseCaseWithTwoParameters<Int, Int> {
    override fun execute(p1: Int, p2: Int): Completable {
        return getLocalApiDataUseCase.execute().flatMapCompletable { mutableAllCompanyData ->
            mutableAllCompanyData.apply {
                get(p1)?.remove(p2)
            }?.let {
                writeLocalApiDataUseCase.execute(it)
            }
        }
    }
}