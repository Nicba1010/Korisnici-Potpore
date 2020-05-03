package dev.banic.korisnicipotpore.usecases.data.api.local

import dev.banic.korisnicipotpore.data.AllCompanyData
import dev.banic.korisnicipotpore.usecases.CompletableUseCaseWithOneParameter
import dev.banic.korisnicipotpore.usecases.data.raw.WriteJsonFileUseCase
import io.reactivex.rxjava3.core.Completable

class WriteLocalApiDataUseCase(
    private val writeJsonFileUseCase: WriteJsonFileUseCase<AllCompanyData>
) : CompletableUseCaseWithOneParameter<AllCompanyData> {

    override fun execute(p1: AllCompanyData): Completable {
        return writeJsonFileUseCase.execute(ALL_DATA_FILENAME, p1)
    }

    companion object {
        const val ALL_DATA_FILENAME: String = "all-data.json"
    }
}