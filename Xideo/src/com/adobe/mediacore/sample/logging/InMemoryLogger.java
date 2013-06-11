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

package com.adobe.mediacore.sample.logging;

import com.adobe.mediacore.logging.LogEntry;
import com.adobe.mediacore.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InMemoryLogger implements Logger {
    private List<LogEntry> logEntries = new ArrayList<LogEntry>();

    private int DEFAULT_MAX_ENTRY_COUNT = 1000;
    private int maxEntryCount = DEFAULT_MAX_ENTRY_COUNT;

    private Verbosity maxVerbosityLevel = Verbosity.INFO;

    private void addEntry(LogEntry logEntry) {
        if (logEntries.size() >= maxEntryCount) {
            logEntries.remove(logEntries.size() - 1);
        }

        logEntries.add(0, logEntry);
    }

    @Override
    public void setCapacity(int maxEntryCount) { this.maxEntryCount = maxEntryCount; }

    @Override
    public List<LogEntry> getEntries() {
        List<LogEntry> entries = new ArrayList<LogEntry>();
        entries.addAll(logEntries);

        return  entries;
    }

    @Override
    public void clear() { logEntries.clear(); }

    @Override
    public void i(String logTag, String message) {
        if (maxVerbosityLevel.getLevel() < Verbosity.INFO.getLevel()) return;

        addEntry(new LogEntry(now(), message, Logger.Verbosity.INFO, logTag));
        android.util.Log.i(logTag, message);
    }

    @Override
    public void d(String logTag, String message) {
        if (maxVerbosityLevel.getLevel() < Verbosity.DEBUG.getLevel()) return;

        addEntry(new LogEntry(now(), message, Logger.Verbosity.DEBUG, logTag));
        android.util.Log.d(logTag, message);
    }

    @Override
    public void w(String logTag, String message) {
        if (maxVerbosityLevel.getLevel() < Verbosity.WARN.getLevel()) return;

        addEntry(new LogEntry(now(), message, Logger.Verbosity.WARN, logTag));
        android.util.Log.w(logTag, message);
    }

    @Override
    public void e(String logTag, String message) {
        if (maxVerbosityLevel.getLevel() < Verbosity.ERROR.getLevel()) return;

        addEntry(new LogEntry(now(), message, Logger.Verbosity.ERROR, logTag));
        android.util.Log.e(logTag, message);
    }

    @Override
    public void setVerbosityLevel(Verbosity level) {
        this.maxVerbosityLevel = level;
    }

    private String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }

}
