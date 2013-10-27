package imclient;

import java.util.Scanner;

public class AudioHandler 
{
    private int  bufferSize;
    
    public AudioHandler()
    {
        bufferSize = 20;
    }
    
    public String readData()
    {
        return null;
    }
    
    public void writeData(String buffer)
    {
        
    }
    
    private String convertBufferString(byte[] buffer)
    {
        String ret = "/audio ";
        
        for (int i = 0; i < buffer.length; i ++)
        {
            ret += (char)buffer[i];
        }
        
        return ret;
    }
    
    private byte[] convertBufferByteArray(String buffer)
    {
        Scanner new_b = new Scanner(buffer.substring(7));
        new_b.useDelimiter("");
        char temp;
        byte[] ret = new byte[bufferSize];
        for (int i = 0; (i < bufferSize) && new_b.hasNext(); i ++)
        {
            temp = new_b.next().charAt(0);
            ret[i] = (byte)temp;
        }
        return ret;
    }
}
