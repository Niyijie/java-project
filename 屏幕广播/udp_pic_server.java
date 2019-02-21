package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class udp_pic_server {


    public final static int FRAME_UNIT_MAX = 60*1024;

    private static DatagramSocket sock = null;

    static {
        try{
            sock = new DatagramSocket(9999);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String args[])
    {
        udp_pic_server server = new udp_pic_server();
        while (true)
            server.sendUnit();
    }


    /*
     * 发送图片
     */
    public void sendUnit()
    {
        //抓一张图片
        byte[] frame = Util.getScreenCapture();

        //分割图片得到存有帧单元的容器
        List<FrameUnit> FrameList = splitUnit(frame);

        try {
            for (FrameUnit unit : FrameList) {
                //得到时间帧和帧总数和数据编号和数据长度和帧数据
                long frameId = unit.getFrameId();
                int unitCount = unit.getUnitCount();
                int unitOrder = unit.getUnitOrder();
                int dataLen = unit.getDataLen();
                byte[] unitData = unit.getUnitData();

                //8 + 1 + 1 + 4
                byte[] bytes = new byte[dataLen + 14];
                //写入时间帧编号
                System.arraycopy(Util.long2Bytes(frameId),0,bytes,0,8);
                //写入帧总数
                System.arraycopy(Util.int2Bytes(unitCount),0,bytes,8,1);
                //写入编号
                System.arraycopy(Util.int2Bytes(unitOrder), 0, bytes, 9, 1);
                //写入数据长度
                System.arraycopy(Util.int2Bytes(dataLen), 0, bytes, 10, 4);
                //写入数据
                System.arraycopy(unitData, 0, bytes, 14, dataLen);

                DatagramPacket pack = new DatagramPacket(bytes, bytes.length);
                pack.setSocketAddress(new InetSocketAddress("localhost", 8888));
                sock.send(pack);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(FrameList.size());
    }

    /*
     * 切割图片
     */
    public List<FrameUnit> splitUnit(byte[] frame)
    {
        List<FrameUnit> FrameList = new ArrayList<>();

        //得到时间帧Id
        long frameId = System.currentTimeMillis();

        //计算unitCount
        int unitCount = frame.length / FRAME_UNIT_MAX;
        //
        if(frame.length % FRAME_UNIT_MAX == 0)
        {
            for(int i = 0;i < unitCount ;i++)
            {
                FrameUnit unit = new FrameUnit();
                unit.setFrameId(frameId);
                unit.setUnitCount(unitCount);
                unit.setUnitOrder(i);
                unit.setDataLen(FRAME_UNIT_MAX);
                byte[] frameUnit = new byte[FRAME_UNIT_MAX];
                System.arraycopy(frame,i*FRAME_UNIT_MAX,frameUnit,0,FRAME_UNIT_MAX);
                unit.setUnitData(frameUnit);
                FrameList.add(unit);
            }
        }
        else
        {
            unitCount = unitCount + 1;
            for(int i = 0;i < unitCount ;i++)
            {
                FrameUnit unit = new FrameUnit();
                unit.setFrameId(frameId);
                unit.setUnitCount(unitCount);
                unit.setUnitOrder(i);
                byte[] frameUnit = new byte[FRAME_UNIT_MAX];

                if(i == unitCount - 1)
                {
                    unit.setDataLen(frame.length % FRAME_UNIT_MAX);
                    System.arraycopy(frame,i*FRAME_UNIT_MAX,frameUnit,0,unit.getDataLen());
                }
                else
                {
                    unit.setDataLen(FRAME_UNIT_MAX);
                    System.arraycopy(frame,i*FRAME_UNIT_MAX,frameUnit,0,FRAME_UNIT_MAX);
                }
                unit.setUnitData(frameUnit);
                FrameList.add(unit);
            }
        }
        return FrameList;
    }
}
