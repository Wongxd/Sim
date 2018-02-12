package io.github.wongxd.simplebuse.event

/**
 * Created by wongxd on 2018/2/9.
 */
data class ActivityEvent(val info: String = "info", val isKillSelf:Boolean =false, val isCancleSimEvent:Boolean =false){

    val msg = "ActivityEvent 消息  ----  $info"


}