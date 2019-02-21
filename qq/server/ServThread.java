package server;

import common.MessageFactory;
import common.Util;

import java.net.Socket;

public class ServThread extends Thread{

    private Socket sock = null;

    ServThread(Socket _sock)
    {
        sock = _sock;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                MessageFactory.parseClientMessageAndSend(sock);
            }
            catch (Exception e)
            {
                break;
            }
        }
    }

}
