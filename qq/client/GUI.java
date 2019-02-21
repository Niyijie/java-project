package client;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    public JTextArea Message = new JTextArea();
    public JButton btn = new JButton("get");

    GUI()
    {
        Message.setBounds(0,0,100,100);
        this.add(Message);

        btn.setBounds(50,50,100,100);
        btn.addActionListener(e->{
            String str = Message.getText();
            System.out.println(str);
        });
        this.add(btn);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        GUI ui = new GUI();
    }
}
