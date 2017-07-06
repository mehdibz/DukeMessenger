package common;

import java.io.Serializable;
import java.util.Vector;

public class offlinepm implements Serializable
{
	Vector offlineMes;
	public offlinepm()
	{
		offlineMes=new Vector(1,1);
	}
	public void addPM(Message pm )
	{
		offlineMes.addElement(pm);
	}
	public Vector getPM()
	{
		return (offlineMes);
	}
}
