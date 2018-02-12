package io.github.wongxd.simplebuskt.annotation

import io.github.wongxd.simplebuskt.thread.ThreadMode
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier


class SimEventMethod(var subscriber: Any, var method: Method, var eventType: Class<*>, var code: Int, var threadMode: ThreadMode) {


    /**
     * 调用方法
     *
     * @param event 参数
     */
    operator fun invoke(event: Any) {
        try {
            if (!Modifier.isPublic(method.modifiers)) {
                method.isAccessible = true  // not public method
            }
            method.invoke(subscriber, event)

        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

}