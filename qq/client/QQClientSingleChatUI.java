package client;

import common.Util;

import javax.swing.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import common.message;

public class QQClientSingleChatUI extends JFrame {

    //当前客户端的套接字
    private Socket sock = null;

    //朋友信息
    private String friend = null;

    //历史聊天区
    private JTextArea taHistory;

    //消息输入区
    private JTextArea taInputMessage;

    //发送按钮
    private JButton btnSend;

    public QQClientSingleChatUI(Socket _sock , String _friend) {
        sock = _sock;
        friend = _friend;

        init();
        this.setVisible(true);
    }

    /**
     * 初始化布局
     */
    private void init() {
        this.setTitle("QQClient");
        this.setBounds(100, 100, 580, 600);
        this.setLayout(null);

        //历史区
        taHistory = new JTextArea();
        taHistory.setBounds(10, 10, 560, 400);

        JScrollPane sp1 = new JScrollPane(taHistory);
        sp1.setBounds(10, 10, 555, 400);
        this.add(sp1);

        //taInputMessage
        taInputMessage = new JTextArea();
        taInputMessage.setBounds(10, 420, 440, 140);
        this.add(taInputMessage);

        //btnSend
        btnSend = new JButton("发送");
        btnSend.setBounds(465, 420, 100, 140);
        btnSend.addActionListener(e -> {
            String str = taInputMessage.getText();
            if (!str.trim().equals("")) {
                sendSingleChatMessage(str);
                addMsgToHistory("我",str);
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


    public void sendSingleChatMessage(String str) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //写入消息类型
            baos.write(Util.int2Bytes(message.SINGLECHAT_MESSAGE));
            //写入接收方信息长度
            baos.write(Util.int2Bytes(friend.length()));
            //写入接收方信息
            baos.write(friend.getBytes());
            //写入消息字节长度
            baos.write(Util.int2Bytes(str.getBytes().length));
            //写入消息
            baos.write(str.getBytes());
            //发送
            OutputStream out = sock.getOutputStream();
            out.write(baos.toByteArray());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

