package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import common.accept;
import common.filePacket;
import common.offer;
import java.awt.Cursor;

public class PrivateChat extends JFrame implements KeyListener,ActionListener,WindowListener
{
	
	private JPanel Panel,buttonpanel;
	private JButton Send,sendFile;
	private JTextArea SendArea,ReceiveArea;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	private JScrollPane sc1,sc2;
	private String myId,with;
	private Chat MasterForm; 
	private File saveTo,selectedFile;
        private Cursor cr;
        private Color white,lst,SR;
        private Font FntGeorgia,Fnt;
	
	////////////////////////////////////////////////////
	
	
	public PrivateChat(Chat master_frame,String des )
	{
		super("Privte chat with "+des ) ;
		MasterForm=master_frame;
		with=des;
		myId=MasterForm.getId();
		white =new Color(40000);
                SR =  new Color(2147413210);
		Send = new JButton("Send");
		sendFile= new JButton("send File");
                cr = new Cursor(12);
		Send.setCursor(cr);
		sendFile.setCursor(cr);
		ReceiveArea= new JTextArea(8,30);
		ReceiveArea.setEditable(false);
		SendArea= new JTextArea(4,24);
		SendArea.setLineWrap(true);
                ReceiveArea.setLineWrap(true);
		sc1=new JScrollPane(ReceiveArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sc2=new JScrollPane(SendArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ReceiveArea.setBackground(SR);
                SendArea.setBackground(SR);
                FntGeorgia = new Font("Trafic", Font.ROMAN_BASELINE, 13);
                Fnt = new Font("Trafic", Font.BOLD, 13);
                ReceiveArea.setFont(FntGeorgia);
                SendArea.setFont(FntGeorgia);
                
		layout= new GridBagLayout();
		gbc= new GridBagConstraints();
		Panel= new JPanel();
		Panel.setLayout(layout);
		
		gbc.gridx= 0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.NONE;
		gbc.gridwidth=0;
		layout.setConstraints(sc1, gbc);
		Panel.add(sc1);
		
		gbc.gridwidth=1;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		layout.setConstraints(sc2, gbc);
		Panel.add(sc2);
		
		buttonpanel= new JPanel();
		buttonpanel.setLayout(layout);
		gbc.weightx=0;
		gbc.weighty=0;
		gbc.gridwidth=2;
		gbc.gridx= 2;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.CENTER;
		layout.setConstraints(buttonpanel, gbc);
		Panel.add(buttonpanel);
		
		gbc.weightx=0;
		gbc.weighty=0;
		gbc.gridwidth=1;
		gbc.gridx= 0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.anchor=GridBagConstraints.CENTER;
		layout.setConstraints(Send, gbc);
		buttonpanel.add(sendFile);
		
		gbc.weightx=0;
		gbc.weighty=1;
		gbc.gridwidth=1;
		gbc.gridx= 0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.anchor=GridBagConstraints.CENTER;
		layout.setConstraints(Send, gbc);
		buttonpanel.add(Send);

		getContentPane().add(Panel);
		setSize(375, 245);
				
		SendArea.addKeyListener(this);
		Send.addActionListener(this);
		sendFile.addActionListener(this);
		this.addWindowListener(this);
                setResizable(false);
		setVisible(true);
		SendArea.requestFocus();
                setBounds(500, 100, 375, 245);
                pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
	}

	public void keyTyped(KeyEvent key) {
		
	}

	public void keyPressed(KeyEvent key) {}

	public void keyReleased(KeyEvent key) 
	{
            if(key.getKeyCode()==key.VK_ENTER)
		{
			printMssage(myId, SendArea.getText());
			MasterForm.sendMessage(myId,SendArea.getText(),with);
			SendArea.setText("");
                        SendArea.setCaretPosition(-1);
                        SendArea.requestFocus();
		}
	}

	public void actionPerformed(ActionEvent obj)
	{
		JButton butten=(JButton)obj.getSource();
		if(butten==Send)
		{	printMssage(myId, SendArea.getText()+"\n");
			MasterForm.sendMessage(myId,SendArea.getText()+"\n",with);
			SendArea.setText("");
                        SendArea.requestFocus();
		}
		else
		{
			if(butten==sendFile)
			{
				
				JFileChooser fileSlector;

				fileSlector=new JFileChooser();
				int seleted=fileSlector.showOpenDialog(PrivateChat.this);
				if(seleted==JFileChooser.APPROVE_OPTION)
				{
					selectedFile=(fileSlector.getSelectedFile());
					sendOffer(selectedFile);
					System.out.println(selectedFile.getName());
					
				}
			}
		}
                SendArea.requestFocus();
		
	}
	public void printMssage(String Id,String Mes)
	{
                ReceiveArea.setCaretPosition(ReceiveArea.getText().length());
		ReceiveArea.append(Id+" : "+ Mes);
	}

	
	public void windowClosing(WindowEvent arg0) 
	{	
		Friend myFriend;
		for(int i=0;i<MasterForm.myFriends.size();i++)
		{
			myFriend=(Friend)MasterForm.myFriends.elementAt(i);
			if(myFriend.friendName.equalsIgnoreCase(with))
			{
				MasterForm.myFriends.removeElementAt(i);
				break;
			}	
		}
	}
	
	public void sendOffer(File selected)
	{
		String name= selected.getName();
		Long size=new Long(selected.length());
		MasterForm.sendoffer( new offer(myId,with,size,name));
	}
	
	public void getFile(offer acceptOrNat)
	{
		JFileChooser saveDialog = new JFileChooser();
		int seleted=saveDialog.showSaveDialog(PrivateChat.this);
		if(seleted==JFileChooser.APPROVE_OPTION)
		{
			saveTo= saveDialog.getSelectedFile();
			MasterForm.sendAccept(new accept("accept",myId,with));
		}
		else
		{
			MasterForm.sendAccept(new accept("rejcet",myId,with));
		}
	}
	
	public void StartUpload()
	{
		try{
			System.out.println("upload");
		      FileInputStream in = new FileInputStream(selectedFile);
		      byte[] buf = new byte[1024];
		      int len;
		      while ((len = in.read(buf)) > 0)
		      {
		        MasterForm.sendFile(new filePacket(myId,with,buf));
		      }
		      in.close();
		    }
  
		    catch(IOException e)
		    {
		      System.out.println(e.getMessage());      
		   }
	}
	
	 public void StartDownload (filePacket Data)
	   {
		 try
		 {
			 System.out.println("download");
			 FileOutputStream out= new FileOutputStream(saveTo,true);
			 byte []buf=Data.getData();
		     out.write(buf);
		     out.flush();
		     out.close();
		      
		 }
		 catch(IOException e)
		 {
			 System.out.println(e.getMessage());
		 }
		   
	   }
	
	public void windowOpened(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}

}
