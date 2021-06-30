package com.taoyixun.tracker.delegate

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import com.taoyixun.tracker.bean.TrackerBean
import com.taoyixun.tracker.bean.TrackerType
import com.taoyixun.tracker.listener.TrackerListener
import com.taoyixun.tracker.util.TagHelper.trackerTag
import com.taoyixun.tracker.util.ViewUtils.getAbsolutePath
import com.taoyixun.tracker.util.ViewUtils.getDescription
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class BaseTrackerDelegate : TrackerListener {

    private val actionWhatCode = 100

    private val executor: ExecutorService by lazy {
        Executors.newCachedThreadPool()
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == actionWhatCode && (msg.obj is TrackerBean)) {
                action(msg.obj as TrackerBean)
            }
        }
    }

    override fun onClick(view: View) {
        executor.execute {
            view.context?.let {
                val absolutePath = view.getAbsolutePath()
                val trackerTag = view.trackerTag
                val description = view.getDescription()
                handler.sendMessage(Message.obtain().apply {
                    obj = TrackerBean(
                        TrackerType.CLICK,
                        trackerTag,
                        absolutePath,
                        description
                    )

                    what = actionWhatCode
                })
            }
        }
    }

    abstract fun action(data: TrackerBean)
}