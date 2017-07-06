package client;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.Message;
import common.offlinepm;
import java.awt.Cursor;

public class offlines extends JFrame implements ActionListener
{
	private JPanel panel;
	private JTextArea offlineArea;
	private JButton close;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	private JScrollPane sc;
        private Cursor cr;
	public offlines (offlinepm offPMs)
	{
		super("Offline Messages");
		panel= new JPanel();
		offlineArea= new JTextArea(9,30);
		close= new JButton("Close");
                cr = new Cursor(12);
		close.setCursor(cr);
		layout= new GridBagLayout();
		gbc= new GridBagConstraints();
		panel.setLayout(layout);
		sc= new JScrollPane(offlineArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		if(offPMs.getPM().size()==0)
		{
			this.dispose();
			return;
		}	
		else
		{
			showPM(offPMs.getPM());
		}
		gbc.weightx=1;
		gbc.weighty=0;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridheight=2;
		gbc.gridwidth=3;
		gbc.fill=GridBagConstraints.BOTH;
		layout.setConstraints(sc,gbc);
		panel.add(sc);
		
		gbc.gridx=2;
		gbc.gridy=3;
		gbc.anchor=GridBagConstraints.EAST;
		gbc.fill=GridBagConstraints.NONE;
		gbc.gridheight=0;
		gbc.gridwidth=0;
		layout.setConstraints(close, gbc);
		panel.add(close);
		
		this.pack();
		this.getContentPane().add(panel);
		this.close.addActionListener(this);
		this.setSize(400,250);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	public void showPM(Vector PMs)
	{
		Message pm;
		for (int i=0;i<PMs.size();i++)
		{
			pm= (Message)PMs.elementAt(i);
			offlineArea.append(pm.get_Id()+" : "+pm.get_Mes()+"\n");
		}
	}
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		this.dispose();
	}
}
