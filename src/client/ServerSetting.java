package client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import sun.awt.WindowClosingListener;

public class ServerSetting extends JFrame implements ActionListener,WindowListener
{
	SignIn main_form;
	JPanel panel;
	JLabel IPLabel;
	JTextField ServerIP;
	JButton ok;
        private Color lst,SR;
        public String serverIP;
        private Cursor cr;
	
	public ServerSetting(SignIn FirstFrame)
	{
		super("Server Setting");
		main_form=FirstFrame;
		panel= new JPanel();
		IPLabel= new JLabel("Chat Server:");
		ServerIP= new JTextField(10);
		ok= new JButton(" Apply ");
                cr = new Cursor(12);
		ok.setCursor(cr);
		ok.addActionListener(this);
		panel.add(IPLabel);
		panel.add(ServerIP);
		panel.add(ok);
                SR =  new Color(2147416000);
                panel.setBackground(SR);
		ServerIP.setText(SignIn.serverIP);
		this.addWindowListener(this);
		this.getContentPane().add(panel);
		this.setSize(240, 100);
                setBounds(390, 240, 240, 100);
                setResizable(false);
		this.setVisible(true);
	}
	
	
	
	public void actionPerformed(ActionEvent arg0) 
	{
		String Ip =ServerIP.getText();
		if(Ip.length()<7)
		{
			JOptionPane.showMessageDialog(null,  " Invalid IP "  ,"Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else
		{
			SignIn.serverIP=Ip;
			main_form.setEnabled(true);
			main_form.requestFocusInWindow();
			this.dispose();
		}
	}



	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

        
	public void windowClosing(WindowEvent arg0)
	{
		main_form.setEnabled(true);
		main_form.requestFocus();
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
