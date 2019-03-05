package io.github.wongxd.simplebuse

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.github.wongxd.simplebuse.event.ActivityEvent
import io.github.wongxd.simplebuse.event.ServiceEvent
import io.github.wongxd.simplebuskt.DefaultEvent
import io.github.wongxd.simplebuskt.DefaultObserver
import io.github.wongxd.simplebuskt.SimBusKt
import io.github.wongxd.simplebuskt.annotation.SimEvent
import io.github.wongxd.simplebuskt.observer.IObserver
import io.github.wongxd.simplebuskt.thread.ThreadMode
import kotlinx.android.synthetic.main.aty_main.*


class AtyMainActivity : AppCompatActivity() {
    val TAG = "AtyMainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aty_main)


        val observer: IObserver<DefaultEvent> = DefaultObserver { event, code -> Log.e(TAG, " DefaultEvent  onMessageReceived    $code") }

        SimBusKt.getInstance().register(EventCode.NORMAL_EVENT, observer = observer)


        btn_nomal.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(code = EventCode.NORMAL_EVENT)
        }

        btn_remove_nomal.setOnClickListener {
            SimBusKt.getInstance().unregister(EventCode.NORMAL_EVENT, observer = observer)
        }


        btn_send_aty.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(EventCode.ACTIVITY_EVENT, ActivityEvent("发送消息"))
        }

        btn_cancle_aty.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(EventCode.OTHER_ACTIVITY_EVENT, ActivityEvent("发送消息", isCancleSimEvent = true))
        }




        btn_send.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(EventCode.SERVICE_EVENT, ServiceEvent("发送消息"))
        }

        btn_kill_bymsg.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(EventCode.SERVICE_EVENT, ServiceEvent("杀死服务", true))
        }

        btn_cancle_bymsg.setOnClickListener {
            SimBusKt.getInstance().notifyObservers(EventCode.OTHER_SERVICE_EVENT, ServiceEvent("取消总线", false, true))
        }

        val intent = Intent(this, MyService::class.java)
        btn_start.setOnClickListener {

            startService(intent)
        }


        btn_kill.setOnClickListener {
            try {
                stopService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        SimBusKt.getInstance().registerClass(this)
        Log.e(TAG, " AtyMainActivity  onCreate")


    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    fun isServiceWork(mContext: Context, serviceName: String): Boolean {
        var isWork = false
        val myAM = mContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList = myAM.getRunningServices(100)
        if (myList.size <= 0) {
            return false
        }
        val size = myList.size
        for (i in 0 until size) {
            val mName = myList[i].service.className.toString()
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }


    @SimEvent(code = EventCode.ACTIVITY_EVENT, threadMode = ThreadMode.IO)
    fun simEventTest(event: ActivityEvent) {
        Log.e(TAG, "simEventTest ${event.msg}     ${System.currentTimeMillis()}")
    }


    @SimEvent(code = EventCode.OTHER_ACTIVITY_EVENT, threadMode = ThreadMode.IO)
    fun simEventOtherTest(event: ActivityEvent) {
        Log.e(TAG, "simEventOtherTest  OTHER_ACTIVITY_EVENT  ${event.msg}     ${System.currentTimeMillis()}")

        if (event.isCancleSimEvent) {
            SimBusKt.getInstance().unregisterClass(this)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        SimBusKt.getInstance().unregisterClass(this)
        Log.e(TAG, " AtyMainActivity  onDestroy")
    }
}
