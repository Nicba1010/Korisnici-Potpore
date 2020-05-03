package dev.banic.korisnicipotpore.usecases.data.raw

import dev.banic.korisnicipotpore.data.FileStorage
import dev.banic.korisnicipotpore.usecases.CompletableUseCaseWithTwoParameters
import io.reactivex.rxjava3.core.Completable

class WriteFileUseCase(
    private val fileStorage: FileStorage
) : CompletableUseCaseWithTwoParameters<String, String> {
    override fun execute(p1: String, p2: String): Completable {
        return Completable.fromAction {
            fileStorage.writeFile(p1, p2)
        }
    }
}