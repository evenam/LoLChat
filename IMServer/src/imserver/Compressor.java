package imserver;

import java.io.*;
import java.util.zip.*;

public class Compressor 
{
    public static byte[] compress(byte[] data) throws IOException
    {
        Deflater def = new Deflater(Deflater.BEST_SPEED);
        def.setInput(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        
        def.finish();
        byte[] buffer = new byte[1024];
        while (!def.finished())
        {
            int count = def.deflate(buffer);
            out.write(buffer, 0, count);
        }
        out.close();
        def.end();
        
        byte[] ret = out.toByteArray();
        
        return ret;
    }
    
    public static byte[] decompress(byte[] data) throws DataFormatException, IOException
    {
        Inflater inf = new Inflater();
        inf.setInput(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inf.finished())
        {
            int count = inf.inflate(buffer);
            out.write(buffer, 0, count);
        }
        out.close();
        inf.end();
        
        byte[] ret = out.toByteArray();
        
        return ret;
    }
    
    public static String byteArrayToString(byte[] array)
    {
        String ret = "";
        for (int i = 0; i < array.length; i ++)
        {
            ret += (char)array[i];
        }
        return ret;
    }
    
    public static byte[] stringToByteArray(String str)
    {
        byte[] ret = new byte[str.length()];
        for (int i = 0; i < str.length(); i ++)
        {
            ret[i] = (byte)str.charAt(i);
        }
        return ret;
    }
}
