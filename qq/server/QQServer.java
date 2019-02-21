package server;

import common.Util;
import common.message;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class QQServer {

    private static QQServer server = new QQServer();

    public static QQServer getInstance()
    {
        return server;
    }

    Map<String,Socket> AllSockts = new HashMap<>();

    public static void main(String[] args)
    {
        System.out.println("服务器启动......");
        server.start();
    }

    public void start()
    {
        try {
            //服务器绑定端口
            ServerSocket sock = new ServerSocket(8889);

            while (true) {
                //接收到客户端请求
                Socket clientSock = sock.accept();
                //将客户套接字信息并保存
                String key = Util.getSockInfo(clientSock);
                System.out.println(key + " connecting!!\n");
                if( !key.equals(":") ) {
                    System.out.println("getInfo " + Util.getSockInfo(clientSock));
                    System.out.println("getLocakInfo " + Util.getLocalSocket(clientSock));
                    AllSockts.put(key, clientSock);
                    //发送刷新消息，刷新客户端好友列表
                    sendFlushMessage();
                    //开启新线程专门服务与该客户端
                    new ServThread(clientSock).start();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void send(String key,byte[] bytes)
    {
        Socket sock = AllSockts.get(key);
        if(sock == null)
        {
            System.out.println("对方不在线!!");
            return;
        }

        try {
            OutputStream out = sock.getOutputStream();
            out.write(bytes);
            out.flush();
        }
        catch (Exception e)
        {
            System.out.println(key);
            e.printStackTrace();
        }
    }

    public void sendToAll(byte[] bytes)
    {
        Iterator<Socket> it = AllSockts.values().iterator();
            while (it.hasNext()) {
                try {
                    OutputStream out = it.next().getOutputStream();
                    out.write(bytes);
                    out.flush();
                } catch (Exception e) {
                    continue;
                }
            }
    }

    public void sendFlushMessage()
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //写入消息类型
            baos.write(Util.int2Bytes(message.FLUSH_MESSAGE));
            //得到好友列表序列化数组
            byte[] FriendList = getFriendListBytes();
            //写入字节数组长度
            baos.write(Util.int2Bytes(FriendList.length));
            //写入字节数组
            baos.write(FriendList);
            baos.close();
            sendToAll(baos.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] getFriendListBytes()
    {
        List<String> list = new ArrayList<String>(AllSockts.keySet());
        return Util.serializeObject((Serializable) list);
    }

    public void removeAndCloseFriend(String info) throws Exception {

        //获取用户套接字
        Socket _sock = AllSockts.get(info);
        //关闭该套接字
        _sock.close();
        //移除该好友
        AllSockts.remove(info);
        //刷新在线好友
        QQServer.getInstance().sendFlushMessage();
        System.out.println(info + " 下线了");
    }
}
