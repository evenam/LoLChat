package imserver;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class SocketAcceptorThread implements Runnable
{
    private ServerSocket server;
    private Thread t;
    private boolean stop;
    private ChatRoom chatroom;
    int i;
    
    public SocketAcceptorThread(ServerSocket ss, ChatRoom cr)
    {
        server = ss;
        stop = true;
        t = new Thread(this);
        chatroom = cr;
        i = 0;
    }
    
    public void run() 
    {
        while (!stop) 
        {
            try
            {
                Socket s = server.accept();;
                chatroom.registerClient(s, generateUsername());
                i ++;
            }
            catch (IOException ex)
            {
                System.out.println("ERROR: cannot register user. ");
            }
        }
    }
    
    public void startService()
    {
        stop = false;
        t.start();
    }
    
    public void stopService()
    {
        stop = true;
    }
    
    private String generateUsername()
    {
        String s;
        s = "User#" + i;
        return s;
    }
}
