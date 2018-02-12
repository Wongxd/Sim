package io.github.wongxd.simplebuskt.annotation

import io.github.wongxd.simplebuskt.thread.ThreadMode


/**
 * Created by wongxd on 2018/2/10.
 */

@kotlin.annotation.MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SimEvent(val code: Int = -1, val threadMode: ThreadMode = ThreadMode.MAIN)