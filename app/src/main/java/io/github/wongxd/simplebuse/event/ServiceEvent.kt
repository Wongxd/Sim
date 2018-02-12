package io.github.wongxd.simplebuse.event

/**
 * Created by wongxd on 2018/2/9.
 */
data class ServiceEvent(val info: String = "info",val isKillService:Boolean =false,val isCancleSimEvent:Boolean =false){

    val msg = "ServiceEvent消息  ----  $info"


}