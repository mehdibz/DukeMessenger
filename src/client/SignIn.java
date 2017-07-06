package client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import common.LoginInfo;
import common.UpdateList;
import common.offlinepm;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.sql.*; 


public class SignIn extends JFrame implements KeyListener, ActionListener
{
	
	private JLabel Idlab,Passlab,position;
	private JTextField Id;
	private JPasswordField Pass;
	private JButton Bsin,Bsup,Bclr,Setser;
	private JPanel Panel;
	private GridBagLayout Layout;
	private GridBagConstraints Gbc; 
	protected Socket toServer;	 
	private ObjectInputStream receiver;
	private ObjectOutputStream sender;
        private Cursor cr;
        private Color lst,SR;
 	public static String serverIP="127.0.0.1";
	
						///////////////////////////////////
	
	public SignIn ()
	{
		super ("Sign in ");
		Layout= new GridBagLayout();
		Gbc =new GridBagConstraints();
		Bsin = new JButton ("Sign In");
		Bsup = new JButton ("Sign Up");
		Setser= new JButton("Setting");
		Idlab = new JLabel ("User ID  : ");
		Passlab = new JLabel (" Password : ");
		Id = new JTextField(5);
		Pass = new JPasswordField (5);
                Bsin.setToolTipText("Press Enter");
		Bsup.setToolTipText("Press F3");
		Setser.setToolTipText("Press F2");
                cr = new Cursor(12);
		Bsin.setCursor(cr);
		Bsup.setCursor(cr);
		Setser.setCursor(cr);
		Panel = new JPanel ();
		Panel.setLayout(Layout);
                SR =  new Color(2147416000);
                Panel.setBackground(SR);
                Pass.addKeyListener(this);
		Id.addKeyListener(this);
                
                Gbc.gridx=0;
		Gbc.gridy=0;
		Layout.setConstraints(Idlab,Gbc);
		Panel.add(Idlab,Gbc);
		
		Gbc.fill=GridBagConstraints.BOTH;
		Gbc.gridwidth=2;
		Gbc.gridx=1;
		Gbc.gridy=0;
		Layout.setConstraints(Id, Gbc);
		Panel.add(Id,Gbc);
		
		Gbc.gridwidth=1;
		Gbc.gridx=0;
		Gbc.gridy=1;
		Layout.setConstraints(Passlab, Gbc);
		Panel.add(Passlab,Gbc);
		
		Gbc.fill=GridBagConstraints.BOTH;
		Gbc.gridwidth=2;
		Gbc.gridx=1;
		Gbc.gridy=1;
		Layout.setConstraints(Pass, Gbc);
		Panel.add(Pass);
		
		Gbc.anchor= GridBagConstraints.EAST;
		Gbc.fill=GridBagConstraints.NONE;
		Gbc.gridwidth=1;
		Gbc.gridx=1;
		Gbc.gridy=2;
		Layout.setConstraints(Bsin, Gbc);
		Panel.add(Bsin);
		
			
                Gbc.anchor=GridBagConstraints.WEST;
		Gbc.fill=GridBagConstraints.NONE;
		Gbc.gridwidth=1;
		Gbc.gridx=2;
		Gbc.gridy=2;
		Layout.setConstraints(Setser, Gbc);
		Panel.add(Setser);
		
			
                Gbc.fill=GridBagConstraints.BOTH;
		Gbc.gridwidth=1;
		Gbc.gridx=0;
		Gbc.gridy=2;
		Layout.setConstraints(Bsup, Gbc);
		Panel.add(Bsup);
                
        	
		Icon logo = new ImageIcon("duke-logo.jpg");
		position = new JLabel(logo);
                Gbc.fill=GridBagConstraints.BOTH;
		Gbc.gridwidth=4;
                Gbc.gridx = 0;
		Gbc.gridy = 4;
		Layout.setConstraints(position, Gbc);
		Panel.add(position);
                
		Bsin.addActionListener(this);
		Bsup.addActionListener(this);
		Setser.addActionListener(this);
		
		getContentPane().add(Panel);
		setSize(230, 300);
		setResizable(false);
		setVisible(true);
                setBounds(390, 240, 230, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		
	}
//////////////////////////////////////	
//////////////////////////////////////
        public void keyTyped(KeyEvent arg0) {
	}

	public void keyPressed(KeyEvent e) {
            if (10 == e.getKeyCode()) {
                if (Pass.getPassword().length < 4) {
			JOptionPane.showMessageDialog(null, "Your PASSWORD is too short",
					null, JOptionPane.ERROR_MESSAGE);
			Pass.setText("");
			Id.requestFocus();
			return;
		}
                else{
                        try
			{
				checkitems(Id.getText(),new String( Pass.getPassword()) );
			}
			catch (loginexception ev) 
			{				
				JOptionPane.showMessageDialog(null,  " User ID or Password is Invalid "  ,"Error", JOptionPane.WARNING_MESSAGE);
				Pass.setText("");
                                Id.setText("");
                                Id.requestFocus();	
			}
                        catch (NullPointerException Nullexp){}
                }
            } 
            if (114 == e.getKeyCode()) {
            	//dispose();
		Id.requestFocus();
                new SignUp();
            }
            if (113 == e.getKeyCode()) {
            	Id.requestFocus();
                this.setEnabled(false);
		new ServerSetting(this);
            }
            if (27 == e.getKeyCode())
			System.exit(0);
         }

	public void keyReleased(KeyEvent arg0) {
	}
        
        
      	public void actionPerformed(ActionEvent evt) 
	{
		JButton source=(JButton) evt.getSource();
		if (source== Bsin)
		{	 Id.requestFocus();
			try
			{
				checkitems(Id.getText(),new String( Pass.getPassword()) );
			}
			catch (loginexception e) 
			{				
				JOptionPane.showMessageDialog(null,  " User ID or Password is Invalid "  ,"Error", JOptionPane.WARNING_MESSAGE);
				Pass.setText("");
                                Id.setText("");
                                Id.requestFocus();	
			}
			 catch (NullPointerException Nullexp){}			
		}
		if (source==Bsup)
		{
                    Id.requestFocus();
                    new SignUp();
		}
			
		if(source==Setser)
		{
                   Id.requestFocus();
                   this.setEnabled(false);
                   new ServerSetting(this);
                   
		}
		
	
	}
	
//////////////////////////////////////	
	public void checkitems(String userName,String password) throws loginexception
	{	
		
		LoginInfo userInfo;
		String serverReply;
		UpdateList users;
		offlinepm offPM;
		String []splitedUsers=null;
		if (userName.length()==0 || password.length()==0) 
			throw new loginexception();
		
		
		userInfo= new LoginInfo(userName,password);
		
		try		
		{
			toServer= new Socket(serverIP,20202);
			sender= new ObjectOutputStream(toServer.getOutputStream());
			sender.writeObject(userInfo);
			receiver= new ObjectInputStream(toServer.getInputStream());
			serverReply=receiver.readObject().toString();
			if (serverReply.equalsIgnoreCase("valid"))
			{
				try
				{
					users=(UpdateList)receiver.readObject();
					offPM=(offlinepm)receiver.readObject();
					new Chat(this,userName,toServer,sender,receiver,users.getNewList());
					new offlines(offPM);
				}
				catch(IOException readUsers)
				{
					JOptionPane.showMessageDialog(null,  "Login Process failed \n     try again"  ,"Error", JOptionPane.WARNING_MESSAGE);
				}
				
			}	
			else
				if(serverReply.equals("URCOnline"))
				{
					JOptionPane.showMessageDialog(null,  "You are online,Are you forget ?"  ,"Error", JOptionPane.WARNING_MESSAGE);
				}
				else
				{					
					throw new loginexception();
				}
		}
		catch( UnknownHostException UnH)
		{
			JOptionPane.showMessageDialog(null,  "Invalid IP or bad Connection"  ,"Error", JOptionPane.WARNING_MESSAGE);
			try
			{
				toServer.close();
			}
			catch(IOException closeSocket)
			{
				System.out.println("Error in closing socket");
			}
			return;
		}
		catch(ClassNotFoundException castError)
		{
			JOptionPane.showMessageDialog(null,  "server send invalid data"  ,"Error", JOptionPane.WARNING_MESSAGE);
			try
			{
				toServer.close();
			}
			catch(IOException closeSocket)
			{
				System.out.println("Error in closing socket ");
			}

			return;
		}
		catch(IOException ioe)
		{
			JOptionPane.showMessageDialog(null,  "Server not found"  ,"Error", JOptionPane.WARNING_MESSAGE);
			try
			{
				toServer.close();
			}
			catch(IOException closeSocket)
			{
				System.out.println("Error in closing socket ");
			}
			return;
		}
	}
///////////////////////////////////////	
	public static void main(String[] args) 
	{
           SignIn Signin_Form = new SignIn();
	}

}


//////////////////////////////////////

class loginexception extends Exception
{
	
}
