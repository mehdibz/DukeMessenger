package common;

import java.io.Serializable;

public class Message implements Serializable
{
	String id,mes,dis;
	public Message(){}
	public Message(String id ,String mes,String disUser)
	{
		this.id=id;
		this.mes=mes;
		this.dis=disUser;
	}
	public String get_Mes()
	{
		return (this.mes);
	}
	public String get_Dis()
	{
		return (this.dis);
	}
	
	public String get_Id()
	{
		return (this.id);
	}
}
