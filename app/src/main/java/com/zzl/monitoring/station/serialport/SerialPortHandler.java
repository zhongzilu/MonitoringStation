package com.zzl.monitoring.station.serialport;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zzl.serialport.IComDataHandler;
import com.zzl.serialport.MyFunc;

/**
 * Serial Port data handler
 * Create by zhongzilu 2019/05/27
 */
public class SerialPortHandler implements IComDataHandler {

    private String TAG = "SerialPortHandler-->";

    private Callback mCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public SerialPortHandler(Callback callback) {
        this.mCallback = callback;
    }

    public SerialPortHandler() {
    }

    @Override
    public void handleData(final String com, final byte[] data, int size) {
        if (data.length == 0) return;

        if (!Packet.verifyPack(data)){
            Log.e("-->", MyFunc.ByteArrToHex(data));
            return;
        }

        //copy a new data byte array
//        final byte[] copyData = new byte[size];
//        System.arraycopy(data, 0, copyData, 0, size);

        if (mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onReceivedData(new ComData(com, data, data.length));
                }
            });
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onReceivedData(ComData bean);
    }
}
