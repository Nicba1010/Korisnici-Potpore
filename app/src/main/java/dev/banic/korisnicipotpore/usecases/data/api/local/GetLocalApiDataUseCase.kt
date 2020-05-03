package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.data.AllCompanyData
import dev.banic.korisnicipotpore.data.MutableAllCompanyData
import dev.banic.korisnicipotpore.data.toMutable
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithNoParameters
import dev.banic.korisnicipotpore.usecases.data.raw.ReadJsonFileUseCase
import dev.banic.korisnicipotpore.util.makeTypeToken
import io.reactivex.rxjava3.core.Single

class GetLocalApiDataUseCase(
    private val readJsonFileUseCase: ReadJsonFileUseCase<AllCompanyData>
) : SingleUseCaseWithNoParameters<MutableAllCompanyData> {
    override fun execute(): Single<MutableAllCompanyData> {
        return readJsonFileUseCase.execute(
            ALL_DATA_FILENAME,
            makeTypeToken()
        ).map { allCompanyData ->
            allCompanyData?.value?.toMutable() ?: mutableMapOf()
        }
    }

    companion object {
        const val ALL_DATA_FILENAME: String = "all-data.json"
    }
}