package dev.banic.korisnicipotpore.usecases.data.raw

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.banic.korisnicipotpore.usecases.Optional
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithTwoParameters
import dev.banic.korisnicipotpore.usecases.map
import io.reactivex.rxjava3.core.Single

abstract class ReadJsonFileUseCase<T>(
    private val readFileUseCase: ReadFileUseCase
) : SingleUseCaseWithTwoParameters<Optional<T>, String, TypeToken<T>> {

    override fun execute(p1: String, p2: TypeToken<T>): Single<Optional<T>> {
        return readFileUseCase.execute(p1).map { data ->
            data.map {
                GSON.fromJson<T>(it, p2.type)
            }
        }
    }

    companion object {
        val GSON: Gson = Gson()
    }
}