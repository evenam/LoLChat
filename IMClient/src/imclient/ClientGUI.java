package imclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientGUI extends JFrame implements ActionListener
{
    private JTextArea taChatBox;
    private JTextField tfInputBox;
    private JButton btnSend;
    private static ClientGUI ref;
    private IMClient out;
    
    private ClientGUI()
    {
        taChatBox = new JTextArea();
        tfInputBox = new JTextField();
        btnSend = new JButton("Send");
        
        this.setLayout(new GridLayout(3,1));
        
        this.add(taChatBox);
        this.add(tfInputBox);
        this.add(btnSend);
        
        this.setVisible(true);
        this.setSize(800, 600);
        
        btnSend.addActionListener(this);
        tfInputBox.addActionListener(this);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource().equals(tfInputBox) || ae.getSource().equals(btnSend))
        {
            out.clientInput(tfInputBox.getText(), 0);
            tfInputBox.setText("");
        }
    }
    
    public void sendOutput(String output)
    {
        taChatBox.setText(taChatBox.getText() + output + "\n");
    }
    
    public static ClientGUI getReference()
    {
        if (ref == null)
        {
            ref = new ClientGUI();
        }
        return ref;
    }

    public void registerOutlet(IMClient outlet) 
    {
        out = outlet;
    }
}
