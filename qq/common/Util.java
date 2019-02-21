package common;

import client.QQClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Util {

    public static void main(String[] args)
    {
        long frameId = System.currentTimeMillis();
        System.out.println(frameId);
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

    public static String getSockInfo(Socket sock)
    {
        InetSocketAddress socket = (InetSocketAddress)sock.getRemoteSocketAddress();
        String ip = socket.getAddress().getHostAddress();
        int port = socket.getPort();
        return ip + ":" + port;
    }

    public static String getLocalSocket(Socket sock)
    {
        String ip = sock.getLocalAddress().getHostAddress();
        int port = sock.getLocalPort();
        return ip + ":" + port;
    }

    public static byte[] serializeObject(Serializable src) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(src);
            oos.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Serializable deSerializeObject(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Serializable o = (Serializable) ois.readObject();
            ois.close();
            bais.close();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
