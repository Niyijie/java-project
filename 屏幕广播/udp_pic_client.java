package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class udp_pic_client {

    private static DatagramSocket sock = null;
    private static DatagramPacket pack = null;
    private static byte[] buf = new byte[udp_pic_server.FRAME_UNIT_MAX + 14];

    private static int index = 0;

    static {
        try {
            sock = new DatagramSocket(8888);
            pack = new DatagramPacket(buf, buf.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        udp_pic_client client = new udp_pic_client();
        Map<Integer, FrameUnit> unitMap = new HashMap<Integer, FrameUnit>();
        StudentUI display = new StudentUI();

        long curFrameId = 0;

        while (true) {
            //get a frame
            FrameUnit frame = client.recieve();
            //get new frame id
            long nowFrameId = frame.getFrameId();

            if (nowFrameId == curFrameId) {
                unitMap.put(frame.getUnitOrder(), frame);
            } else if (nowFrameId > curFrameId) {
                unitMap.clear();
                curFrameId = nowFrameId;
                unitMap.put(frame.getUnitOrder(), frame);
            }

            if (unitMap.size() == frame.getUnitCount()) {
                byte[] imageData = client.getImageBytes(unitMap,frame.getUnitCount());
                System.out.println("display");
                display.updateIcon(imageData);
            }
        }
    }

    public byte[] getImageBytes(Map<Integer, FrameUnit> unitMap,int count)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (int i = 0; i < count; i++) {
                FrameUnit unit = unitMap.get(i);
                baos.write(unit.getUnitData());
            }
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
//            ImageIO.write(image, "jpg", new FileOutputStream("/Users/niyijie/Desktop/大数据/"+index+".jpg"));
//            System.out.println("saved " + index +".jpg");
//            index ++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    //get a frame unit
    public FrameUnit recieve() {
        try {
            sock.receive(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FrameUnit unit = new FrameUnit();

        byte[] frameId = new byte[8];
        byte[] unitCount = new byte[1];
        byte[] order = new byte[1];
        byte[] dataLen = new byte[4];

        System.arraycopy(buf, 0, frameId, 0, 8);
        System.arraycopy(buf, 8, unitCount, 0, 1);
        System.arraycopy(buf, 9, order, 0, 1);
        System.arraycopy(buf, 10, dataLen, 0, 4);

        byte[] unitData = new byte[Util.bytes2Int(dataLen)];
        System.arraycopy(buf, 14, unitData, 0, unitData.length);


        unit.setFrameId(Util.bytes2Long(frameId));
        unit.setUnitCount((int)unitCount[0]);
        unit.setUnitOrder((int)order[0]);
        unit.setDataLen(Util.bytes2Int(dataLen));
        unit.setUnitData(unitData);

        return unit;
    }
}

