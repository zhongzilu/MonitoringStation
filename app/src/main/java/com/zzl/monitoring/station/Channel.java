package com.zzl.monitoring.station;

/**
 * Create by zilu 2020/09/04
 */
interface Channel {
    //风速
    int WIND_SPEED = 0;
    //气温
    int TEMPERATURE = 2;
    //气压
    int PRESSURE = 4;
    //紫外线
    int U_RAY = 5;
    //风向
    int WIND_DIR = 6;
    //大气湿度
    int MOISTURE = 8;
    //简易总辐射
    int SIMPLE_TOTAL_RADIATION = 11;
    //辐射累计
    int RADIATION_TOTAL = 13;
    //PM2.5
    int PM2_5 = 14;
    //PM10
    int PM10 = 15;
}
