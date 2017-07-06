package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OnlineUser 
{       String UserId;
	ObjectInputStream recever;
	ObjectOutputStream sender;
	public OnlineUser(){};
	
	public OnlineUser(String id,ObjectInputStream receiver,ObjectOutputStream sender)
	{
		UserId=id;
		this.recever=receiver;
		this.sender=sender;
	}
	public String getId()
	{
		return (UserId);
	}
}
