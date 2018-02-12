package io.github.wongxd.simplebuskt

import android.util.SparseArray
import io.github.wongxd.simplebuskt.observer.IObservable
import io.github.wongxd.simplebuskt.observer.IObserver
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * 被观察者的具体化，该类负责给观察者发送事件
 * 参考 [java.util.Observable]
 */
internal class EventObservable : IObservable {


    companion object {
        data class ShouldRemovedListBean(val code: Int, val event: Class<*>, val observer: IObserver)
    }

    /**
     * 特定 事件类型 对应 的订阅者
     */
    private val type2Observer: MutableMap<Class<*>, Vector<IObserver>> = HashMap()

    /**
     * 特定code 对应 的 事件类型
     */
    private val code2EventType: SparseArray<Vector<Class<*>>> = SparseArray()


    /**
     * 标记通知是否结束了 避免 通过通知 取消总线时  ConcurrentModificationException 。
     */
    private var isNotifyFinish = true

    /**
     * 应该移除的订阅者的列表
     */
    private val shouldRemovedList: MutableList<ShouldRemovedListBean> = ArrayList()


    /**
     * 移除所有订阅者.
     */
    @Synchronized
    fun removeAllObservers() {
        code2EventType.clear()
        type2Observer.clear()
    }


    /**
     * 添加订阅者
     */
    @Synchronized
    override fun addObserver(code: Int, event: Class<*>, observer: IObserver) {

//        Log.e("addObserver","$code   $event   $observer")
        if (code2EventType[code] == null) {
            val temp: Vector<Class<*>> = Vector()
            temp.addElement(event)
            code2EventType.put(code, temp)
        }

        val observers: Vector<IObserver> = if (type2Observer[event] == null) {
            val temp: Vector<IObserver> = Vector()
            type2Observer[event] = temp
            temp
        } else {
            type2Observer[event]!!
        }


        if (!observers.contains(observer)) {
            observers.add(observer)
        }

    }


    /**
     * 移除订阅者
     */
    @Synchronized
    override fun removeObserver(code: Int, event: Class<*>, observer: IObserver) {


        if (!isNotifyFinish) {
            shouldRemovedList.add(ShouldRemovedListBean(code, event, observer))
            return
        }

//        Log.e("removeObserver","$code   $event   $observer")
        if (code2EventType.get(code) == null) return

        val observers: Vector<IObserver> = type2Observer[event] ?: return

        observers.remove(observer)
    }


    /**
     * 获得订阅者的数量
     */
    @Synchronized
    fun countObservers(): Int {
        var count = 0
        type2Observer.values.forEach { count += it.size }
        return count
    }


    /**
     * 通知事件订阅队列里的每一个订阅者
     *
     * @param event 给订阅者传递的事件
     */
    override fun notifyObservers(event: Any, code: Int) {

        synchronized(this) {

            if (code2EventType.get(code) == null) return

            val eventType: Class<*> = event.javaClass

            val observers = type2Observer[eventType] ?: return

            isNotifyFinish = false
            observers.forEach {
                it.onMessageReceived(event, code)
            }
            isNotifyFinish = true
            doIfHadRemoveEvent()
        }

    }


    /**
     * 如果有 移除订阅者的操作，在这里执行，可以 避免 通过通知 取消总线时  ConcurrentModificationException 。
     */
    private fun doIfHadRemoveEvent() {
        shouldRemovedList.forEach {
            removeObserver(it.code, it.event, it.observer)
        }
        shouldRemovedList.clear()
    }

}


