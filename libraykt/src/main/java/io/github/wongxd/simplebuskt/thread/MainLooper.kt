package io.github.wongxd.simplebuskt.thread

import android.os.Handler
import android.os.Looper

class MainLooper private constructor(looper: Looper) : Handler(looper) {
    companion object {
        private val instance = MainLooper(Looper.getMainLooper())

        fun runOnUiThread(runnable: Runnable) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run()
            } else {
                instance.post(runnable)
            }

        }
    }
}