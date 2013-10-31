package javaaudiotest;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.*;
import java.util.zip.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;


public class JavaAudioTest {
  private int inLine;
  private int outLine;
  private AudioFormat format;
  private Info[] lines;
  private TargetDataLine inputLine;
  private SourceDataLine outputLine;
  private DataLine.Info inInfo;
  private DataLine.Info outInfo;
  private int bufferSize;

  public static void main(String[] args)
  {
      JavaAudioTest test = new JavaAudioTest(4, 0);
      test.printLineInfo();
      test.startListening();
  }
  
  public JavaAudioTest(int in, int out){
    this.inLine = in;
    this.outLine = out;
    this.setup();
  }

  public JavaAudioTest(){
    this.inLine = 4;
    this.outLine = 0;
    this.setup();
  }

  private void setup(){
    format = new AudioFormat(16000, 16, 2, true, true);
    lines = AudioSystem.getMixerInfo();    
    inInfo = new DataLine.Info(TargetDataLine.class, format);
    outInfo = new DataLine.Info(SourceDataLine.class, format);
    bufferSize = (int) format.getSampleRate();// * format.getFrameSize();
    System.out.println("Buffersize: " + bufferSize + "\nFrame Size: " + format.getFrameSize());
    System.out.println("SIZE: " + bufferSize);
  }

  public void printLineInfo(){
    for (int i = 0; i < lines.length; i++){
      System.out.println(i+": "+lines[i].getName()+"\n"+lines[i].getDescription());
    }
  }

  public void startListening(){
    try{
      inputLine = (TargetDataLine)AudioSystem.getMixer(lines[inLine]).getLine(inInfo);
      inputLine.open(format, bufferSize);
      inputLine.start();
      
      outputLine = (SourceDataLine)AudioSystem.getMixer(lines[outLine]).getLine(outInfo);
      outputLine.open(format, bufferSize);
      outputLine.start();
      
      byte[] buffer = new byte[bufferSize];

      System.out.println("Listening on line " +inLine+", " + lines[inLine].getName() + "...");
      boolean quit = false;
      
      while(!quit)
      {
        inputLine.read(buffer,0,buffer.length);
        outputLine.write(buffer, 0, buffer.length);
      }
      
      inputLine.close();
      outputLine.close();
    }catch (LineUnavailableException e){
      System.out.println("Line " + inLine + " is unavailable.");
      e.printStackTrace();
      System.exit(1);
    }
  }
  
  public double volumeRMS(byte[] _raw) {
      double[] raw = new double[_raw.length];
      for (int i = 0; i < _raw.length; i ++)
      {
          raw[i] = (double)_raw[i]/255.0;
      }
        double sum = 0d;
        if (raw.length==0) {
            return sum;
        } else {
            for (int ii=0; ii<raw.length; ii++) {
                sum += raw[ii];
            }
        }
        double average = sum/raw.length;

        double sumMeanSquare = 0d;
        for (int ii=0; ii<raw.length; ii++) {
            sumMeanSquare += Math.pow(raw[ii]-average,2d);
        }
        double averageMeanSquare = sumMeanSquare/raw.length;
        double rootMeanSquare = Math.sqrt(averageMeanSquare);

        return rootMeanSquare;
    }
  
    public void compress(byte[] bytes)
    {
        OutputStream out = new ByteOutputStream(bytes.length);
        Deflater def = new Deflater(Deflater.BEST_SPEED);
        DeflaterOutputStream dos = new DeflaterOutputStream(out, def, 4 * 1024);
        
    }
}