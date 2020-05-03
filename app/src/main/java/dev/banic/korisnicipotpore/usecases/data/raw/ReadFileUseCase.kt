package dev.banic.korisnicipotpore.usecases.data.raw

import dev.banic.korisnicipotpore.data.FileStorage
import dev.banic.korisnicipotpore.usecases.MaybeUseCaseWithOneParameter
import dev.banic.korisnicipotpore.usecases.Optional
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithOneParameter
import dev.banic.korisnicipotpore.usecases.toOptional
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class ReadFileUseCase(
    private val fileStorage: FileStorage
) : SingleUseCaseWithOneParameter<Optional<String>, String> {
    override fun execute(p1: String): Single<Optional<String>> {
        return Single.just(fileStorage.readFile(p1).toOptional())
    }
}