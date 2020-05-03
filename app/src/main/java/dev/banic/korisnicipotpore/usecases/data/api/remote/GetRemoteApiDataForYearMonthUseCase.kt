package dev.banic.korisnicipotpore.usecases.data.api.remote

import dev.banic.korisnicipotpore.data.Api
import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithFourParameters
import io.reactivex.rxjava3.core.Single

class GetRemoteApiDataForYearMonthUseCase(
    private val api: Api
) : SingleUseCaseWithFourParameters<CompanyPaymentData, Int, Int, Int?, Int?> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(
        year: Int,
        month: Int,
        pageSize: Int?,
        pageIndex: Int?
    ): Single<CompanyPaymentData> {
        return api.getCompanyPaymentData(
            Api.CompanyPaymentDataRequest(
                year,
                month,
                pageSize ?: Api.DEFAULT_PAGE_SIZE,
                pageIndex ?: 1
            )
        )
    }
}