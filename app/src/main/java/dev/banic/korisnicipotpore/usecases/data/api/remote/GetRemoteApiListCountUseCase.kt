package dev.banic.korisnicipotpore.usecases.data.api.remote

import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithOneParameter
import dev.banic.korisnicipotpore.usecases.network.CheckNetworkConnectedUseCase
import dev.banic.korisnicipotpore.usecases.network.NetworkNotAvailableException
import dev.banic.korisnicipotpore.util.ApiUtil
import io.reactivex.rxjava3.core.Single

class GetRemoteApiListCountUseCase(
    private val checkRemoteApiDataForYearMonthAvailableUseCase: CheckRemoteApiDataForYearMonthAvailableUseCase,
    private val checkNetworkConnectedUseCase: CheckNetworkConnectedUseCase
) : SingleUseCaseWithOneParameter<Int, Int> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(startingIndex: Int): Single<Int> {
        return checkNetworkConnectedUseCase.execute().flatMap { connected ->
            if (!connected) {
                if (startingIndex == 0) {
                    Single.error(NetworkNotAvailableException())
                } else {
                    Single.just(startingIndex)
                }
            } else {
                getRemoteApiListCount(startingIndex)
            }
        }
    }

    private fun getRemoteApiListCount(currentIndex: Int): Single<Int> {
        return checkRemoteApiDataForYearMonthAvailableUseCase.execute(
            ApiUtil.getYearForIndex(currentIndex),
            ApiUtil.getMonthForIndex(currentIndex)
        ).flatMap { dataAvailable ->
            if (dataAvailable) {
                getRemoteApiListCount(currentIndex + 1)
            } else {
                Single.just(currentIndex)
            }
        }
    }
}