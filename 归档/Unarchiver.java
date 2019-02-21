package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Unarchiver {

    public static void main(String[] args) throws Exception
    {
        List<FileBean> list = new ArrayList<>();

        File fd = new File("/Users/niyijie/Desktop/编程/src/com/company/test.rar");
        if(fd == null)
            return;
        FileInputStream fis = new FileInputStream(fd);

        FileBean fb = null;

        while((fb = nextFile(fis)) != null)
        {
            list.add(fb);
        }
        fis.close();

        for(FileBean f : list)
        {
            FileOutputStream fos = new FileOutputStream("/Users/niyijie/Desktop/编程/src/com/company/t/" + f.getName());
            fos.write(f.getContent());
            fos.close();
        }

    }

    public static FileBean nextFile(FileInputStream fis) throws Exception
    {
        //读取文件名长度
        byte[] byte4 = new byte[4];
        int ret = fis.read(byte4);
        if(ret == -1)
        {
            return null;
        }

        //读取文件名
        int nameLen = Util.byte2int(byte4);
        byte[] fileName = new byte[nameLen];
        fis.read(fileName);

        //读取内容长度
        fis.read(byte4);
        int fileLen = Util.byte2int(byte4);
        byte[] fileContent = new byte[fileLen];

        //读取内容
        fis.read(fileContent);

        FileBean fb = new FileBean(new String(fileName),fileContent);
        return fb;
    }
}
