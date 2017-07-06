package common;

import java.io.Serializable;

public class SignUpRequest implements Serializable
{
	private String name,family,sex,id,password,name2,family2,sex2,id2,password2;
	
	public SignUpRequest(){}
	
	public SignUpRequest(String name,String family,String sex,String id,String pass )
	{
		this.name=name;
		this.family= family;
		this.sex=sex;
		this.id=id;
		this.password=pass;
	}
	public String getId()
	{
		return id;
	}
	public String requestString()
	{
		return (this.id+","+this.password+","+this.name+","+this.family+","+this.sex );
	}
      
}
