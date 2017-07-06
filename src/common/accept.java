package common;

import java.io.Serializable;

 public class accept implements Serializable 
{
	private String accORrejct,from,to;
	public accept(){}
	public accept(String userRes,String sur,String des)
	{
		accORrejct=userRes;
		from=sur;
		to=des;
	}
	
	public String getUserRes()
	{
		return accORrejct;
	}
	
	public String getSur()
	{
		return from ;
	}
	
	public String getDes()
	{
		return to;
	}
}
