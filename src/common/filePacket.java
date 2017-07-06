package common;

import java.io.Serializable;

public class filePacket implements Serializable 
{
	String to,from;
	byte Data[]; 
	
	public filePacket(){}
	
	public filePacket(String sur,String des,byte data[])
	{
		this.from= sur;
		this.to=des;
		this.Data=data;
	}
	
	
	public String getSur()
	{
		return from;
	}
	
	public String getDes()
	{
		return to;
	}
	
	public byte[] getData()
	{
		return Data;
	}
}
