package imserver;

import java.util.*;
import java.net.*;

public class ChatRoom 
{
    private ArrayList<ClientServiceThread> clients;
    //private ArrayList<String> history;
    
    public ChatRoom()
    {
        //history = new ArrayList<String>();
        clients = new ArrayList<ClientServiceThread>();
        System.out.println("***START OF SESSION***");
    }
    
    public void sendMessage(String username, String message)
    {
        System.out.println(username + ": " + message);
        //history.add(username + ": " + message);
        for (int i = 0; i < clients.size(); i ++)
        {
            clients.get(i).sendMessage(username + ": " + message);
        }
    }
    
    public void registerClient(Socket s, String username)
    {
        ClientServiceThread cst = new ClientServiceThread(s, username, this);
        clients.add(cst);
        cst.start();
        System.out.println(cst.getUsername() + " has joined the chatroom. ");
    }
    
    public void endSession()
    {
        for (int i = 0; i < clients.size(); i ++)
        {
            clients.get(i).stop();
        }
        System.out.println("***END OF SESSION***");
    }

    // Implement printHistory better (using files and stuff
    public void printHistory()
    {
        /*for (int i = 0; i < history.size(); i ++)
        {
            System.out.println(history.get(i));
        }*/
    }
    
    public ArrayList<ClientServiceThread> getClients()
    {
        return clients;
    }
}
