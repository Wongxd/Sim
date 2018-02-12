package io.github.wongxd.simplebuse

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.github.wongxd.simplebuse.event.ServiceEvent
import io.github.wongxd.simplebuskt.SimBusKt
import io.github.wongxd.simplebuskt.annotation.SimEvent

class MyService : Service() {
    val TAG = MyService::class.java.simpleName

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        SimBusKt.getInstance().registerClass(this)
        Log.e(TAG, " MyService  onCreate")
    }


    @SimEvent(code = EventCode.SERVICE_EVENT)
    fun simEventTest(event: ServiceEvent) {
        Log.e(TAG, "simEventTest ${event.msg}     ${System.currentTimeMillis()}")
        if (event.isKillService) {
            this.stopSelf()
        }

    }


    @SimEvent(code = EventCode.OTHER_SERVICE_EVENT)
    fun simEventOtherTest(event: ServiceEvent) {
        Log.e(TAG, "simEventTest  OTHER_SERVICE_EVENT  ${event.msg}     ${System.currentTimeMillis()}")

        if (event.isCancleSimEvent) {
            SimBusKt.getInstance().unregisterClass(this)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        SimBusKt.getInstance().unregisterClass(this)
        Log.e(TAG, " MyService  onDestroy")
    }
}
