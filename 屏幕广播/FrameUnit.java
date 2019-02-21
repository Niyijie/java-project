package com.company;

public class FrameUnit {

    //时间帧编号
    private long frameId;

    //切割帧后的总单元数
    private int unitCount;

    //切割帧后的编号
    private int unitOrder;

    //数据长度
    private int dataLen;

    //要传送的数据
    private byte[] unitData;

    public void setFrameId(long n)
    {
        frameId = n;
    }

    public void setUnitCount(int n)
    {
        unitCount = n;
    }

    public void setUnitOrder(int n)
    {
        unitOrder = n;
    }

    public void setUnitData(byte[] bytes)
    {
        unitData = bytes;
    }

    public void setDataLen(int n)
    {
        dataLen = n;
    }

    public long getFrameId() {
        return frameId;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public int getUnitOrder() {
        return unitOrder;
    }

    public byte[] getUnitData() {
        return unitData;
    }

    public int getDataLen()
    {
        return dataLen;
    }
}
