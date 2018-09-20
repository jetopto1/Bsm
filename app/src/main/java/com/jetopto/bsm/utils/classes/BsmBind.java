package com.jetopto.bsm.utils.classes;

public class BsmBind {
    private final static String TAG = BsmBind.class.getSimpleName();

    private boolean isBind = true;
    private String deviceMAC;
    private String defaultMac = "D6:44:A1:7A:BB:83";

    public boolean isBsmBind()
    {
       return isBind;
    }

    public void setBindState(boolean flag)
    {
        this.isBind = flag;
    }

    public String getDeviceMAC()
    {
        if (deviceMAC != null)
            return deviceMAC;
        else
        {
            return defaultMac;
        }
    }

    public void setBindDeviceMAC(String mac)
    {
        this.deviceMAC = mac;
    }

}
