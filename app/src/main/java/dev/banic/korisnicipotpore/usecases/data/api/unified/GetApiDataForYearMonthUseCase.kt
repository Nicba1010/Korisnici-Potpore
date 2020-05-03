package dev.banic.korisnicipotpore.usecases.data.api.unified

import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.usecases.FlowableUseCaseWithTwoParameters
import dev.banic.korisnicipotpore.usecases.data.api.local.GetLocalApiDataForYearMonthUseCase
import dev.banic.korisnicipotpore.usecases.data.api.remote.GetRemoteApiDataForYearMonthUseCase
import dev.banic.korisnicipotpore.usecases.network.CheckNetworkConnectedUseCase
import io.reactivex.rxjava3.core.Flowable

class GetApiDataForYearMonthUseCase(
    private val getLocalApiDataForYearMonthUseCase: GetLocalApiDataForYearMonthUseCase,
    private val getRemoteApiDataForYearMonthUseCase: GetRemoteApiDataForYearMonthUseCase,
    private val checkNetworkConnectedUseCase: CheckNetworkConnectedUseCase
) : FlowableUseCaseWithTwoParameters<CompanyPaymentData, Int, Int> {
    override fun execute(p1: Int, p2: Int): Flowable<CompanyPaymentData> {
        TODO("Not yet implemented")
    }
//    override fun execute(year: Int, month: Int): Flowable<Pair<CompanyPaymentData, LoadSource>> {
//        return getLocalApiDataForYearMonthUseCase.execute(year, month).map { it to LoadSource.LOCAL_DATA }.toFlowable()
//        return getLocalApiDataForYearMonthUseCase.execute(year, month).toFlowable().flatMap { data ->
//            var list: List<Single<Optional<Pair<CompanyPaymentData, LoadSource>>>> = listOf()
//
//            if (data.value != null) {
//                list = listOf(
//                    Single.just(Pair(data.value, LoadSource.LOCAL_DATA).toOptional()),
//                    checkNetworkConnectedUseCase.execute().flatMap { available ->
//                        if (available) {
//                            getRemoteApiDataForYearMonthUseCase.execute(
//                                year,
//                                month,
//                                null,
//                                null
//                            ).map {
//                                Pair(it, LoadSource.NETWORK)
//                            }.convertToOnErrorOptional()
//                        } else {
//                            Single.just(Optional.absent<Pair<CompanyPaymentData, LoadSource>>())
//                        }
//                    }
//                )
//            } else {
//                list = listOf(
//                    checkNetworkConnectedUseCase.execute().flatMap { available ->
//                        if (available) {
//                            getRemoteApiDataForYearMonthUseCase.execute(
//                                year,
//                                month,
//                                null,
//                                null
//                            ).map {
//                                Pair(it, LoadSource.NETWORK)
//                            }.convertToOnErrorOptional()
//                        } else {
//                            Single.error(NetworkNotAvailableException())
//                        }
//                    }
//                )
//            }
//
//            Flowable.empty()
//        }
//    }

    enum class LoadSource {
        NETWORK_PRELOAD,
        NETWORK,
        LOCAL_DATA
    }
}