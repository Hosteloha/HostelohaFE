package com.hosteloha.app.log;

import android.util.Log;

public class HostelohaLog {

    private static final String TAG = HostelohaLog.class.getSimpleName();

    public static void debugOut(String msg) {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        Log.i(TAG, " " + trace[1].getFileName() + " (" + trace[1].getLineNumber() + ")" + trace[1].getMethodName() + "  =======> :: " + msg);
    }
}