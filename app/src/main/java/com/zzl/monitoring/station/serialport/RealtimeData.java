package com.zzl.monitoring.station.serialport;

/**
 * Create by zilu 2020/09/04
 */
public class RealtimeData {
    private final short[] channel;
    private final byte[] relay;

    public RealtimeData(int channelSize, int relaySize) {
        this.channel = new short[channelSize];
        this.relay = new byte[relaySize];
    }

    public RealtimeData(short[] channel, byte[] relay) {
        this.channel = channel;
        this.relay = relay;
    }

    public short getChannel(int index) {
        return channel[index];
    }

    public byte getRelay(int index) {
        return relay[index];
    }

    public short[] getChannel() {
        return channel;
    }

    public byte[] getRelay() {
        return relay;
    }

    public void setChannel(int index, short value) {
        this.channel[index] = value;
    }

    public void setRelay(int index, byte value) {
        this.relay[index] = value;
    }
}
