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
        IMClient client = new IMClient("0.0.0.0", 1337, true, true);
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
    
    public IMClient(String ip, int port, boolean useGUI, boolean useAudio)
    {
        if (useGUI)
        {
            gui = ClientGUI.getReference();
            gui.registerOutlet(this);
        }
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
        connection = new ConnectionThread(s, useGUI, useAudio);
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
    
    public void clientInput(String input, int type)
    {
        if (type == 1) // audio
        {
            try
            {
                sendMessage("/audio " + Compressor.byteArrayToString(Compressor.compress(Compressor.stringToByteArray(input))));
            }
            catch (IOException e) {}
        }
        else // text input
        {
            if (input.toLowerCase().equals("/exit") || input.toLowerCase().equals("/disconnect"))
            {
                endSession();
                System.exit(0);
            }
            else
            {
                boolean shouldSend = true;
                // filter reserves stuff
                try
                {
                    if (input.toLowerCase().substring(0, 6).equals("/audio"))
                        shouldSend = false;
                }
                catch (Exception e) {}

                if (shouldSend)
                {
                    try
                    {
                        sendMessage("/text " + Compressor.byteArrayToString(Compressor.compress(Compressor.stringToByteArray(input))));
                    }
                    catch (IOException e) {}
                }
            }
        }
    }
    
    private void output(String s)
    {
        try
        {
            if (s.substring(0, 5).equals("/text"))
            {
                if (gui != null)
                    gui.sendOutput(Compressor.byteArrayToString(Compressor.decompress(Compressor.stringToByteArray(s.substring(6)))));
                else
                    System.out.println(s);
            }
        }
        catch (Exception e) {}
        try
        {
            if (s.substring(0, 6).equals("/audio"))
            {
                // no, play this out yo speakerzzz
                if (gui != null)
                    gui.sendOutput(Compressor.byteArrayToString(Compressor.decompress(Compressor.stringToByteArray(s.substring(7)))));
                else
                    System.out.println(s);
            }
        }
        catch (Exception e) {}
    }
}
