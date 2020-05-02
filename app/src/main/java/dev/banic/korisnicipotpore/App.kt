package dev.banic.korisnicipotpore

import android.app.Application
import android.content.pm.ApplicationInfo
import timber.log.Timber

import timber.log.Timber.DebugTree


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
            Timber.plant(DebugTree())
        }
    }
}