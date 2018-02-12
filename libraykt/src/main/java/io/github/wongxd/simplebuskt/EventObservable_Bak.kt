//package io.github.wongxd.simplebuskt
//
//import android.util.SparseArray
//import io.github.wongxd.simplebuskt.observer.IObservable
//import io.github.wongxd.simplebuskt.observer.IObserver
//import java.util.*
//import kotlin.collections.ArrayList
//
//
///**
// * 被观察者的具体化，该类负责给观察者发送事件
// * 参考 [java.util.Observable]
// */
//internal class EventObservable_Bak : IObservable {
//
//
//    /**
//     * 存放 对特定事件敏感的订阅者
//     */
//    private val observerListMap: MutableMap<Class<*>, MutableList<IObserver>> = HashMap<Class<*>, MutableList<IObserver>>()
//
//
//    /**
//     * 存放 对应 code 的 事件 类型
//     */
//    private val codeEventTypeArray: SparseArray<MutableList<Class<*>>> = SparseArray<MutableList<Class<*>>>()
//
//    init {
//        observerListMap.clear()
//        codeEventTypeArray.clear()
//    }
//
//
//    /**
//     * 删除所有订阅者.
//     */
//    @Synchronized
//    fun removeAllObservers() {
//        observerListMap.clear()
//        codeEventTypeArray.clear()
//    }
//
//
//    /**
//     * 通知事件订阅队列里的每一个接收该类型事件的订阅者
//     */
//    fun notifyObservers(code: Int) {
//        notifyObservers(DefaultEvent(), code)
//    }
//
//
//    override fun addObserver(observer: IObserver) {
//
//    }
//
//    override fun removeObserver(observer: IObserver) {
//
//    }
//
//    /**
//     * 向事件订阅队列添加订阅者
//     *
//     * @param code
//     * @param event
//     * @param observer
//     */
//    fun addObserver(code: Int, event: Any, observer: IObserver) {
//
//        val eventType: Class<*> = event as? Class<*> ?: event.javaClass
//
//        synchronized(this) {
//
//            val classList: MutableList<Class<*>> = if (codeEventTypeArray.get(code) == null) {
//                val temp: MutableList<Class<*>> = ArrayList()
//                codeEventTypeArray.put(code, temp)
//                temp
//            } else {
//                codeEventTypeArray.get(code)
//            }
//
//            if (!classList.contains(eventType))
//                classList.add(eventType)
//
//
//            var observers: MutableList<IObserver>? = observerListMap[eventType]?.toMutableList()
//
//            if (observers == null) {
//                observers = ArrayList()
//                observerListMap[eventType] = observers
//            }
//
//            if (!observers.contains(observer)) {
//                observers.add(observer)
//            }
//        }
//    }
//
//
//    /**
//     * 事件订阅队列移除订阅者
//     *
//     * @param code
//     * @param event
//     * @param observer
//     */
//    fun removeObserver(code: Int, event: Any, observer: IObserver) {
//
//
//        val eventType: Class<*> = event as? Class<*> ?: event.javaClass
//
//        synchronized(this) {
//
//            val classList: MutableList<Class<*>> = codeEventTypeArray.get(code)
//                    ?: throw NullPointerException("classList == null,your observer maybe not register yet")
//
//            classList.remove(eventType)
//
//            val observers = observerListMap[eventType]
//                    ?: throw NullPointerException("observers == null,your observer maybe not register yet")
//
//            observers.remove(observer)
//        }
//    }
//
//    /**
//     * 获得 特定 事件 订阅者的数量
//     */
//    fun countEventObservers(event: Any): Int {
//
//        var eventType: Class<*>? = null
//
//        eventType = event as? Class<*> ?: event.javaClass
//
//
//        val observers = observerListMap[eventType] ?: return 0
//
//        return observers.size
//    }
//
//
//    /**
//     * 获得 特定 CODE 订阅者的数量
//     */
//    fun countEventObservers(code: Int): Int {
//        val classList = codeEventTypeArray.get(code)
//        if (classList == null || classList.size == 0) {
//            return 0
//        }
//
//        return classList.sumBy { countEventObservers(it) }
//    }
//
//    /**
//     * 通知事件订阅队列里的每一个订阅者
//     *
//     * @param event 给订阅者传递的事件
//     */
//    override fun notifyObservers(event: Any, code: Int) {
//
//        var arrays: Array<IObserver>
//        val eventType = event.javaClass
//        synchronized(this) {
//
//            val classList = codeEventTypeArray.get(code)
//            if (classList == null || !classList.contains(eventType)) {
//                return
//            }
//
//
//            val observers = observerListMap[eventType] ?: return
//
//            arrays = observers.toTypedArray()
//
//            for (observer in arrays) {
//                observer.onMessageReceived(event, code)
//            }
//        }
//
//    }
//
//}
//
//
