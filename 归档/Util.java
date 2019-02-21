package com.company;

public class Util {
    /**
     * 整型转字节数组
     * */
    public static byte[] int2byte(int num)
    {
        byte[] arr = new byte[4];
        arr[0] = (byte) num;
        arr[1] = (byte)(num >> 8);
        arr[2] = (byte)(num >> 16);
        arr[3] = (byte)(num >> 24);
        return arr;
    }
    /**
     * 字节数组转整型
     * */
    public static int byte2int(byte[] arr)
    {
        int n0,n1,n2,n3;
        n0 = (arr[0]&0xff);
        n1 = (arr[1]&0xff)<<8;
        n2 = (arr[2]&0xff)<<16;
        n3 = (arr[3]&0xff)<<24;
        return n0 | n1 | n2 | n3;
    }
}
