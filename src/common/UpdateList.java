package common;

import java.io.Serializable;
import java.util.Vector;

public class UpdateList implements Serializable
{
	Vector users;
	
	public UpdateList()
	{
		users= new Vector(1,1) ;
	}
	public void addToList(UsersStatus user)
	{
		users.addElement(user);
	}
	public Vector getNewList()
	{
		return users;
	}
}
