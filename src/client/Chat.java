package client;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import common.Message;
import common.UpdateList;
import common.UsersStatus;
import common.accept;
import common.filePacket;
import common.offer;
import java.awt.Cursor;
import java.awt.Font;

public class Chat extends JFrame implements KeyListener, ActionListener,Runnable,WindowListener
{
	
	private JPanel Panel,userPanel,buttonpanel;
	private JButton Send,Private;
	private JTextArea SendArea,ReceiveArea;
	private JList availableUser;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	private JScrollPane sc1,sc2,sc3;
	private Socket toServer;
	private String userId,clr;
	private Thread recieveThread;
	private ObjectOutputStream sendMes;
	private ObjectInputStream receiveMessage;
	protected Vector myFriends;
	private boolean signOut;
	private DefaultListModel ListElements;
	private SignIn masterForm;
        private Cursor cr;
        private Font FntGeorgia,Fnt;
        private Color white,lst,SR;
	//////////////////////////////////////////////////////////////////////////////////
	
	
	public Chat(SignIn masterForm,String id,Socket toServer,ObjectOutputStream sendMes,ObjectInputStream receiveMessage,Vector users)
	{
		super("Public Chat") ;
		this.masterForm= masterForm;
		this.masterForm.setEnabled(false);
		this.masterForm.setVisible(false);
                white =new Color(40000);
                lst = new Color(2147416400);
                SR =  new Color(2147416000);
		myFriends=new Vector(5,1);
		signOut=false;
		Send = new JButton("Send");
		ReceiveArea= new JTextArea(8,25);
		ReceiveArea.setEditable(false);
                ReceiveArea.setLineWrap(true);
		SendArea= new JTextArea(4,25);
                SendArea.setLineWrap(true);
                ReceiveArea.setBackground(SR);
                SendArea.setBackground(SR);
                FntGeorgia = new Font("Trafic", Font.ROMAN_BASELINE, 13);
                Fnt = new Font("Trafic", Font.BOLD, 13);
                ReceiveArea.setFont(FntGeorgia);
                SendArea.setFont(FntGeorgia);
                ReceiveArea.setWrapStyleWord(true);
                ReceiveArea.setLineWrap(true);
                ReceiveArea.setAutoscrolls(true);
                ListElements= new DefaultListModel();
                UsersStatus userInNewList;
		for(int i=0;i<users.size();i++)
		{	
			try
			{	
				userInNewList=(UsersStatus)users.elementAt(i);
				ListElements.addElement(userInNewList.getID());
			}
			catch(ArrayIndexOutOfBoundsException OutOfBound)
			{
				break;
			}
		}
		availableUser=new JList(ListElements);
		availableUser.setVisibleRowCount(6);
		availableUser.setAutoscrolls(true);
		availableUser.setFixedCellWidth(100);
                availableUser.setBackground(lst);
                availableUser.setFont(Fnt);
                Private=new JButton("Private");
                cr = new Cursor(12);
		Send.setCursor(cr);
		Private.setCursor(cr);
		sc1=new JScrollPane(ReceiveArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sc2=new JScrollPane(SendArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sc3=new JScrollPane(availableUser,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		sc1.setAutoscrolls(true);
		sc2.setAutoscrolls(true);
                sc3.setAutoscrolls(true);
            
                sc1.setWheelScrollingEnabled(true);
		layout= new GridBagLayout();
		gbc= new GridBagConstraints();
		Panel= new JPanel();
                Panel.setLayout(layout);
		
                userPanel=new JPanel();
		userPanel.setLayout(layout);
		
		gbc.gridx= 0;
		gbc.gridy=0;
		gbc.gridwidth=3;
		gbc.fill=GridBagConstraints.BOTH;
		layout.setConstraints(sc1, gbc);
		Panel.add(sc1);

		gbc.gridx= 0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.VERTICAL;
		gbc.gridwidth=0;
		gbc.weightx=0;
		gbc.weighty=1;
		layout.setConstraints(sc3, gbc);
		userPanel.add(sc3);
	
		gbc.gridx= 3;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridwidth=0;
		gbc.weightx=0;
		gbc.weighty=0;
             	layout.setConstraints(userPanel, gbc);
		Panel.add(userPanel);
	
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.fill= GridBagConstraints.HORIZONTAL;
	
	
		gbc.gridwidth=1;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		layout.setConstraints(sc2, gbc);
		Panel.add(sc2);
		
		buttonpanel=new JPanel();
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
		buttonpanel.add(Private);
		
		gbc.weightx=0;
		gbc.weighty=1;
		gbc.gridwidth=1;
		gbc.gridx= 0;
		gbc.gridy=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.anchor=GridBagConstraints.CENTER;
		layout.setConstraints(Send, gbc);
		buttonpanel.add(Send);


		this.addWindowListener(this);
		this.toServer=toServer;
		this.sendMes =sendMes;
		this.receiveMessage= receiveMessage;
		this.userId=id;
		recieveThread=new Thread(this);
		recieveThread.start();

		SendArea.addKeyListener(this);
		Send.addActionListener(this);
		Private.addActionListener(this);
                this.requestFocusInWindow();
		SendArea.requestFocus(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                getContentPane().add(Panel);
		setSize(400, 400);
		setResizable(false);
                setVisible(true);
		setBounds(500, 300, 400, 400);
		pack();
		
	}

	
//////////////////////// IMPLEMENTING THE KEY LISTENER ///////////////////////////////

	public void keyPressed(KeyEvent key) 
	{
		
			
	}
	public void keyTyped(KeyEvent arg0){}
	public void keyReleased(KeyEvent key) {
            if (key.getKeyCode()==KeyEvent.VK_ENTER)
		{	
		   
                        printMssage(userId,SendArea.getText()+"\n");
			sendMessage(userId,SendArea.getText()+"\n","PUBLIC");
			SendArea.setText("");
                        SendArea.setCaretPosition(-1);
                        SendArea.requestFocus();
                            
                }
        }

//////////////////////////////////////////////////////////////////////////////////////	
	

	public void actionPerformed(ActionEvent Source )
	{
                if( (JButton)Source.getSource()==Send )
		{
			printMssage(userId, SendArea.getText()+"\n");
			sendMessage(userId,SendArea.getText()+"\n","PUBLIC");
			SendArea.setText("");
                        SendArea.requestFocus();
		}
		else 
			if( (JButton)Source.getSource()==Private)
			{
				if(availableUser.getSelectedValue()!=null)
				{
					myFriends.addElement(new Friend ( new PrivateChat(this,(String)availableUser.getSelectedValue()),(String)availableUser.getSelectedValue() ));
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Select one of your friends "," select a person", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		
	}
	
	public void printMssage(String Id,String Mes)
	{
		ReceiveArea.setFont(FntGeorgia);
                ReceiveArea.setCaretPosition(ReceiveArea.getText().length());
                ReceiveArea.append(Id+" : "+ Mes);
                
        }
	
	public void sendMessage(String id,String message,String to)
	{
		synchronized (sendMes)
		{
			try
			{
				sendMes.writeObject(new Message(id,message,to));
			}
			catch (IOException IOE) 
			{}
			sendMes.notifyAll();
		}
	}
	
	
	public void sendoffer(offer offerObj)
	{
		synchronized (sendMes)
		{
			try
			{
				sendMes.writeObject(offerObj);
			}
			catch (IOException IOE) 
			{System.out.println(IOE.getMessage()+" offer");}
			sendMes.notifyAll();
		}
	}

	public void sendAccept(accept userAccept)
	{
		synchronized (sendMes)
		{
			try
			{
				sendMes.writeObject(userAccept);
			}
			catch (IOException IOE) 
			{System.out.println(IOE.getMessage()+" accept");}
			sendMes.notifyAll();
		}
	}
	
	public void sendFile(filePacket data)
	{
		synchronized (sendMes)
		{
			try
			{
				sendMes.writeObject(data);
			}
			catch (IOException IOE) 
			{System.out.println(IOE.getMessage()+" filepacket");}
			sendMes.notifyAll();
		}
        }
	
	public String getId() 
	{
		return (userId);
	}
	
	public void run()
	{
		Object receivedData;
		Message receivedMes;
		UpdateList usersList;
		offer offerObj;
		accept sendFileRes;
		while(!signOut)
		{
			try
			{	
				receivedData=receiveMessage.readObject();
				if (receivedData instanceof Message) 
				{
					 receivedMes= (Message) receivedData;
					 messageHandling(receivedMes);
				}
				else
				{
					if(receivedData instanceof UpdateList)
					{
						usersList= (UpdateList)receivedData;
						setList(usersList);
					}
					else
					{
						if (receivedData instanceof offer)
						{
							offerObj= (offer)receivedData;
							showOffer(offerObj);
						}
						else
						{
							if(receivedData instanceof accept)
							{	
								sendFileRes= (accept)receivedData;
								if(sendFileRes.getUserRes().equalsIgnoreCase("accept"))
									UploadFile(sendFileRes);
							}
							else
								if(receivedData instanceof filePacket)
								{
									filePacket data=(filePacket)receivedData;
									Download(data);
								}
						}
					}
				}
			}
			catch(ClassNotFoundException CNFE)
			{
	
	 			try	{recieveThread.sleep(1000);}
				catch (InterruptedException IE)	{}
				
			}
			catch(IOException IOE)
			{
				
			}
		}
	}
	
	public void messageHandling(Message ReceivedMess)
	{
		boolean sended=false;
		if(ReceivedMess.get_Dis().equalsIgnoreCase("PUBLIC"))
		{
			printMssage(ReceivedMess.get_Id(),ReceivedMess.get_Mes());
                        
		}
		else
		{	Friend vec_friend;
			for(int i=0 ;i<myFriends.size();++i)
			{
				try
				{
					vec_friend=(Friend)myFriends.elementAt(i);
					if(vec_friend.friendName.equalsIgnoreCase(ReceivedMess.get_Id()))
					{
						vec_friend.chatFrame.printMssage(ReceivedMess.get_Id(), ReceivedMess.get_Mes());
						sended=true;
						break;
					}
				}
				catch(ArrayIndexOutOfBoundsException ArrayOutOfBu)
				{
					break;
				}
				
			}
			if(!sended)
			{
				PrivateChat newWindow=new PrivateChat(this,ReceivedMess.get_Id());
				newWindow.printMssage(ReceivedMess.get_Id(), ReceivedMess.get_Mes());
				myFriends.addElement( new Friend(newWindow,ReceivedMess.get_Id()) );
			}
			
		}
        }
	
	public void setList(UpdateList usersList)
	{
		Vector newList;
		newList= usersList.getNewList();
		UsersStatus userInlist;
		for(int i=0;i<newList.size();i++)
		{
			userInlist=(UsersStatus)newList.elementAt(i);
			
			if(userInlist.getStatus().equalsIgnoreCase("ADD"))
			{	
				ListElements.addElement(userInlist.getID());
			}	
			else
			{	
				String myFriend;
				for(int j=0;j<ListElements.size();j++)
				{
					myFriend=(String)ListElements.elementAt(j);
					if(myFriend.equalsIgnoreCase(userInlist.getID()))
					{
						ListElements.removeElementAt(j);
					}
				}
			}
		}
		
	}


	public void showOffer(offer offerObj)
	{
		String from;
		Friend friendInVec;
		for(int i=0;i<myFriends.size();i++)
		{
			friendInVec=(Friend) myFriends.elementAt(i);
			if (friendInVec.friendName.equalsIgnoreCase(offerObj.getSender()))
			{
				friendInVec.chatFrame.getFile(offerObj);
				break;
			}
		}
	}
	
	public void UploadFile(accept userAcception)
	{
		String from;
		Friend friendInVec;
		for(int i=0;i<myFriends.size();i++)
		{
			friendInVec=(Friend) myFriends.elementAt(i);
			if (friendInVec.friendName.equalsIgnoreCase(userAcception.getSur()))
			{
				friendInVec.chatFrame.StartUpload();
				break;
			}
		}

	}
	public void Download(filePacket Data)
	{
		String from;
		Friend friendInVec;
		for(int i=0;i<myFriends.size();i++)
		{
			friendInVec=(Friend) myFriends.elementAt(i);
			if (friendInVec.friendName.equalsIgnoreCase(Data.getSur()))
			{
				friendInVec.chatFrame.StartDownload(Data);
				break;
			}
		}
	}
	
	public void windowClosing(WindowEvent arg0) 
	{
		try
		{
			masterForm.toServer.close();
			signOut=true;
		}
		catch(IOException e)
		{
			System.out.println("can not sign out you");
		}
		masterForm.setVisible(true);
		masterForm.setEnabled(true);
		masterForm.requestFocus();
		this.dispose();
	}
	public void windowOpened(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
}
