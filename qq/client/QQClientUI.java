package client;


import com.sun.java.swing.plaf.windows.resources.windows;
import common.MessageFactory;
import common.Util;

import javax.swing.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.message;

public class QQClientUI extends JFrame {

    //当前客户端的套接字
    private Socket sock = null;

    private Map<String,QQClientSingleChatUI> FriendWindowsMap = new HashMap<>();

    //历史聊天区
    private JTextArea taHistory ;

    //好友列表
    private JList<String> lstFriends  ;

    //消息输入区
    private JTextArea taInputMessage ;

    //发送按钮
    private JButton btnSend ;

    public QQClientUI(Socket _sock){
        sock = _sock;

        init();
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                //关闭套接字
                try {
                    //向服务器发送下线消息
                    sendLineOffMessage();
                    //关闭套接字
                    sock.close();
                    System.exit(-1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化布局
     */
    private void init() {
        this.setTitle("QQClient");
        this.setBounds(100,100, 800, 600);
        this.setLayout(null);

        //历史区
        taHistory = new JTextArea();
        taHistory.setBounds(10, 10, 600, 400);

        JScrollPane sp1 = new JScrollPane(taHistory);
        sp1.setBounds(10, 10, 600, 400);
        this.add(sp1);

        //lstFriends
        lstFriends = new JList<String>();
        lstFriends.setBounds(620, 10, 160, 400);
        //添加监听器
        lstFriends.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2)
                {
                    String friend = lstFriends.getSelectedValue();
                    //如果点击的是自己，则返回
                    if (friend.equals(Util.getLocalSocket(sock)))
                        return;

                    if (FriendWindowsMap.containsKey(friend))
                    {
                        FriendWindowsMap.get(friend).setVisible(true);
                    }
                    else {
                        addFriendSingleChatUI(friend);
                    }
                }
            }
        });

        this.add(lstFriends);

        //taInputMessage
        taInputMessage = new JTextArea();
        taInputMessage.setBounds(10, 420, 540, 140);
        this.add(taInputMessage);

        //btnSend
        btnSend = new JButton("发送");
        btnSend.setBounds(560, 420, 200, 140);
        btnSend.addActionListener(e -> {
            String str = taInputMessage.getText();
            if(!str.trim().equals("")) {
                sendChatsMessage(str);
            }
            taInputMessage.setText("");
        });

        this.add(btnSend);
    }

    /**
     * 添加消息到历史区
     */
    public void addMsgToHistory(String senderInfo, String msgStr) {
        Date d = new Date();
        taHistory.append(d.toString() + "\n");

        taHistory.append(senderInfo + " 说 :\r\n");
        taHistory.append("       " + msgStr);
        taHistory.append("\r\n");
        taHistory.append("\r\n");
    }

    public void addLogMessageToHistory(String msgStr) {
        Date d = new Date();
        taHistory.append(d.toString() + "\n");
        taHistory.append(msgStr);
        taHistory.append("\r\n");
        taHistory.append("\r\n");
    }

    public void flushFriendList(List<String> friends)
    {
        if(friends.size() == 0)
            return;

        DefaultListModel<String> list = new DefaultListModel<>();
        for(String str:friends)
        {
            list.addElement(str);
        }
        lstFriends.setModel(list);
    }

    public void sendChatsMessage(String str)
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //写入消息类型
            baos.write(Util.int2Bytes(message.CHATS_MESSAGE));
            //写入消息字节长度
            baos.write(Util.int2Bytes(str.getBytes().length));
            //写入消息
            baos.write(str.getBytes());
            //发送
            OutputStream out = sock.getOutputStream();
            out.write(baos.toByteArray());
            out.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendLineOffMessage()
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //写入消息类型
            baos.write(Util.int2Bytes(message.CLIENT_LINE_OFF));
            //发送
            OutputStream out = sock.getOutputStream();
            out.write(baos.toByteArray());
            out.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendLineOnMessage()
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //写入消息类型
            baos.write(Util.int2Bytes(message.CLIENT_LINE_ON));
            //发送
            OutputStream out = sock.getOutputStream();
            out.write(baos.toByteArray());
            out.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public QQClientSingleChatUI getFriendSingleChatUI(String friendUI)
    {
        if(FriendWindowsMap.containsKey(friendUI))
        {
            return FriendWindowsMap.get(friendUI);
        }
        return null;
    }

    public QQClientSingleChatUI addFriendSingleChatUI(String friend) {
        //如果表中没有该朋友 则创建

        QQClientSingleChatUI ui = new QQClientSingleChatUI(sock, friend);
        FriendWindowsMap.put(friend, ui);
        return ui;
    }
}
