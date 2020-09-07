package com.zzl.monitoring.station;

import com.zzl.monitoring.station.serialport.Packet;
import com.zzl.monitoring.station.serialport.RealtimeData;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ExampleUnitTest {

    @Test
    public void test() {
        byte source = (byte) 0xD8;
        short target = 0xD8;
        System.out.println(source);
        System.out.println(source & 0xff);

        Assert.assertEquals(target, source & 0xff);
    }

    @Test
    public void addition_isCorrect() {
        byte[] source = new byte[]{0x01, 0x03, 0x00, 0x00, (byte) 0xF1, (byte) 0xD8};
        int len = source.length;
        short target = (short) ((source[len - 1] << 8) | (source[len - 2] & 0xff));
        System.out.println(target);

        short res = Packet.CRC16(source, len - 2);
        System.out.println(res);

        Assert.assertEquals((byte) 0xF1, (byte) res);
        Assert.assertEquals((byte) 0xD8, (byte) (res >> 8));

        Assert.assertEquals(target, res);
    }

    @Test
    public void testGetRealtimeDateCMD() {
        System.out.println(Arrays.toString(Packet.getRealtimeDateCMD()));
    }

    @Test
    public void testParseRealtimeData() {
        byte[] source = new byte[]{
                0x01, 0x03, 0x00, 0x40, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF,
                0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F,
                (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF,
                0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x7F, (byte) 0xFF, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x6B, (byte) 0xDA
        };

        short[] targetChannel = new short[]{
                32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767, 32767
        };

        byte[] targetRelay = new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00
        };

        RealtimeData data = Packet.parseRealtimeResp(source);

        Assert.assertArrayEquals(targetRelay, data.getRelay());
        Assert.assertArrayEquals(targetChannel, data.getChannel());
    }

//    private short CRC16(byte[] p) {
//        char CRC16Lo, CRC16Hi, CL, CH, SaveHi, SaveLo;
//        int Flag;
//        CRC16Lo = 0xFF;
//        CRC16Hi = 0xFF;
//        CL = 0x01;
//        CH = 0xA0;
//        for (byte b : p) {
//            CRC16Lo ^= (b & 0xff); //每一个数据与 CRC 寄存器进行异或
//            for (Flag = 0; Flag < 8; Flag++) {
//                SaveHi = CRC16Hi;
//                SaveLo = CRC16Lo;
//                CRC16Hi >>= 1;
//                CRC16Lo >>= 1; //高位右移一位，低位右移一位
//                if ((SaveHi & 0x01) == 0x01) //如果高位字节最后一位为 1
//                    CRC16Lo |= 0x80; //则低位字节右移后前面补 1 否则自动补 0
//                if ((SaveLo & 0x01) == 0x01) //如果 LSB 为 1，则与多项式码进行异或
//                {
//                    CRC16Hi ^= CH;
//                    CRC16Lo ^= CL;
//                }
//            }
//        }
//
//        return (short) ((CRC16Hi << 8) | CRC16Lo);
//    }
}