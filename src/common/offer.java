package common;

import java.io.Serializable;

public class offer implements Serializable
{
	private String from,to,FName;
	private Long Fsize;
	
	public offer(){};
	public offer(String sender,String dis,Long size,String name)
	{
		from=sender;
		to=dis;
		Fsize= size;
		FName=name;
	}

	public String getSender()
	{
		return from;
	}

	public String getFilename()
	{
		return FName;
	}
	public Long getFileSize()
	{
		return Fsize;
	}
	public String getDes(){
		return to;
	}
}
