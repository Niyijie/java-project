package common;

import client.QQClientSingleChatUI;
import client.QQClientUI;
import server.QQServer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MessageFactory {

    //解析消息并发送
    public static void parseClientMessageAndSend(Socket sock) throws Exception {
        InputStream ips = sock.getInputStream();
        //获取消息类型
        byte[] typeBytes = new byte[4];
        ips.read(typeBytes);
        int msgType = Util.bytes2Int(typeBytes);


        switch (msgType)
        {
            case message.SINGLECHAT_MESSAGE:
            {
                //得到接收方信息长度
                byte[] recieverInfoLenBytes = new byte[4];
                ips.read(recieverInfoLenBytes);
                int recieverInfoLen = Util.bytes2Int(recieverInfoLenBytes);
                //得到接收方信息
                byte[] recieverInfoBytes = new byte[recieverInfoLen];
                ips.read(recieverInfoBytes);
                String recieverInfo = new String(recieverInfoBytes);
                //得到消息长度
                byte[] msgLenBytes = new byte[4];
                ips.read(msgLenBytes);
                int msgLen = Util.bytes2Int(msgLenBytes);
                //得到消息内容
                byte[] msg = new byte[msgLen];
                ips.read(msg);

                //创建字节流
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //得到发送者信息
                String sendInfo = Util.getSockInfo(sock);

                //根据消息类型写入新到消息类型
                baos.write(Util.int2Bytes(message.SINGLECHAT_MESSAGE));

                //写入发送方信息长度
                baos.write(Util.int2Bytes(sendInfo.length()));
                //写入发送方信息
                baos.write(sendInfo.getBytes());
                //写入消息长度
                baos.write(Util.int2Bytes(msgLen));
                //写入消息
                baos.write(msg);
                //发送
                QQServer.getInstance().send(recieverInfo, baos.toByteArray());

                return ;
            }
            case message.CHATS_MESSAGE:
            {
                //得到消息长度
                byte[] msgLenBytes = new byte[4];
                ips.read(msgLenBytes);
                int msgLen = Util.bytes2Int(msgLenBytes);
                //得到消息内容
                byte[] msg = new byte[msgLen];
                ips.read(msg);

                //创建字节流
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //得到发送者信息
                String sendInfo = Util.getSockInfo(sock);

                //根据消息类型写入新到消息类型
                baos.write(Util.int2Bytes(message.CHATS_MESSAGE));

                //写入发送方信息长度
                baos.write(Util.int2Bytes(sendInfo.length()));
                //写入发送方信息
                baos.write(sendInfo.getBytes());
                //写入消息长度
                baos.write(Util.int2Bytes(msgLen));
                //写入消息
                baos.write(msg);
                //发送
                QQServer.getInstance().sendToAll(baos.toByteArray());

                return ;
            }
            case message.CLIENT_LINE_OFF:
            {
                String recieverInfo = Util.getSockInfo(sock);
                //移除该套接字并删除关闭连接
                QQServer.getInstance().removeAndCloseFriend(recieverInfo);
                //通知其他客户该用户下线了
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(Util.int2Bytes(message.CLIENT_LINE_OFF));
                //写入用户信息字节长度
                baos.write(Util.int2Bytes(recieverInfo.getBytes().length));
                //写入用户信息
                baos.write(recieverInfo.getBytes());
                QQServer.getInstance().sendToAll(baos.toByteArray());
                return ;
            }
            case message.CLIENT_LINE_ON:
            {
                String recieverInfo = Util.getSockInfo(sock);
                //通知其他客户该用户上线了
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(Util.int2Bytes(message.CLIENT_LINE_ON));
                //写入上线用户信息字节长度
                baos.write(Util.int2Bytes(recieverInfo.getBytes().length));
                //写入上线用户信息
                baos.write(recieverInfo.getBytes());
                QQServer.getInstance().sendToAll(baos.toByteArray());
                return ;
            }
        }
    }

    public static void parseServerMessage(Socket sock, QQClientUI ui)
    {
        try {
            InputStream ips = sock.getInputStream();
            //获取消息类型
            byte[] typeBytes = new byte[4];
            ips.read(typeBytes);
            int msgType = Util.bytes2Int(typeBytes);

            switch (msgType)
            {
                case message.FLUSH_MESSAGE:
                {
                    //获取字节长度
                    byte[] friendsListLenBytes = new byte[4];
                    ips.read(friendsListLenBytes);
                    int friendsListLen = Util.bytes2Int(friendsListLenBytes);
                    //获取好友信息
                    byte[] friendsList = new byte[friendsListLen];
                    ips.read(friendsList);
                    List<String> friends = (List<String>) Util.deSerializeObject(friendsList);
                    ui.flushFriendList(friends);
                    return;
                }
                case message.CHATS_MESSAGE: {

                    //得到发送方信息长度
                    byte[] senderInfoLenBytes = new byte[4];
                    ips.read(senderInfoLenBytes);
                    int senderInfoLen = Util.bytes2Int(senderInfoLenBytes);
                    //得到发送方信息
                    byte[] senderInfoBytes = new byte[senderInfoLen];
                    ips.read(senderInfoBytes);
                    String senderInfo = new String(senderInfoBytes);
                    //得到消息长度
                    byte[] msgLenBytes = new byte[4];
                    ips.read(msgLenBytes);
                    int msgLen = Util.bytes2Int(msgLenBytes);
                    //得到消息内容
                    byte[] msg = new byte[msgLen];
                    ips.read(msg);

                    String str = new String(msg);

                    ui.addMsgToHistory(senderInfo, str);
                    return;
                }
                case message.CLIENT_LINE_OFF:
                {
                    String senderInfo = Util.getLocalSocket(sock);
                    //读取字节长度
                    byte[] infoBytesLen = new byte[4];
                    ips.read(infoBytesLen);
                    //获取下线者信息
                    byte[] infoBytes = new byte[Util.bytes2Int(infoBytesLen)];
                    ips.read(infoBytes);
                    ui.addLogMessageToHistory(new String(infoBytes) + " 离开了聊天室");
                    return;
                }
                case message.CLIENT_LINE_ON:
                {
                    //读取字节长度
                    byte[] infoBytesLen = new byte[4];
                    ips.read(infoBytesLen);
                    //获取下线者信息
                    byte[] infoBytes = new byte[Util.bytes2Int(infoBytesLen)];
                    ips.read(infoBytes);

                    String senderInfo = new String(infoBytes);

                    ui.addLogMessageToHistory(new String(infoBytes) + " 加入了聊天室");
                    return;
                }
                case message.SINGLECHAT_MESSAGE:
                {
                    //得到发送方信息长度
                    byte[] senderInfoLenBytes = new byte[4];
                    ips.read(senderInfoLenBytes);
                    int senderInfoLen = Util.bytes2Int(senderInfoLenBytes);
                    //得到发送方信息
                    byte[] senderInfoBytes = new byte[senderInfoLen];
                    ips.read(senderInfoBytes);
                    String senderInfo = new String(senderInfoBytes);
                    //得到消息长度
                    byte[] msgLenBytes = new byte[4];
                    ips.read(msgLenBytes);
                    int msgLen = Util.bytes2Int(msgLenBytes);
                    //得到消息内容
                    byte[] msg = new byte[msgLen];
                    ips.read(msg);

                    String str = new String(msg);

                    QQClientSingleChatUI friendUI = ui.getFriendSingleChatUI(senderInfo);
                    if(friendUI == null)
                    {
                        friendUI = ui.addFriendSingleChatUI(senderInfo);
                    }

                    friendUI.addMsgToHistory(senderInfo,str);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
