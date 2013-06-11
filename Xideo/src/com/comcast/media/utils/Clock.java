/*************************************************************************
*
* ADOBE CONFIDENTIAL
* ___________________
*
*  Copyright 2012 Adobe Systems Incorporated
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of Adobe Systems Incorporated and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to Adobe Systems Incorporated and its
* suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from Adobe Systems Incorporated.
**************************************************************************/

package com.comcast.media.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class Clock {
    private final long interval;
    private final String name;
    private boolean isRunning;

    private final List<ClockEventListener> clockEventListeners = new ArrayList<ClockEventListener>();

    private Timer timer;

    public interface ClockEventListener {
        void onTick(String name);
    }

    public void addClockEventListener(ClockEventListener listener) { clockEventListeners.add(listener); }
    public void removeClockEventListener(ClockEventListener listener) { clockEventListeners.remove(listener); }

    private void dispatchClockEvent() {
        for (ClockEventListener listener : clockEventListeners) {
            listener.onTick(name);
        }
    }

    public Clock(String name, long interval) {
        this.name = name;
        this.interval = interval;
    }

    public void start() {
    	if (isRunning)
    		return;
    	isRunning = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dispatchClockEvent();
            }
        }, 0, interval);
    }

    public void stop() {
    	isRunning = false;
        timer.cancel();
    }
    
}
