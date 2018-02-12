package io.github.wongxd.simplebuskt

import io.github.wongxd.simplebuskt.observer.IObserver

/**
 * Created by wongxd on 2018/2/10.
 */
class DefaultObserver(private val callback: (event: Any, code: Int) -> Unit) : IObserver {

    override fun onMessageReceived(event: Any, code: Int) {
        callback.invoke(event, code)
    }

}