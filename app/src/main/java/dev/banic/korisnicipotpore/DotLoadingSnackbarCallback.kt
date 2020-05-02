package dev.banic.korisnicipotpore

import android.os.Handler
import android.os.HandlerThread
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class DotLoadingSnackbarCallback(
    private val maxNumberOfDots: Int = 3,
    private val loadingStepDelay: Long = 333
) : Snackbar.Callback() {
    lateinit var handlerThread: HandlerThread
    lateinit var handler: Handler
    lateinit var uiHandler: Handler
    lateinit var originalText: String
    var numberOfDots = 0

    override fun onShown(sb: Snackbar) {
        super.onShown(sb)
        handlerThread = HandlerThread("Dot Thread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        uiHandler = Handler(sb.context.mainLooper)

        originalText = sb.view.findViewById<TextView>(
            com.google.android.material.R.id.snackbar_text
        ).text.toString()

        handler.post {
            val dataPartiallyLoadedText = sb.context.getString(R.string.data_partially_loaded)
            while (true) {
                Thread.sleep(loadingStepDelay)

                if (numberOfDots < maxNumberOfDots) {
                    numberOfDots++
                } else {
                    numberOfDots = 0
                }

                uiHandler.postAtFrontOfQueue {
                    sb.setText(dataPartiallyLoadedText + ".".repeat(numberOfDots))
                }
            }
        }
    }

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        handler.removeCallbacksAndMessages(null)
    }
}