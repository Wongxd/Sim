package io.github.wongxd.simplebuskt.observer

/**
 * 事件订阅者（观察者）
 */
interface IObserver {

    /**
     * 当消息发布者发布事件的时候调用该方法
     *
     * @param event 发布的事件
     * @param code  事件标记（用于确定哪些接收者应该接收到事件）
     */
    fun onMessageReceived(event: Any, code: Int)

}
