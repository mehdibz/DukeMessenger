package client;

public class Friend
{
	PrivateChat chatFrame;
	String friendName;
	public Friend(PrivateChat chat_whit,String freindname)
	{
		chatFrame=chat_whit;
		this.friendName=freindname;
	}
	public String myFringName() 
	{
		return (friendName);
	}
}
