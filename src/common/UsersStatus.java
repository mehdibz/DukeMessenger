package common;

import java.io.Serializable;

public class UsersStatus implements Serializable
{
	String userId,status;
	public UsersStatus(){}
	public UsersStatus(String id,String status) 
	{
		this.userId= id;
		this.status=status;
	}
	public String getID()
	{
		return this.userId;
	}
	public String getStatus()
	{
		return this.status;
	}
}
