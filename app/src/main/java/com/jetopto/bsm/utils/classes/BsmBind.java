package com.jetopto.bsm.utils.classes;

public class BsmBind {
    private final static String TAG = BsmBind.class.getSimpleName();

    private String deviceMac;

    public boolean isBsmBind() {
        return null != deviceMac;
    }

    public String getDeviceMac() {
        return (null != deviceMac) ? deviceMac : "";
    }

    public void setBindDeviceMAC(String mac) {
        this.deviceMac = mac;
    }

    @Override
    public String toString() {
        return "deviceMac: " + deviceMac + ", isBind(" + isBsmBind() + ")";
    }
}
