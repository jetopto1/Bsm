package com.jetopto.bsm.core;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jetopto.bsm.core.listener.ISensorStateListener;
import com.jetopto.bsm.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorMonitorService extends Service {

    private static final String TAG = SensorMonitorService.class.getSimpleName();


    private List<ISensorStateListener> mListenerList = new ArrayList<>();
    private SensorMonitorBinder mBinder = new SensorMonitorBinder();
    private Handler mHandler = new Handler();

    public SensorMonitorService() {
        super();
    }

    //BLE
    private BluetoothAdapter mBTAdapter;
    private static final long SCAN_PERIOD = 1000 * 60 * 30;
    private static final int MaxDeviceCount = 500;
    private final String BLE_MAC = "D6:44:A1:7A:BB:83";
    private final String BLE_NAME = "HC-42";

    class deviceInfo {
        public String Name;
        public String Address;
        public Integer RSSI;
        public int Type,BondState;
        public byte[] scanRecord;
    }

    private deviceInfo[] scanDevice=new deviceInfo[MaxDeviceCount];
    private Integer scanIndex=0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        final BluetoothManager bluetoothManager =
//                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//
//        mBTAdapter = bluetoothManager.getAdapter();
//        mHandler= new Handler();
//
//        if (mBTAdapter.isEnabled()) {
//            scanLeDevice(true);
//        } else {
//            Log.e(TAG, "Please turn on Bluetooth");
//        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBTAdapter = bluetoothManager.getAdapter();
        mHandler= new Handler();

        if (mBTAdapter.isEnabled()) {
            scanLeDevice(true);
        } else {
            Log.e(TAG, "Please turn on Bluetooth");
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        scanLeDevice(false);
    }

    public void registerListener(ISensorStateListener listener) {
        mListenerList.add(listener);
    }

    public void unRegisterListener(ISensorStateListener listener) {

        scanLeDevice(false);
        if (!mListenerList.isEmpty() && mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }


    public void monitorSensor() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO remove these shit, do the right thing
                Random random = new Random();
                int level = random.nextInt(3) + 1;
                String rState = "safety";
                String lState = "safety";
                switch (level) {
                    case 1:
                        lState = "safety";
                        rState = "safety";
                        break;
                    case 2:
                        lState = "urgent";
                        rState = "urgent";
                        break;
                    case 3:
                        lState = "emergent";
                        rState = "emergent";
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.LEFT_SENSOR_LEVEL, lState);
                bundle.putString(Constant.RIGHT_SENSOR_LEVEL, rState);
                updateListener(bundle);
                mHandler.postDelayed(this, 2000);
            }
        }, 1000);
    }

    /*
     * Rada indicator
     * 00 LR :GG 0
     * 01 LR :GY 1
     * 02 LR :GR 2
     * 10    :YG 16
     * 11    :YY 17
     * 12    :YR 18
     * 20    :RG 32
     * 21    :RY 33
     * 22    :RR 34
     * */
    private void updateSensorStatus(int i) {

        String rState = "safety";
        String lState = "safety";

        switch (i) {
            case 0:
                lState = "safety";
                rState = "safety";
                break;
            case 1:
                lState = "safety";
                rState = "urgent";
                break;
            case 2:
                lState = "safety";
                rState = "emergent";
                break;
            case 16:
                lState = "urgent";
                rState = "safety";
                break;
            case 17:
                lState = "urgent";
                rState = "urgent";
                break;
            case 18:
                lState = "urgent";
                rState = "emergent";
                break;
            case 32:
                lState = "emergent";
                rState = "safety";
                break;
            case 33:
                lState = "emergent";
                rState = "urgent";
                break;
            case 34:
                lState = "emergent";
                rState = "emergent";
                break;
            default:
                lState = "safety";
                rState = "safety";
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constant.LEFT_SENSOR_LEVEL, lState);
        bundle.putString(Constant.RIGHT_SENSOR_LEVEL, rState);
        updateListener(bundle);


    }

    private void updateListener(Bundle bundle) {
        for (ISensorStateListener listener : mListenerList) {
            listener.onStateChanged(bundle);
        }
    }

    public class SensorMonitorBinder extends Binder {
        public SensorMonitorBinder() {
            super();
        }

        public SensorMonitorService getService() {
            return SensorMonitorService.this;
        }
    }

    private void scanLeDevice(final boolean enable) {

        if (enable) {
            Log.e(TAG, "BLE scanning .......");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SensorMonitorService.this.getApplicationContext(), "Scan stop!", Toast.LENGTH_SHORT).show();
                    mBTAdapter.stopLeScan(mLeScanCallback); // API 19 method
                    SensorMonitorService.this.scanLeDevice(true);
                }
            },SCAN_PERIOD);

            mBTAdapter.startLeScan(mLeScanCallback); // API 19 method
        } else {
            mBTAdapter.stopLeScan(mLeScanCallback);

        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

                    if (scanIndex == MaxDeviceCount) scanIndex = 0;

                    if (bluetoothDevice.getAddress().equals((BLE_MAC))) {

                        final String deviceName = bluetoothDevice.getName();
                        scanDevice[scanIndex] = new deviceInfo();
                        if (deviceName != null && deviceName.length() > 0) {
                            scanDevice[scanIndex].Name = deviceName;
                        } else {
                            scanDevice[scanIndex].Name = "unknown device";
                        }
                        scanDevice[scanIndex].Address = bluetoothDevice.getAddress();
                        scanDevice[scanIndex].RSSI = rssi;
                        scanDevice[scanIndex].Type = bluetoothDevice.getType();
                        scanDevice[scanIndex].BondState = bluetoothDevice.getBondState();

                        // Search for actual packet length
                        int packetLength = 0;
                        while (scanRecord[packetLength] > 0 && packetLength < scanRecord.length) {
                            packetLength += scanRecord[packetLength] + 1;
                        }
                        scanDevice[scanIndex].scanRecord = new byte[packetLength];
                        System.arraycopy(scanRecord, 0, scanDevice[scanIndex].scanRecord, 0, packetLength);
                        Log.d(TAG, String.format("*** Scan Index=%d, Name=%s, Address=%s, RSSI=%d, scan result length=%d",
                                scanIndex, scanDevice[scanIndex].Name, scanDevice[scanIndex].Address, scanDevice[scanIndex].RSSI, packetLength));

                        //Parsing BSM UUID
                        if (/*scanDevice[scanIndex].Name.equals(BLE_NAME) && */scanDevice[scanIndex].Address.equals(BLE_MAC)) {
                            Log.d(TAG, "Detect BSM iBecon !!");
                            SensorMonitorService.this.BsmDataParsing(scanIndex);
                        }

                        scanIndex++;
                    }

                }
            };

    private void BsmDataParsing(int i) {

        switch(scanDevice[i].Type) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                Log.d(TAG,"Classic");
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                Log.d(TAG,"BLE");
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                Log.d(TAG,"Dual");
                break;
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                Log.d(TAG,"Ukonown");
                break;
            default:
                Log.d(TAG,"Not defined");
                break;

        }

        final StringBuilder strScanResult = new DataHandle().bytesToHex(scanDevice[i].scanRecord);
        Log.d(TAG, String.format("scan result %s", strScanResult.toString()));

        // Explain the scan  result, see BLUETOOTH SPECIFICATION Version 4.0 [Vol 3]
        // Part C. Generic Access Profile. Section 18 -  APPENDIX C
        final StringBuilder strEIR = new StringBuilder();
        int p=0;

        while (p<scanDevice[i].scanRecord.length) {
            byte fieldLength = scanDevice[i].scanRecord[p];
            byte fieldType =  scanDevice[i].scanRecord[p+1];
            Integer  uuid16;
            Log.d(TAG, String.format(" scan field ptr=%d, length=%d, type = 0x%02X", p, fieldLength, fieldType));
            switch (fieldType) {
                case (byte)0xff:
                    strEIR.append("[0xFF] Manufacturer Specific Data: 0x").append(new DataHandle().byteArrayToHex(scanDevice[i].scanRecord, p + 2, fieldLength - 1)).append("\n");
                    int adCompanyID = (scanDevice[i].scanRecord[p+2]&0xFF) | ((scanDevice[i].scanRecord[p+3]&0xFF)<<8);
                    strEIR.append(String.format("     Company ID: 0x%04X (%s)\n", adCompanyID, "JET"/*CompanyIDs.lookup(adCompanyID,"Unknown")*/));
                    if (fieldLength>3) { //more data
                        byte adDataType = scanDevice[i].scanRecord[p + 4];
                        strEIR.append(String.format("     Data Type: 0x%02X\n", adDataType));
                        switch (adDataType) {
                            case 2: //iBeacon
                                byte adDataLen = scanDevice[i].scanRecord[p + 5];
                                //strEIR.append(String.format("     Data Length: %02X\n", adDataLen));
                                if (adDataLen == 0x15) {
                                    switch (adCompanyID) {
                                        case 0x4C:
                                            strEIR.append("     [i-Beacon]\n ");
                                            break;
                                        case 0x59:
                                            strEIR.append("     [nRF-Beacon]\n ");
                                            break;
                                        default:
                                            strEIR.append("     [Beacon]\n ");
                                            break;
                                    }

                                    strEIR.append("          UUID: 0x").append(new DataHandle().byteArrayToHex(scanDevice[i].scanRecord, p + 6, 8)).append("\n");
                                    strEIR.append("                        ").append(new DataHandle().byteArrayToHex(scanDevice[i].scanRecord, p + 6 + 8, 8)).append("\n");
                                    // Update sensor state
                                    updateSensorStatus(Integer.parseInt(new DataHandle().byteToHex(scanDevice[i].scanRecord, 24, 1).toString(), 16));

                                    Log.e(TAG," color = " + Integer.parseInt(new DataHandle().byteToHex(scanDevice[i].scanRecord, 24, 1).toString(), 16));


                                    int adMajor = ((scanDevice[i].scanRecord[p + 22] & 0xFF) << 8 | (scanDevice[i].scanRecord[p + 23] & 0xFF));
                                    int adMinor = ((scanDevice[i].scanRecord[p + 24] & 0xFF) << 8 | (scanDevice[i].scanRecord[p + 25] & 0xFF));
                                    int adCalcPower = scanDevice[i].scanRecord[p + 26];
                                    strEIR.append(String.format("          Major: %d\n", adMajor));
                                    strEIR.append(String.format("          Minor: %d\n", adMinor));
                                    strEIR.append(String.format("          Calibration Power: %d dBm\n ", adCalcPower));
                                } else {
                                    strEIR.append(String.format("     ? Invalid data length for iBeacon: 0x%02X\n", adDataLen));
                                }
                                break;

                            default:
                                strEIR.append(String.format("     ? Unrecognized type: 0x%02X\n", adDataType));
                                break;
                        }
                    }
                    break;

                default:
                    strEIR.append(String.format("[0x%02X] Unrecognized EIR type: 0x",fieldType)).append(new DataHandle().byteArrayToHex(scanDevice[i].scanRecord, p, fieldLength - 1)).append("\n");
                    break;
            } // end switch (field type)
            p += (fieldLength+1);

        } //end while

        Log.e(TAG, strEIR.toString());
    }
}
