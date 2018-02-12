package io.github.wongxd.simplebuskt.observer

/**
 * 事件发布者（被观察者）
 */

interface IObservable {


    /**
     * 向事件订阅队列添加订阅者
     */
    fun addObserver(code: Int, event: Class<*>, observer: IObserver)


    /**
     * 删除指定订阅者
     */
    fun removeObserver(code: Int, event: Class<*>, observer: IObserver)

    /**
     * 通知事件订阅队列里的每一个订阅者
     *
     * @param event 给订阅者传递的事件
     * @param code  标记  （用于确定哪些接收者应该接收到事件）
     */
    fun notifyObservers(event: Any, code: Int)

}
