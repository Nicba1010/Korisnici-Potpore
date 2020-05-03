package dev.banic.korisnicipotpore.usecases.data

import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithTwoParameters
import io.reactivex.rxjava3.core.Single

class GetDataForYearMonthUseCase : SingleUseCaseWithTwoParameters<CompanyPaymentData, Int, Int> {
    override fun execute(p1: Int, p2: Int): Single<CompanyPaymentData> {
        TODO("Not yet implemented")
    }
}