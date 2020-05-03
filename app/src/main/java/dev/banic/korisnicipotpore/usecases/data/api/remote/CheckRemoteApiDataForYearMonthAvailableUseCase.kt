package dev.banic.korisnicipotpore.usecases.data.api.remote

import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithTwoParameters
import io.reactivex.rxjava3.core.Single

class CheckRemoteApiDataForYearMonthAvailableUseCase(
    private val getRemoteApiDataForYearMonthUseCase: GetRemoteApiDataForYearMonthUseCase
) : SingleUseCaseWithTwoParameters<Boolean, Int, Int> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(year: Int, month: Int): Single<Boolean> {
        return getRemoteApiDataForYearMonthUseCase.execute(year, month, 1, 1).map {
            it.totalCount > 0
        }
    }
}