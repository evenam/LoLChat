package imserver;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class IMServer
{
    private ChatRoom chatroom;
    private SocketAcceptorThread acceptor;
    private ServerSocket ss;
    
    public static void main(String[] args) 
    {
        IMServer server = new IMServer();
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().trim();
        while (true)
        {
            server.serverInput(input);
            // server command here
            input = in.nextLine();
        }
    }
    
    public IMServer()
    {
        try 
        {
            ss = new ServerSocket(1337);
            System.out.println("Binded server to port 1337");
        } 
        catch (IOException ex) 
        {
            System.out.println("ERROR: unable to bind server to port 1337");
            System.exit(1);
        }
        
        chatroom = new ChatRoom();
        acceptor = new SocketAcceptorThread(ss, chatroom);
        acceptor.startService();
    }

    private void printServerIP() 
    {
        System.out.println(ss.getInetAddress().getHostAddress() + ":1337");
    }

    private void endSession() 
    {
        acceptor.stopService();
        chatroom.endSession();
    }

    private void printHistory() 
    {
        chatroom.printHistory();
    }

    private void sendMessage(String msg) 
    {
        chatroom.sendMessage("***SERVER***", msg);
    }

    private void printServerList()
    {
        System.out.println("Users:");
        for (int i = 0; i < chatroom.getClients().size(); i ++)
        {
            System.out.println(chatroom.getClients().get(i).getUsername());
        }
    }
    
    public void serverInput(String input)
    {
        if ("exit".equals(input.toLowerCase()))
        {
            endSession();
            System.exit(0);
        }
        else 
        {
            try
            {
                if (input.toLowerCase().substring(0, 9).equals("servermsg"))
                {
                    sendMessage(input.substring(10));
                }
            }
            catch (StringIndexOutOfBoundsException ex)
            {
                if (input.toLowerCase().equals("userlist"))
                {
                    printServerList();
                }
                else if ("history".equals(input.toLowerCase()))
                {
                    printHistory();
                }
                else if ("serverip".equals(input.toLowerCase()))
                {
                    printServerIP();
                }
            }
        }
    }
}
