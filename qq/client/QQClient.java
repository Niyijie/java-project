package client;

import common.MessageFactory;
import common.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class QQClient extends Thread{

    private Socket clientSock = null;

    private QQClientUI ui = null;

    public QQClient(Socket sock)
    {
        clientSock = sock;
        ui = new QQClientUI(sock);
    }

    public void run() {
        ui.sendLineOnMessage();
        while (true) {
            try {
                MessageFactory.parseServerMessage(clientSock,ui);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args)
    {
        try {
//            47.93.213.146
            Socket sock = new Socket("47.93.213.146", 8889);
            QQClient c = new QQClient(sock);
            c.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
