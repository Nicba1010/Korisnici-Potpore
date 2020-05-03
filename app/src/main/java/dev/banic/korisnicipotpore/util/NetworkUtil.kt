package dev.banic.korisnicipotpore.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object NetworkUtil {
    fun isNetworkConnected(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
            it.activeNetwork?.let { network ->
                it.getNetworkCapabilities(
                    network
                )?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
            }
        } ?: false
    }
}