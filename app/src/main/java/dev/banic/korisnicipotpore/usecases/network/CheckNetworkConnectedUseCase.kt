package dev.banic.korisnicipotpore.usecases.network

import android.content.Context
import dev.banic.korisnicipotpore.usecases.SingleUseCaseWithNoParameters
import dev.banic.korisnicipotpore.util.NetworkUtil
import io.reactivex.rxjava3.core.Single

class CheckNetworkConnectedUseCase(
    val context: Context
) : SingleUseCaseWithNoParameters<Boolean> {
    override fun execute(): Single<Boolean> {
        return Single.just(NetworkUtil.isNetworkConnected(context))
    }
}