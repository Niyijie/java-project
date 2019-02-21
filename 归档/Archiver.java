package com.company;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Archiver {

    public static void main(String[] args) throws Exception{
        FileOutputStream fos = new FileOutputStream("/Users/niyijie/Desktop/编程/src/com/company/test.rar");

        fos.write(addFile("/Users/niyijie/Desktop/编程/src/com/company/Archiver.java"));
        fos.write(addFile("/Users/niyijie/Desktop/编程/src/com/company/2.jpg"));
        fos.close();

    }

    public static byte[] addFile(String path) throws Exception {
        //打开文件
        File file = new File(path);

        //获取文件名字
        String fname = file.getName();

        //获取文件长度
        int fileLen = (int)file.length();

        //总长度
        int total = 4 + fname.getBytes().length + 4 + fileLen;
        byte[] bytes = new byte[total];

        //写入文件名长度
        byte[] fnameLenArr = Util.int2byte(fname.getBytes().length);
        System.arraycopy(fnameLenArr,0,bytes,0,4);

        //写入文件名
        byte[] fnameArr = fname.getBytes();
        System.arraycopy(fnameArr,0,bytes,4,fnameArr.length);

        //写入文件内容长度
        byte[] fileLenArr = Util.int2byte(fileLen);
        System.arraycopy(fileLenArr,0,bytes,4 + fnameArr.length,4);

        //写入文件内容
        //读取文件内容到字节数组中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        int len = 0;
        while((len=fis.read(buf))!=-1)
        {
            baos.write(bytes,0,len);
        }
        fis.close();
        byte[] fileArr = baos.toByteArray();
        //写入
        System.arraycopy(fileArr,0,bytes,4 + fnameArr.length + 4,fileArr.length);
        return bytes;
    }
}