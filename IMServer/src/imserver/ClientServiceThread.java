package imserver;

import java.net.*;
import java.io.*;

public class ClientServiceThread implements Runnable
{
    private boolean stop;
    private Socket socket;
    private Thread t;
    private BufferedReader in;
    private PrintStream out;
    private String username;
    private ChatRoom chatroom;
    
    public ClientServiceThread(Socket s, String user, ChatRoom cr)
    {
        socket = s;
        stop = true;
        t = new Thread(this);
        username = user;
        chatroom = cr;
    }
    
    public void start()
    {
        stop = false;
        t.start();
    }
    
    public void run() 
    {
        try 
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } 
        catch (IOException ex) 
        {
            System.out.println("ERROR: cannot create input stream. ");
        }
        
        try 
        {
            out = new PrintStream(socket.getOutputStream());
        } 
        catch (IOException ex) 
        {
            System.out.println("ERROR: cannot create output stream. ");
        }
        
        String input = "";
        while (!stop)
        {
            if (socket.isConnected())
            {
                try
                {
                    input = in.readLine();
                }
                catch (IOException ex)
                {
                    System.out.println("ERROR: could not fetch input. ");
                }

                if (input == null)
                {
                    stop = true;
                }
                else if (input == "/disconnect")
                {
                    stop = true;
                }
                else if (input.charAt(0) == '/')
                {
                    if (input.substring(0, 9).equals("/username"))
                    {
                        if (!input.substring(10).equals("***SERVER***"))
                        username = input.substring(10);
                    }
                    // insert user commands here
                }
                else
                {
                    chatroom.sendMessage(username, input);
                }
            }
            else
            {
                System.out.println(username + " disconnected from the server");
                stop = true;
            }
        }
        
        try 
        {
            in.close();
        } 
        catch (IOException ex) 
        {
            System.out.println("ERROR: cannot close input stream.");
        }
        out.close();
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void sendMessage(String message)
    {
        if (!stop)
        {
            out.println(message);
            out.flush();
        }
    }
    
    public String getUsername()
    {
        return username;
    }
}
