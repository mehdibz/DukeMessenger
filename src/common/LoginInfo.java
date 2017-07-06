package common;

import java.io.Serializable;

public class LoginInfo implements Serializable
{
	private String userName;
	private String password;
	
	public LoginInfo(){};
	
	public LoginInfo(String userName,String password)
	{
		this.userName=userName;
		this.password=password;
	}
	public String getUserName()
	{
		return userName;
	}
	public String getpassword()
	{
		return password;
	}
}
