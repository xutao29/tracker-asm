package com.taoyixun.tracker.util

import android.view.View
import android.view.ViewGroup
import android.widget.TextView

object ViewUtils {

    fun View.getDescription(): String? {
        return when (this) {
            is TextView -> {
                text.toString()
            }
            else -> null
        }
    }

    fun View.getAbsolutePath(): String {
        if (parent == null) {
            return "rootView"
        }
        var path = ""
        var temp = this
        while (temp.parent != null && temp.parent is View) {
            var index = 0
            try {
                index = indexOfChild(temp.parent as ViewGroup, temp)
            } catch (e: Exception) {
            }
            path = "${temp.javaClass.simpleName}[$index]/${path}"
            temp = temp.parent as View
        }
        return path
    }

    private fun indexOfChild(parent: ViewGroup?, child: View): Int {
        if (parent == null) {
            return 0
        }
        val count = parent.childCount
        var j = 0
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            if (child.javaClass.isInstance(view)) {
                if (view === child) {
                    return j
                }
                j++
            }
        }
        return -1
    }
}