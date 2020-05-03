package dev.banic.korisnicipotpore.usecases.data.raw

import com.google.gson.Gson
import dev.banic.korisnicipotpore.usecases.CompletableUseCaseWithTwoParameters
import io.reactivex.rxjava3.core.Completable

abstract class WriteJsonFileUseCase<T>(
    private val writeFileUseCase: WriteFileUseCase
) : CompletableUseCaseWithTwoParameters<String, T> {
    
    override fun execute(p1: String, p2: T): Completable {
        return writeFileUseCase.execute(p1, GSON.toJson(p2))
    }

    companion object {
        val GSON: Gson = Gson()
    }
}