package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.usecases.data.CompletableUseCaseWithThreeParameters
import io.reactivex.rxjava3.core.Completable

class WriteLocalApiDataForYearMonthUseCase(
    private val getLocalApiDataUseCase: GetLocalApiDataUseCase,
    private val writeLocalApiDataUseCase: WriteLocalApiDataUseCase
) : CompletableUseCaseWithThreeParameters<CompanyPaymentData, Int, Int> {
    override fun execute(p1: CompanyPaymentData, p2: Int, p3: Int): Completable {
        return getLocalApiDataUseCase.execute().flatMapCompletable { mutableAllCompanyData ->
            mutableAllCompanyData.apply {
                getOrPut(p2, { mutableMapOf() })[p3] = p1
            }?.let {
                writeLocalApiDataUseCase.execute(it)
            }
        }
    }
}