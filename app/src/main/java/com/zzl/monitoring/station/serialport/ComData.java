package com.zzl.monitoring.station.serialport;


import com.zzl.serialport.ComBean;
import com.zzl.serialport.MyFunc;

/**
 * Create by zhongzilu 2019/05/28
 */
public class ComData extends ComBean {

    public ComData(String port, byte[] buffer, int size) {
        super(port, buffer, size);
    }

    @Override
    public String toString() {
        return MyFunc.ByteArrToHex(bRec);
    }
}
