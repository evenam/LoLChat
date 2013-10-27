package imclient;

import java.io.*;
import java.net.*;

public class ConnectionThread implements Runnable
{
    private boolean stop;
    private Thread t;
    private PrintStream out;
    private BufferedReader in;
    private Socket socket;
    private ClientGUI gui;
    
    public ConnectionThread(Socket s, boolean useGUI, boolean useAudio)
    {
        if (useGUI)
            gui = ClientGUI.getReference();
        else
            gui = null;
        
        socket = s;
        t = new Thread(this);
        stop = true;
        try 
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } 
        catch (IOException ex) 
        {
            output("ERROR: cannot create input stream. ");
            System.exit(1);
        }
        try 
        {
            out = new PrintStream(s.getOutputStream());
        } 
        catch (IOException ex) 
        {
            output("ERROR: cannot create output stream. ");
            System.exit(1);
        }
    }
    
    public void run() 
    {
        String input = "";
        output("Connection Established");
        while (!stop)
        {
            if (socket.isConnected())
            {
                try 
                {
                    input = in.readLine();
                    output(input);
                } 
                catch (IOException ex) 
                {
                    output("ERROR: cannot retrieve message. ");
                    System.exit(1);
                }
            }
            else
            {
                output("Disconnected from server. ");
                stop = true;
            }
        }
    }
    
    public void start()
    {
        stop = false;
        t.start();
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
    
    private void output(String s)
    {
        if (gui != null)
            gui.sendOutput(s);
        else
            System.out.println(s);
    }
}
