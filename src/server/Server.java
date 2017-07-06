package server;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Server extends Thread implements ActionListener
{
    private ServerSocket fromClient;
    private boolean runing;
    private JFrame window;
    private JButton shutDown,SetDB;
    private JPanel panel;
    private Vector OnlineUser;
    static  int userCont=0;
    private int x0, x1, x2, x3,i;
    private InetAddress laddress;
    private String s0, s1, s2, s3,url,sql;
    private JTextField t2,t3;
    private JLabel l1,l2,position,l3,l4;
    private Cursor cr;
    private GridBagLayout Layout;
    private GridBagConstraints Gbc; 
    private Font FntGeorgia;
   
    
    public Server() {
           super ("Server");
           Color black =new Color(0);
           Color white =new Color(171000);
          try {
            laddress = InetAddress.getLocalHost();
            byte ipaddress[] = laddress.getAddress();
            x0 = ipaddress[0];
            x1 = ipaddress[1];
            x2 = ipaddress[2];
            x3 = ipaddress[3];
            if (x0 < 0)
                x0 = 256 + x0;
            if (x1 < 0)
                x1 = 256 + x1;
            if (x2 < 0)
                x2 = 256 + x2;
            if (x3 < 0)
                x3 = 256 + x3;
            s0 = String.valueOf(x0);
            s1 = String.valueOf(x1);
            s2 = String.valueOf(x2);
            s3 = String.valueOf(x3);
            fromClient= new ServerSocket(20202);
                   
            runing=true;
            this.start();
        } catch(IOException ioe) {
            System.out.println("Server can not run");
            System.exit(1);
        }
       
        OnlineUser= new Vector(1,1);
        l1 = new JLabel();
        l2 = new JLabel();
        l3 = new JLabel();
        l4 = new JLabel();
        window = new JFrame("Chat Server");
        shutDown= new JButton(" Server OFF");
        FntGeorgia = new Font("Trafic", Font.BOLD, 16);
        cr = new Cursor(12);
        shutDown.setCursor(cr);
        shutDown.addActionListener(this);
        panel= new JPanel();
        l1.setFont(FntGeorgia);
        l2.setFont(FntGeorgia);
        l1.setForeground(white);
        l2.setForeground(white);
        l3.setForeground(black);
        l4.setForeground(black);
        l1.setText(" Server IP :  " + s0 + "." + s1 + "." + s2 + "." + s3);
        l2.setText(" Active Port :  20202 ");
        l3.setText("..............................................");
        l4.setText("........................");
        
        Icon logo = new ImageIcon("29171696.png");
	position = new JLabel(logo);
        panel.add(l4);
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(shutDown);
        panel.add(position);
        panel.setBackground(black);
        window.getContentPane().add(panel);
        window.setSize(220,340);
        window.setBounds(390,240,220,340);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
        
    }
////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent arg0) {
        JButton source=(JButton) arg0.getSource();
            if(source== shutDown)
		{
                    try {
                        runing=false;
                        fromClient.close();
                        } 
                    catch(IOException io) {
                        //System.exit(1);
                        }
		}
   }
////////////////////////////////////////////////////////////
    public void run() {
        while (runing) {
            try {
                new NetConnection(fromClient.accept(),OnlineUser);
            } catch(IOException ioe) {
                System.out.println(" Client Acception Failed");
            }
        }
        try {
            fromClient.close();
            System.exit(0);
        } catch(IOException ioe) {
            System.exit(1);
        }
    }
////////////////////////////////////////////////////////////
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
                               
                new Server();
      }
     

}
