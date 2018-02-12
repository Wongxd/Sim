//package io.github.wongxd.simplebuskt
//
//import io.github.wongxd.simplebuskt.annotation.SimEvent
//import io.github.wongxd.simplebuskt.annotation.SimEventMethod
//import io.github.wongxd.simplebuskt.observer.IObserver
//import io.github.wongxd.simplebuskt.thread.MainLooper
//import io.github.wongxd.simplebuskt.thread.ThreadMode
//import io.github.wongxd.simplebuskt.thread.ThreadPoolUtils
//import java.util.*
//
//
///**
// * 事件管理类
// */
//class SimBusKt_Bak private constructor() {
//
//    companion object {
//
//        private val default: SimBusKt_Bak = SimBusKt_Bak()
//
//        fun getInstance(): SimBusKt_Bak = default
//
//        private val poolUtils = ThreadPoolUtils(ThreadPoolUtils.CachedThread, 2)
//    }
//
//    private val TAG = javaClass.simpleName
//
//    private val observable: EventObservable = EventObservable()
//
//
//    /**
//     * 将事件接收者添加到注册列表
//     *
//     *
//     * 此时事件类型 为 DefaultEvent
//     *
//     *
//     * (如果将Activity作为订阅者在此注册的时候，切记在onDestroy()里面移除注册，否则可能导致内存泄露)
//     */
//    fun register(code: Int, observer: IObserver) {
//        register(code, DefaultEvent::class.java, observer)
//    }
//
//    /**
//     * 将事件接收者添加到注册列表
//     * (如果将Activity作为订阅者在此注册的时候，切记在onDestroy()里面移除注册，否则可能导致内存泄露)
//     *
//     * @param code
//     * @param event    可以是 事件 的实例 如 new DefaultEvent();  也可以是 事件 的类 如 DefaultEvent.class
//     * @param observer
//     */
//    fun register(code: Int, event: Any, observer: IObserver) {
//        observable.addObserver(observer)
//    }
//
//    /**
//     * 将事件接收者添加到注册列表
//     *
//     *
//     * (如果将Activity作为订阅者在此注册的时候，切记在onDestroy()里面移除注册，否则可能导致内存泄露)
//     */
//    private fun register(observer: IObserver) {
//        observable.addObserver(observer)
//    }
//
//    /**
//     * 将事件接收者移除注册列表
//     */
//    private fun unregister(observer: IObserver) {
//        observable.removeObserver(observer)
//    }
//
//    /**
//     * 将事件接收者移除注册列表
//     */
//    fun unregister(code: Int, observer: IObserver) {
//        unregister(code, DefaultEvent::class.java, observer)
//    }
//
//
//    /**
//     * 将事件接收者移除注册列表
//     *
//     * @param code
//     * @param event    可以是 事件 的实例 如 new DefaultEvent();  也可以是 事件 的类 如 DefaultEvent.class
//     * @param observer
//     */
//    fun unregister(code: Int, event: Any, observer: IObserver) {
//
//    }
//
//
//    /**
//     * 给订阅者发送事件
//     *
//     * @param event 事件对象
//     * @param code  事件标记（用于确定哪些接收者应该接收到事件）
//     */
//    fun notifyObservers(event: Any, code: Int) {
//        observable.notifyObservers(event, code)
//    }
//
//
//    //###########################################-----添加注解方式-----######################################################################
//    //###########################################-----添加注解方式-----######################################################################
//    //###########################################-----添加注解方式-----######################################################################
//
//
//    /**
//     * 一个特定事件类型 的所有的 对其监测的方法
//     */
//    private val eventSimEventMethods = HashMap<Class<*>, MutableList<SimEventMethod>>()
//
//    /**
//     * 一个订阅者中的所有的 的所有的 用 SimEvent 注解 的方法
//     */
//    private val subscriberSimEventMethods = HashMap<Any, MutableList<SimEventMethod>>()
//
//
//    /**
//     * 一个订阅者中的所有的 动态创建的 IObserver 实例
//     */
//    private val subscriberIObservers = HashMap<Any, MutableList<IObserver>>()
//
//
//    /**
//     * 注册
//     *
//     * @param subscriber 订阅者
//     */
//    fun registerClass(subscriber: Any) {
//        val subClass = subscriber.javaClass
//        val methods = subClass.declaredMethods
//        for (method in methods) {
//            if (method.isAnnotationPresent(SimEvent::class.java)) {
//                //获得参数类型
//                val parameterType = method.parameterTypes
//                //参数不为空 且参数个数为1
//                if (parameterType != null && parameterType.size == 1) {
//
//                    val eventType = parameterType[0]
//
//                    val simEvent = method.getAnnotation(SimEvent::class.java)
//                    val code = simEvent.code
//                    val threadMode = simEvent.threadMode
//
//                    val simEventMethod = SimEventMethod(subscriber, method, eventType, code, threadMode)
//                    addEventTypeMethodsToMap(eventType, simEventMethod)
//
//                    addSubscriberMethodsToMap(subscriber, simEventMethod)
//
//                    addSubscriber(simEventMethod)
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 将 对特定事件敏感的所有  用 SimEvent 注解 的 方法 添加到 集合
//     *
//     *
//     * 将注解方法信息以event类型为key保存到map中
//     *
//     * @param eventType     事件类型
//     * @param simEventMethod 注解方法信息
//     */
//    private fun addEventTypeMethodsToMap(eventType: Class<*>, simEventMethod: SimEventMethod) {
//        var simEventMethods: MutableList<SimEventMethod>? = eventSimEventMethods[eventType]
//        if (simEventMethods == null) {
//            simEventMethods = ArrayList()
//            eventSimEventMethods[eventType] = simEventMethods
//        }
//
//        if (!simEventMethods.contains(simEventMethod)) {
//            simEventMethods.add(simEventMethod)
//        }
//    }
//
//
//    /**
//     * 将  一个订阅者 中的所有  用 SimEvent 注解 的 方法 添加到 集合
//     *
//     *
//     * 将注解方法信息以event类型为key保存到map中
//     *
//     * @param subscriber     订阅者
//     * @param simEventMethod 注解方法信息
//     */
//    private fun addSubscriberMethodsToMap(subscriber: Any, simEventMethod: SimEventMethod) {
//        var simEventMethods: MutableList<SimEventMethod>? = subscriberSimEventMethods[subscriber]
//        if (simEventMethods == null) {
//            simEventMethods = ArrayList()
//            subscriberSimEventMethods[subscriber] = simEventMethods
//        }
//
//        if (!simEventMethods.contains(simEventMethod)) {
//            simEventMethods.add(simEventMethod)
//        }
//    }
//
//    /**
//     *
//     * 向 SimEventHolder 中添加 订阅者
//     *
//     * @param simEventMethod
//     */
//    private fun addSubscriber(simEventMethod: SimEventMethod) {
//        val eventType = simEventMethod.eventType
//        val sCode = simEventMethod.code
//        val subscriber = simEventMethod.subscriber
//
//        val observer = object : IObserver {
//            override fun onMessageReceived(event: Any, code: Int) {
//                callEvent(code, event)
//            }
//        }
//
//
//        var observers: MutableList<IObserver>? = subscriberIObservers[subscriber]
//        if (observers == null) {
//            observers = ArrayList()
//            subscriberIObservers[subscriber] = observers
//        }
//
//        if (!observers.contains(observer)) {
//            observers.add(observer)
//        }
//
//
//        register(observer)
//
//    }
//
//    /**
//     * 回调到订阅者的方法中
//     *
//     * @param code   code
//     * @param event event
//     */
//    private fun callEvent(code: Int, event: Any) {
//        val eventClass = event.javaClass
//        val methods = eventSimEventMethods[eventClass]
//        if (methods != null && methods.size > 0) {
//            for (simEvent in methods) {
//
//                val sub = simEvent.method.getAnnotation(SimEvent::class.java)
//                val c = sub.code
//                if (c == code) {
//                    postToThread(event, simEvent)
//                }
//
//            }
//        }
//    }
//
//
//    /**
//     * 用于处理订阅事件在那个线程中执行
//     *
//     * @param event
//     * @param simEventMethod
//     */
//    private fun postToThread(event: Any, simEventMethod: SimEventMethod) {
////        Log.e(TAG, event.toString() + "---" + simEventMethod.code + "---" + simEventMethod.eventType + "---" + simEventMethod.threadMode)
//        val runnable = Runnable {
//            //            Log.e(TAG, "期望线程：" + simEventMethod.threadMode.toString() + "---实际线程： " + Thread.currentThread().name)
//            simEventMethod.invoke(event)
//        }
//
//        when (simEventMethod.threadMode) {
//            ThreadMode.MAIN ->
//
//                MainLooper.runOnUiThread(runnable)
//
//
//            ThreadMode.IO ->
//
//                poolUtils.submit(runnable)
//
//
//            ThreadMode.CURRENT ->
//
//                runnable.run()
//        }
//    }
//
//    /**
//     * 取消注册
//     *
//     * @param subscriber
//     */
//    fun unregisterClass(subscriber: Any) {
//        val simEventMethods = subscriberSimEventMethods[subscriber]
//        if (simEventMethods != null) {
//            for (simEventMethod in simEventMethods) {
//                unSubscribeMethod(subscriber, simEventMethod)
//            }
//        }
//
//        subscriberSimEventMethods.remove(subscriber)
//        subscriberIObservers.remove(subscriber)
//    }
//
//
//    /**
//     * 从 一个特定事件类型 的 所有 的 SimEventMethod 中移除 当前 订阅者 中 的 SimEventMethod
//     */
//    private fun unSubscribeMethod(subscriber: Any, simEventMethod: SimEventMethod) {
//        val eventType = simEventMethod.eventType
//        val simEventMethods = eventSimEventMethods[eventType]
//        val shouldDelList = simEventMethods?.filter { it == simEventMethod }
//
//        shouldDelList?.forEach {
//            unSubscribeIObserver(subscriber, simEventMethod)
//        }
//
//        shouldDelList?.let { simEventMethods.removeAll(it) }
//    }
//
//    /**
//     *
//     * 移除 订阅者 中 的 IObserver 实例
//     *
//     * @param subscriber
//     *
//     * @param simEventMethod
//     *
//     */
//    private fun unSubscribeIObserver(subscriber: Any, simEventMethod: SimEventMethod) {
//        val observers = subscriberIObservers[subscriber]
//        if (observers != null) {
//            for (observer in observers) {
//                unregister(observer)
//            }
//
//            observers.clear()
//        }
//    }
//
//
//}
