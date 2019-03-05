package io.github.wongxd.simplebuskt

import io.github.wongxd.simplebuskt.observer.IObserver

/**
 * Created by wongxd on 2018/2/10.
 */
class DefaultObserver(private val callback: (event: DefaultEvent, code: Int) -> Unit) : IObserver<DefaultEvent> {

    override fun onMessageReceived(event: DefaultEvent, code: Int) {
        callback.invoke(event, code)
    }

}