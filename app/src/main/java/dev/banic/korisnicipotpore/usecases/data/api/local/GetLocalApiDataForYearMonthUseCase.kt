package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.usecases.Optional
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithTwoParameters
import dev.banic.korisnicipotpore.usecases.toOptional
import io.reactivex.rxjava3.core.Single

class GetLocalApiDataForYearMonthUseCase(
    private val getLocalApiDataUseCase: GetLocalApiDataUseCase
) : SingleUseCaseWithTwoParameters<Optional<CompanyPaymentData>, Int, Int> {
    override fun execute(p1: Int, p2: Int): Single<Optional<CompanyPaymentData>> {
        return getLocalApiDataUseCase.execute().map { mutableAllCompanyData ->
            mutableAllCompanyData[p1]?.get(p2).toOptional()
        }
    }
}