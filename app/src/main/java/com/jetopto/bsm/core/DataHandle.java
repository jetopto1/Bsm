package com.jetopto.bsm.core;

import android.util.Log;

public class DataHandle {

    private final static String TAG = DataHandle.class.getSimpleName();

    public StringBuilder bytesToHex(byte[] bytes) {
        Log.d(TAG, "dumpHex()");
        final StringBuilder strResult = new StringBuilder();
        for (int i=0; i<bytes.length; i++)
            strResult.append(String.format("%02X ", bytes[i]));
        return strResult;
    }

    public StringBuilder byteToHex(byte data[], int start, int length) {
        Log.d(TAG, String.format("dumpHex() start=%d, length=%d",start,length));
        final StringBuilder strResult = new StringBuilder();
        for (int i=start; i<start+length; i++)
            strResult.append(String.format("%02X", data[i]));
        return strResult;
    }

    public StringBuilder byteArrayToHex(byte data[], int start, int length) {
        Log.d(TAG, String.format("dumpHex() start=%d, length=%d",start,length));
        final StringBuilder strResult = new StringBuilder();
        for (int i=start; i<start+length; i++)
            strResult.append(String.format("%02X ", data[i]));
        return strResult;
    }
}
