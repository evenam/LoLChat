package imclient;

import java.io.*;
import java.net.*;

public class IMClient 
{
    private Socket s;
    private ConnectionThread connection;
    private ClientGUI gui;
    
    public static void main(String[] args) 
    {
        IMClient client = new IMClient("0.0.0.0", 1337, true);
        /*
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        String input = "";
        
        while (true)
        {
            try
            {
                input = in.readLine();
            }
            catch (IOException ex)
            {
                System.out.println("ERROR: unable to get input. ");
                System.exit(1);
            }
            client.clientInput(input);
        }*/
    }
    
    public IMClient(String ip, int port, boolean useGUI)
    {
        if (useGUI)
            gui = ClientGUI.getReference();
        else
            gui = null;
        
        try
        {
            s = new Socket(ip, port);
            output("Connected to host " + ip + ":" + port);
        }
        catch (IOException ex)
        {
            output("ERROR: cannot resolve host. ");
            System.exit(1);
        }
        connection = new ConnectionThread(s, useGUI);
        connection.start();
    }

    private void sendMessage(String input) 
    {
        connection.sendMessage(input);
    }

    private void endSession() 
    {
        connection.stop();
    }
    
    public void clientInput(String input)
    {
        if (input.toLowerCase().equals("/exit") || input.toLowerCase().equals("/disconnect"))
        {
            endSession();
            System.exit(0);
        }
        else
        {
            sendMessage(input);
        }
    }
    
    private void output(String s)
    {
        if (gui != null)
            gui.sendOutput(s);
        else
            System.out.println(s);
    }
}
