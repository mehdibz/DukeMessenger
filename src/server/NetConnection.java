package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import common.LoginInfo;
import common.Message;
import common.SignUpRequest;
import common.UpdateList;
import common.UsersStatus;
import common.accept;
import common.filePacket;
import common.offer;
import common.offlinepm;
import java.sql.Connection;

public class NetConnection extends Thread
{
	private Socket toClient;
	private Vector onlineUsers;
	private boolean signOut;
	private ObjectInputStream receiver;
	private ObjectOutputStream sender;
        public  String qu,s1,s2,s3,s4,s5,url,sql;
       	Object receivedData;
        public  Statement st;
        public  ResultSet rs;
        public Connection conn;
	String userId="unknown user";
		
	public NetConnection(Socket toClient,Vector OnlineUsers)
	{
		signOut=false;
		this.toClient=toClient;
		this.onlineUsers=OnlineUsers;
		this.start();
	}
	
	public void run() 
	{
		try
		{	
			receiver= new ObjectInputStream(toClient.getInputStream());
			sender=new ObjectOutputStream(toClient.getOutputStream());
		}
		catch(IOException io)
		{
			System.out.println("can not conect to client");
			signOut=true;
		}
		
		
		while(!signOut)
		{
			try
			{	
				receivedData=receiver.readObject();
				if(receivedData instanceof Message)
				{	
					Message InComingMes = (Message)receivedData;
					if (InComingMes.get_Dis().equals("PUBLIC"))
					{
						sendPublic(InComingMes);
					}
					else
					{
						sendPrivate(InComingMes);
					}
				}
				else if(receivedData instanceof LoginInfo )
					{
                                            try{
                                                    LoginInfo userIdAndPass= (LoginInfo)receivedData;
                                                    verification(toClient,userIdAndPass);
                                                }
                                                catch(InstantiationException inE){}
                                                catch(IllegalAccessException inE){ }
						
					}
					else
						if(receivedData instanceof SignUpRequest)
						{
							SignUpRequest signUPData= (SignUpRequest)receivedData;
							signUpUser(signUPData);
							try
							{
								toClient.close();
							}
							catch(IOException closeSocket)
							{
								System.out.println("can not closs the socket");
							}

							break;
						}
						else 
							if(receivedData instanceof offer)
							{
								offer offerObj=(offer)receivedData;
								sendOffer(offerObj);
                                                                
							}
							else 
								if (receivedData instanceof accept)
								{
									accept desResponse=(accept)receivedData;
									sendAccept(desResponse);
								}
								else
									if(receivedData instanceof filePacket)
									{
										filePacket data=(filePacket)receivedData;
										sendFilePacket(data);
									}
			}
			catch(ClassNotFoundException ClNotFoEx)
			{
				System.out.println("class not found exception"+ClNotFoEx);
			}
		
			catch(IOException ioe)
			{	
				OnlineUser user;
				int index=-1;
				UpdateList signouted= new UpdateList();
				signouted.addToList(new UsersStatus(userId,"REMOVE"));
				System.out.println(userId + " SignOut ");
				
				for(int i=0;i<onlineUsers.size();i++)
				{
					user=(OnlineUser)onlineUsers.elementAt(i);
					
					if(user.getId().equalsIgnoreCase(userId))
					{
						index=i;
						signOut=true;
					}
					else
					{	
						try
						{
							user.sender.writeObject(signouted);
						}
						catch(IOException excep)
						{}
					}
				}
				synchronized (onlineUsers)
				{ 
					if(index>-1)
						onlineUsers.removeElementAt(index);
					onlineUsers.notifyAll();
				}
				try
				{
					toClient.close();
				}
				catch(IOException closeExcep)
				{
					System.out.println("can not close the socket");
				}
				break;
				
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////	
	/////////////////////////////////////////////////////////////////////////////
	
	synchronized public void verification(Socket toClient,LoginInfo userIdAndpss) throws InstantiationException, IllegalAccessException
	{
		String ok="valid",error="invalid",note ="URCOnline",UserInf,User,SplitedUser[];
		OnlineUser UserInVec;
		UpdateList availableUser;
		availableUser=new UpdateList();
		
		for(int i=0;i<onlineUsers.size();i++)
		{	
			UserInVec=(OnlineUser) onlineUsers.elementAt(i);
			availableUser.addToList(new UsersStatus(UserInVec.UserId,"ADD"));
			if(UserInVec.UserId.equals(userIdAndpss.getUserName()))
			{
				try
				{
					sender.writeObject(note);
				}
				catch(IOException sendError)
				{}
				notifyAll();
				return;
			}
		}
		                       
                try
                { 
                       Class.forName("com.mysql.jdbc.Driver").newInstance();
                       url="jdbc:mysql://localhost:3306/mymessenger?user=root&password=123";
                       conn = DriverManager.getConnection(url);
                       Statement st = conn.createStatement();
                       sql="select * from user where ID='"+userIdAndpss.getUserName()+"' AND Password='"+userIdAndpss.getpassword()+"'";
                              
                       st.executeQuery(sql);
                       rs = st.getResultSet();
                       if(rs.next())
                       {
                               UpdateList newUser=new UpdateList();
                               OnlineUser others;
                               sender.writeObject(ok);
                               sender.writeObject(availableUser);
                               userId=userIdAndpss.getUserName();
                               sender.writeObject(chekOfflinepm(userId));
                               newUser.addToList(new UsersStatus(userId,"ADD"));
                               for(int i=0;i<onlineUsers.size();i++)
				{
                                      others=(OnlineUser)onlineUsers.elementAt(i);
                                      others.sender.writeObject(newUser);
				}
				onlineUsers.addElement(new OnlineUser(userIdAndpss.getUserName(),receiver,sender)); 
                                notifyAll();
				return;
                       }
                       else
                       {
                               sender.writeObject(error);
                       }
                       rs.close();
                       st.close();
                       conn.close();
                       notifyAll();
                       return;
                                    
                }
                catch(SQLException e)
                {
                      System.out.println("SQLException  ===");
                }
                catch(InstantiationException  insE)
                {}
                catch(IllegalAccessException ille)
                {}
                catch (ClassNotFoundException ex)
                {
                      ex.printStackTrace();
                }
                catch(IOException sendError)
                {}                
                
        }

	/////////////////////////////////////////////////////////////////////////////
	
	synchronized public void signUpUser(SignUpRequest signUpData)
	{
		String UserInf,SplitedUser[];
            	UserInf=signUpData.requestString();
			     
		SplitedUser=UserInf.split(",");
                s1=SplitedUser[0];
                s2=SplitedUser[1];
                s3=SplitedUser[2];
                s4=SplitedUser[3];
                s5=SplitedUser[4];
                       
                        try
                        { 
                              Class.forName("com.mysql.jdbc.Driver").newInstance();
                              url="jdbc:mysql://localhost:3306/mymessenger?user=root&password=123";
                              conn = DriverManager.getConnection(url);
                              Statement st = conn.createStatement();
                              sql="select * from user where ID='"+signUpData.getId()+"'";
                              st.executeQuery(sql);
                              rs = st.getResultSet();
                              if(rs.next())
                              {
                                    rs.close();
                                    st.close();
                                    conn.close();
                                    sender.writeObject("used");
                                    notifyAll();
                                    return;
                              }
                               sql="insert into user(ID,Password,Name,Family,Gender) values ('%s','%s','%s','%s','%s')";
                               sql=String.format(sql,s1,s2,s3,s4,s5);
                               st.execute(sql);
                               st.close();
                               conn.close();
                               rs.close();
                               sender.writeObject("URreg");
                               notifyAll();
                               return;
                              
                        }
                        catch(SQLException e){
                               System.out.println("SQLException  ===");
                        }
                        catch(InstantiationException  insE)
                        {}
                        catch(IllegalAccessException ille)
                        {}
                        catch (ClassNotFoundException ex) {
                               ex.printStackTrace();
                        }
                        catch(IOException sendError)
                        {}
                        
       }

	/////////////////////////////////////////////////////////////////////////////	
		
	
	synchronized public void sendPublic(Message InComingMes)
	{
		int AmountOfOnlines= onlineUsers.size();
		OnlineUser user;
		for (int i=0;i<AmountOfOnlines;i++)
		{
			user=(OnlineUser)onlineUsers.elementAt(i);
			if( !(user.getId().equals( InComingMes.get_Id() ) ) )
			{
				try
				{
					user.sender.writeObject(InComingMes);
				}
				catch(IOException ioe)
				{
					System.out.println("can not send to" +user.getId());
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////

	synchronized public void sendPrivate(Message InComingMess)
	{
		boolean send=false;
		int AmountOfOnlines= onlineUsers.size();
		OnlineUser user;
		for (int i=0;((i<AmountOfOnlines) && (!send));i++)
		{
			user=(OnlineUser)onlineUsers.elementAt(i);
			if( (user.getId().equals( InComingMess.get_Dis() ) ) )
			{
				try
				{
					user.sender.writeObject(InComingMess);
					send=true;
				}
				catch(IOException ioe)
				{
					System.out.println("can not send to" +user.getId());
				}
			}
		}
		if(!send)
		{
                    
			writeOfflines(InComingMess);
		}
		notifyAll();
	}

	/////////////////////////////////////////////////////////////////////////////
	
	synchronized public void writeOfflines(Message offlinePM)
	{
		FileOutputStream out;
		PrintStream writeToFile;
                
		try
		{
			out= new FileOutputStream("offlines.txt",true);
			writeToFile= new PrintStream(out);
			writeToFile.println(offlinePM.get_Id()+":"+offlinePM.get_Mes()+":"+offlinePM.get_Dis());
                        
                        
		}
		catch(IOException ioe)
		{
			
		}
	}

	synchronized public offlinepm chekOfflinepm(String id)
	{	
		File offline,temp;
		FileInputStream FileIn;
		BufferedReader inputBuffer;
		FileOutputStream FileOut;
		PrintStream writeTofile;
		offlinepm offMes;
		String mesInFile=null,destination=null,splitedMess[],From,Mes; 
		
		offMes= new offlinepm();
		temp=new File("temp.txt");
		offline= new File("offlines.txt");
		try
		{
			FileIn= new FileInputStream(offline);
			inputBuffer=new BufferedReader(new InputStreamReader(FileIn));
			mesInFile=inputBuffer.readLine();
			destination= inputBuffer.readLine();
			FileOut= new FileOutputStream(temp);
			writeTofile=new PrintStream(FileOut);
		}
		catch(IOException ioe)
		{
			return offMes;
		}
		while((mesInFile != null )&&(destination!=null))
		{
			splitedMess=mesInFile.split(":");
			From=splitedMess[0];
			try
			{
				Mes=splitedMess[1];
			}
			catch(ArrayIndexOutOfBoundsException arrayError)
			{
				Mes="\n";
			}
			if(destination.equalsIgnoreCase(":"+id))
			{
				offMes.addPM(new Message(From,Mes,destination) );
			}
			else
			{
				writeTofile.println(mesInFile+"\n"+destination);
			}
			try
			{
				mesInFile=inputBuffer.readLine();
				destination=inputBuffer.readLine();
			}
			catch(IOException IOE)
			{
				mesInFile=null;
				destination=null;
			}
			
		}
		try
		{
			inputBuffer.close();
			FileIn.close();
			writeTofile.close();
			FileOut.close();
		}
		catch(IOException IOE)
		{}
		offline.delete();
		System.out.println("off file deleted"); 
		temp.renameTo(new File("offlines.txt"));
		System.out.println("temp renamed");
		return offMes;
	}
	
	synchronized public void sendOffer(offer offerObj)
	{
		boolean send=false;
		int AmountOfOnlines= onlineUsers.size();
		OnlineUser user;
		for (int i=0;((i<AmountOfOnlines) && (!send));i++)
		{
			user=(OnlineUser)onlineUsers.elementAt(i);
			if( (user.getId().equals( offerObj.getDes() ) ) )
			{
				try
				{
					user.sender.writeObject(offerObj);
					send=true;
				}
				catch(IOException ioe)
				{
					System.out.println("can not send to" +user.getId());
				}
			}
		}
		if(!send)
		{
			try
			{
				sender.writeObject(new accept("rejcet",offerObj.getSender(),offerObj.getDes()));
			}
			catch(IOException excep)
			{}
		}
		notifyAll();
 
	}
	
	synchronized public void sendAccept(accept userRes)
	{
		int AmountOfOnlines= onlineUsers.size();
		OnlineUser user;
		for (int i=0;i<AmountOfOnlines ;i++)
		{
			user=(OnlineUser)onlineUsers.elementAt(i);
			if( (user.getId().equals( userRes.getDes() ) ) )
			{
				try
				{
					user.sender.writeObject(userRes);
					break;
				}
				catch(IOException ioe)
				{
					System.out.println("can not send to" +user.getId());
				}
			}
		}
		notifyAll();
	
	}
	
	synchronized public void sendFilePacket(filePacket Data)
	{
		int AmountOfOnlines= onlineUsers.size();
		OnlineUser user;
		for (int i=0;i<AmountOfOnlines ;i++)
		{
			user=(OnlineUser)onlineUsers.elementAt(i);
			if( (user.getId().equals( Data.getDes() ) ) )
			{
				try
				{
					user.sender.writeObject(Data);
					break;
				}
				catch(IOException ioe)
				{
					System.out.println("can not send to" +user.getId());
				}
			}
		}
		notifyAll();
	}
}
