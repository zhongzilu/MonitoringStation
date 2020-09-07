package com.zzl.monitoring.station.serialport;

/**
 * Create by zilu 2020/09/03
 */
public class Packet {
    private static final String TAG = "Packet-->";

    /**
     * 获取读取实时环境数据的命令
     *
     * @return byte[]
     */
    public static byte[] getRealtimeDateCMD() {
        return new byte[]{0x01, 0x03, 0x00, 0x00, (byte) 0xF1, (byte) 0xD8};
    }

    /**
     * 获取读取气象站地址的命令
     *
     * @return byte[]
     */
    public static byte[] getServerAddressCMD() {
        return new byte[]{0x00, 0x20, 0x00, 0x68};
    }

    /**
     * 校验数据包
     *
     * @param data XPH协议数据包
     * @return 是否校验通过
     */
    public static boolean verifyPack(byte[] data) {
        int len = data.length;
        short flag = CRC16(data, len - 2);
        short target = (short) ((data[len - 1] << 8) | (data[len - 2] & 0xff));
        return flag == target;
    }

    /**
     * 解析实时环境数据
     *
     * @param data 响应数据包
     */
    public static RealtimeData parseRealtimeResp(byte[] data) throws IllegalArgumentException {
        if (!verifyPack(data)) {
            throw new IllegalArgumentException("verify data not match.");
        }

        if (data.length < 70) {
            throw new IllegalArgumentException("data length not match.");
        }

        RealtimeData res = new RealtimeData(16, 32);

        //channel
        byte index = 0;
        short temp;
        for (int i = 4; i < 36; i += 2) {
            temp = (short) ((data[i] << 8) | (data[i + 1] & 0xff));
            res.setChannel(index, temp == 32767 ? 0 : temp);
            index++;
        }

        //relay
        System.arraycopy(data, 36, res.getRelay(), 0, 32);

        return res;
    }

    /**
     * CRC16数据校验
     *
     * @param bytes 数据包
     * @param len   数据长度
     * @return 校验码
     */
    public static short CRC16(byte[] bytes, int len) {
        char CRC16Lo, CRC16Hi, CL, CH, SaveHi, SaveLo;
        int Flag;
        CRC16Lo = 0xFF;
        CRC16Hi = 0xFF;
        CL = 0x01;
        CH = 0xA0;
        for (int i = 0; i < len; i++) {
            CRC16Lo ^= (bytes[i] & 0xff); //每一个数据与 CRC 寄存器进行异或
            for (Flag = 0; Flag < 8; Flag++) {
                SaveHi = CRC16Hi;
                SaveLo = CRC16Lo;
                CRC16Hi >>= 1;
                CRC16Lo >>= 1; //高位右移一位，低位右移一位
                if ((SaveHi & 0x01) == 0x01) //如果高位字节最后一位为 1
                    CRC16Lo |= 0x80; //则低位字节右移后前面补 1 否则自动补 0
                if ((SaveLo & 0x01) == 0x01) //如果 LSB 为 1，则与多项式码进行异或
                {
                    CRC16Hi ^= CH;
                    CRC16Lo ^= CL;
                }
            }
        }

        return (short) ((CRC16Hi << 8) | CRC16Lo);
    }
}
