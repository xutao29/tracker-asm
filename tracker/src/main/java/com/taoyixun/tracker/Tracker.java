package com.taoyixun.tracker;

import android.view.View;

import com.taoyixun.tracker.delegate.DefaultDelegate;
import com.taoyixun.tracker.listener.TrackerListener;

import org.jetbrains.annotations.NotNull;

/**
 * created by xutao
 * on 6/23/21
 */
public class Tracker {
    private static TrackerListener clickDelegate = new DefaultDelegate();

    public static void onClick(@NotNull View view) {
        clickDelegate.onClick(view);
    }

    public static void setClickDelegate(TrackerListener clickDelegate) {
        Tracker.clickDelegate = clickDelegate;
    }
}
