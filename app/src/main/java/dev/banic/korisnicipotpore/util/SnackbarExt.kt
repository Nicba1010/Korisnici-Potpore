@file:Suppress("unused")

package dev.banic.korisnicipotpore.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import dev.banic.korisnicipotpore.R
import dev.banic.korisnicipotpore.ui.CompanyListsActivity

fun Snackbar.setNonDismissingAction(
    @StringRes resId: Int,
    listener: View.OnClickListener
): Snackbar {
    return setAction(resId) {
    }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar) {
            super.onShown(transientBottomBar)

            transientBottomBar.view.findViewById<View>(
                com.google.android.material.R.id.snackbar_action
            ).setOnClickListener(listener)
        }
    })
}

fun Snackbar.setNonDismissingAction(
    @StringRes resId: Int,
    listener: (View) -> Unit
): Snackbar {
    return setAction(resId) {
    }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar) {
            super.onShown(transientBottomBar)

            transientBottomBar.view.findViewById<View>(
                com.google.android.material.R.id.snackbar_action
            ).setOnClickListener(listener)
        }
    })
}

fun Snackbar.setNonDismissingAction(
    text: CharSequence,
    listener: View.OnClickListener
): Snackbar {
    return setAction(text) {
    }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar) {
            super.onShown(transientBottomBar)

            transientBottomBar.view.findViewById<View>(
                com.google.android.material.R.id.snackbar_action
            ).setOnClickListener(listener)
        }
    })
}

fun Snackbar.setNonDismissingAction(
    text: CharSequence,
    listener: (View) -> Unit
): Snackbar {
    return setAction(text) {
    }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar) {
            super.onShown(transientBottomBar)

            transientBottomBar.view.findViewById<View>(
                com.google.android.material.R.id.snackbar_action
            ).setOnClickListener(listener)
        }
    })
}

fun Snackbar.animateLoading(
    @StringRes loadingMessageRes: Int? = null,
    maxNumberOfDots: Int = 3,
    loadingStepDelay: Long = 333,
    timeoutMs: Long = -1,
    onLoadingAnimationTimeout: (() -> Unit)? = null
): Snackbar {
    // TODO: Make an option to disable action during loading
    var numberOfDots = 0
    val handler = Handler(
        HandlerThread("Dot Thread").apply {
            start()
        }.looper
    )
    val uiHandler = Handler(context.mainLooper)

    val originalText: String = view.findViewById<TextView>(
        com.google.android.material.R.id.snackbar_text
    ).text.toString()

    val updatingText: String = loadingMessageRes?.let {
        context.getString(loadingMessageRes)
    } ?: originalText

    fun startLoading() {
        val startTime: Long = System.currentTimeMillis()
        handler.post {
            while (timeoutMs == -1L || (System.currentTimeMillis() - startTime) < timeoutMs) {
                if (numberOfDots < maxNumberOfDots) {
                    numberOfDots++
                } else {
                    numberOfDots = 0
                }

                uiHandler.postAtFrontOfQueue {
                    setText(updatingText + ".".repeat(numberOfDots))
                }

                Thread.sleep(loadingStepDelay)
            }

            uiHandler.postAtFrontOfQueue {
                setText(originalText)
            }

            onLoadingAnimationTimeout?.invoke()
        }
    }

    if (isShown) {
        startLoading()
    } else {
        addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {
                super.onShown(transientBottomBar)
                startLoading()
            }
        })
    }

    return addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            handler.removeCallbacksAndMessages(null)
        }
    })
}

fun Snackbar.setNonSwipeable(): Snackbar {
    return setBehavior(
        NoSwipeBehavior()
    )
}

fun Activity.makeNetworkCheckSnackbar(
    view: View,
    @StringRes pleaseEnableResId: Int,
    @StringRes pleaseEnableMobileManuallyResId: Int,
    @StringRes loadingMessageRes: Int,
    @Duration duration: Int
): Snackbar {
    val connectivityManager: ConnectivityManager? = getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as? ConnectivityManager

    return Snackbar.make(view, pleaseEnableResId, duration).apply {
        setNonDismissingAction(R.string.enable_network) {
            animateLoading(loadingMessageRes, timeoutMs = 10000L)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivityForResult(
                    Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),
                    CompanyListsActivity.REQUEST_CODE_CONNECTIVITY
                )
            } else {
                Toast.makeText(
                    this@makeNetworkCheckSnackbar,
                    pleaseEnableMobileManuallyResId,
                    Toast.LENGTH_LONG
                ).show()
                (getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.apply {
                    @Suppress("DEPRECATION")
                    isWifiEnabled = true
                }
            }
        }
        setActionTextColor(getColor(R.color.colorPrimary))
        setNonSwipeable()
        connectivityManager?.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    animateLoading(loadingMessageRes, timeoutMs = 5000L)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    capabilities: NetworkCapabilities
                ) {
                    if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        connectivityManager.unregisterNetworkCallback(this)
                        dismiss()
                    }
                }
            }
        )
    }
}

internal class NoSwipeBehavior : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View): Boolean {
        return false
    }
}