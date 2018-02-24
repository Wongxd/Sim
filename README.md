# SimpleBus
一个轻量的事件总线    A lightweight event bus


###GET IT

[![](https://jitpack.io/v/Wongxd/SimpleBus.svg)](https://jitpack.io/#Wongxd/SimpleBus)

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}



Step 2. Add the dependency

	dependencies {
	        compile 'com.github.Wongxd:SimpleBus:0.0.1'
	}



### USE IT

Setp 1. 创建一个事件类

	data class ActivityEvent(val info: String = "info", val isKillSelf:Boolean =false, val isCancleSimEvent:Boolean =false){ val msg = "ActivityEvent 消息  ----  $info"}

Setp 1. 发送事件

 	SimBusKt.getInstance().notifyObservers(EventCode.ACTIVITY_EVENT, ActivityEvent("发送消息"))


Setp 3. 在需要的地方接收事件

	//首先注册这个类为一个接收者
	SimBusKt.getInstance().registerClass(this)
   	
	//用注解来标注一个参数为 特定事件 的方法
  	@SimEvent(code = EventCode.OTHER_ACTIVITY_EVENT, threadMode = ThreadMode.IO)

    fun simEventOtherTest(event: ActivityEvent) {

        Log.e(TAG, "simEventOtherTest  OTHER_ACTIVITY_EVENT  ${event.msg}     ${System.currentTimeMillis()}")



        if (event.isCancleSimEvent) {

            SimBusKt.getInstance().unregisterClass(this)

        }

    }


	//在不需要接收事件时，取消注册
	SimBusKt.getInstance().unregisterClass(this)




