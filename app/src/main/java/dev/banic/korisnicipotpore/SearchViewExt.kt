package dev.banic.korisnicipotpore

import android.widget.ImageView
import android.widget.SearchView
import timber.log.Timber

fun SearchView.setSearchButtonEnabled(enabled: Boolean) {
    try {
        javaClass.getDeclaredField(
            "mSearchButton"
        ).let { mSearchButtonField ->
            try {
                mSearchButtonField.isAccessible = true
                (mSearchButtonField.get(this) as? ImageView)?.let { mSearchButton ->
                    mSearchButton.isEnabled = enabled
                }
            } catch (e: SecurityException) {
                Timber.e(
                    "Could not make mSearchButton accessible, SecurityException occurred"
                )
            }
        }
    } catch (e: NoSuchFieldException) {
        Timber.e("Couldn't find mSearchButton field, it doesn't exist")
    } catch (e: SecurityException) {
        Timber.e("Couldn't find mSearchButton field, SecurityException occurred")
    }
}