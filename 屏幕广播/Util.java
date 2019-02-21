package com.company;

import sun.jvm.hotspot.runtime.Bytes;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class Util {

    private static Robot robot;


    static {
        try{
            robot = new Robot();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)
    {
        long frameId = System.currentTimeMillis();
        System.out.println(frameId);
    }

    public static byte[] getScreenCapture()
    {
            Rectangle rect = new Rectangle(0,0,1280,800);
            BufferedImage image = robot.createScreenCapture(rect);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpg", baos);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return baos.toByteArray();
    }

    public static byte[] int2Bytes(int n)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) n;
        bytes[1] = (byte) (n >> 8);
        bytes[2] = (byte) (n >> 16);
        bytes[3] = (byte) (n >> 24);
        return bytes;
    }

    public static int bytes2Int(byte[] bytes)
    {
        return bytes[0] & 0xFF|
                (bytes[1] & 0xFF) << 8|
                (bytes[2] & 0xFF) << 16|
                (bytes[3] & 0xFF) << 24;
    }

    public static byte[] long2Bytes(long n)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) n;
        bytes[1] = (byte) (n >> 8);
        bytes[2] = (byte) (n >> 16);
        bytes[3] = (byte) (n >> 24);
        bytes[4] = (byte) (n >> 32);
        bytes[5] = (byte) (n >> 40);
        bytes[6] = (byte) (n >> 48);
        bytes[7] = (byte) (n >> 56);
        return bytes;
    }

    public static long bytes2Long(byte[] bytes)
    {
        return (long)(bytes[0] & 0xFF)|
                (long)(bytes[1] & 0xFF) << 8 |
                (long)(bytes[2] & 0xFF) << 16 |
                (long)(bytes[3] & 0xFF) << 24 |
                (long)(bytes[4] & 0xFF) << 32 |
                (long)(bytes[5] & 0xFF) << 40 |
                (long)(bytes[6] & 0xFF) << 48 |
                (long)(bytes[7] & 0xFF) << 56;
    }

}
