package client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import common.SignUpRequest;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;



public class SignUp extends JFrame implements KeyListener, ActionListener 
{
	private JLabel Namelab,Familylab,Genderlab,Idlab,Passlab,Repasslab,position;
	private JTextField Name,Family,Id;
	private JPasswordField Pass,Repass;
	private JRadioButton Male,Female,Noun;
	private JButton Submit,Reset,Back;
	private ButtonGroup group;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	private JPanel Panel;
        private javax.swing.JPanel p2, p3;
	private String Item;
	private	Socket toServer;
        private Cursor cr;
        private Color lst,SR;
/////////////////////////////////////////////////////////////////
	
	public SignUp ()
	{
		super("Sign Up");
		layout= new GridBagLayout();
		gbc=new GridBagConstraints();
		Id= new JTextField(10);
		Namelab = new JLabel("Name: ");
		Familylab = new JLabel("family: ");
		Genderlab = new JLabel("Gender: ");
		Idlab = new JLabel("User ID: ");
		Passlab = new JLabel("Password: ");
		Repasslab= new JLabel("Retype Password: ");
		Name= new JTextField (10); 
		Family= new JTextField (10);
		Pass= new JPasswordField (10);
		Repass= new JPasswordField(10);
		Male= new JRadioButton("Male");
		Female= new JRadioButton("Female");
		Noun= new JRadioButton();
		Panel=new JPanel();
		Panel.setLayout(layout);
                SR =  new Color(2147416000);
                Panel.setBackground(SR);
		Submit= new JButton("Submit");
		Reset= new JButton("Reset");
		Back= new JButton("Back");
                Submit.setToolTipText("Press Enter");
		Reset.setToolTipText("Press F3");
		Back.setToolTipText("Press ESC");
                cr = new Cursor(12);
		Submit.setCursor(cr);
		Reset.setCursor(cr);
		Back.setCursor(cr);
		
		p2=new JPanel(); 
		p3=new JPanel();
                p2.setBackground(SR);
		p3.setBackground(SR);
                Male.setBackground(SR);
		Female.setBackground(SR);
                Male.setCursor(cr);
                Female.setCursor(cr);
                group= new ButtonGroup();
                group.add(Male);
		group.add(Female);
		group.add(Noun);
		
		gbc.anchor=GridBagConstraints.WEST;
		gbc.weighty=1;
		
		
		gbc.gridx=0;
		gbc.gridy=0;		
                layout.setConstraints( Namelab,gbc);
		Panel.add(Namelab);
		
				
		gbc.gridx=1;
		gbc.gridy=0;
		layout.setConstraints( Name,gbc);
		Panel.add(Name);
		
		
		gbc.gridx=0;
		gbc.gridy=1;
		layout.setConstraints( Familylab,gbc);
		Panel.add(Familylab);
		
		
		gbc.gridx=1;
		gbc.gridy=1;
		layout.setConstraints( Family,gbc);
		Panel.add(Family);
		
		gbc.gridx=0;
		gbc.gridy=2;
		layout.setConstraints( Genderlab,gbc);
		Panel.add(Genderlab);

		p2.add(Male);
		p2.add(Female);

		gbc.gridx=1;
		gbc.gridy=2;
		layout.setConstraints( p2,gbc);
		Panel.add(p2 );
		
		
		gbc.gridx=0;
		gbc.gridy=3;
		layout.setConstraints( Idlab,gbc);
		Panel.add(Idlab);
		
		
		gbc.gridx=1;
		gbc.gridy=3;
		layout.setConstraints( Id,gbc);
		Panel.add(Id);
		
		gbc.gridx=0;
		gbc.gridy=4;
		layout.setConstraints( Passlab,gbc);
		Panel.add(Passlab);
		
		gbc.gridx=1;
		gbc.gridy=4;
		layout.setConstraints( Pass,gbc);
		Panel.add(Pass);
		
		gbc.gridx=0;
		gbc.gridy=5;
		layout.setConstraints( Repasslab,gbc);
		Panel.add(Repasslab);
		
		gbc.gridx=1;
		gbc.gridy=5;
		layout.setConstraints( Repass,gbc);
		Panel.add(Repass);
                
                Icon logo = new ImageIcon("duke-logo.jpg");
		position = new JLabel(logo);
                gbc.fill=GridBagConstraints.BOTH;
		gbc.gridwidth=4;
                gbc.gridx = 0;
		gbc.gridy = 4;
		layout.setConstraints(position, gbc);
                Panel.setLayout(layout);
//		Panel.add(position);
		
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.weighty=1;
		
		
		p3.add(Submit);
		p3.add(Reset);
		p3.add(Back);
		
		gbc.gridwidth=3;
		gbc.gridx=0;
		gbc.gridy=6;
		layout.setConstraints( p3,gbc);
		Panel.add(p3);
		
		Submit.addActionListener(this);
                Reset.addActionListener(this);
                Back.addActionListener(this);
                Id.addKeyListener(this);
                Name.addKeyListener(this);
                Family.addKeyListener(this);
                Pass.addKeyListener(this);
                Repass.addKeyListener(this);
                
			
		getContentPane().add(Panel);
		setSize(290, 230);
		setResizable(false);
                setBounds(390, 240, 290, 230);
		setVisible(true);
             	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		
	}
        public void keyTyped(KeyEvent arg0) {
	}

	public void keyPressed(KeyEvent evt) {
                ObjectOutputStream outPut;
		ObjectInputStream Input;
		String result;
            if (10 == evt.getKeyCode()) {
                try
			{
				checkitems();
				String sex;
				if (Male.isSelected())
					sex="Male";
				else
					sex="Female";
				SignUpRequest UserInf= new SignUpRequest(Name.getText(),Family.getText(),sex,Id.getText(),new String(Pass.getPassword())); 
				try
				{
					toServer= new Socket(SignIn.serverIP,20202);
					outPut= new ObjectOutputStream(toServer.getOutputStream());
					outPut.writeObject(UserInf);
					Input= new ObjectInputStream(toServer.getInputStream());
					try
					{
						result=(String)Input.readObject();
					}
					catch(ClassNotFoundException CNF)
					{
						JOptionPane.showMessageDialog(null,  " Invalid response form server"   ,"Error", JOptionPane.WARNING_MESSAGE);
						toServer.close();
						return;
					}
					if(result.equals("URreg"))
					{
						JOptionPane.showMessageDialog(null,  " Thanks , your Registration was successfully   "    ,"Registration is complete", JOptionPane.INFORMATION_MESSAGE);
						toServer.close();
                                                
                                                dispose();
                                                
						return;
					}
					else
					{
						JOptionPane.showMessageDialog(null,  " Your Inputed ID was Reserved By Someone , Try agin ! \n "   ,"Error", JOptionPane.WARNING_MESSAGE);
						toServer.close();
                                                Name.setText("");
                                                Family.setText("");
                                                Noun.setSelected(true);
                                                Id.setText("");
                                                Pass.setText("");
                        			Repass.setText("");
                                                Name.requestFocus();
						return;
					}
				}
				catch(UnknownHostException InvalidIP)
				{
					JOptionPane.showMessageDialog(null,  " Can not find Server /n check server ip and try again "  ,"Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				catch (IOException IOE) 
				{
					JOptionPane.showMessageDialog(null,  " Server is Down "  ,"Error", JOptionPane.WARNING_MESSAGE);
					try{
						toServer.close();
					}
					catch(IOException socketError){}
					return;
				}
			}
			catch (InputError e) 
			{
				JOptionPane.showMessageDialog(null,  " Your data is incorect \n " + Item  ,"Error", JOptionPane.WARNING_MESSAGE);
			
			}
            } 
            if (114 == evt.getKeyCode()) {
                        Name.setText("");
			Family.setText("");
			Noun.setSelected(true);
			Id.setText("");
			Pass.setText("");
			Repass.setText("");
                        Name.requestFocus();
            }
            if (27 == evt.getKeyCode())
			
                dispose();
              
         }

	public void keyReleased(KeyEvent arg0) {
	}
	
	public void actionPerformed(ActionEvent evt) 
	{
		ObjectOutputStream outPut;
		ObjectInputStream Input;
		JButton source=(JButton) evt.getSource();
		String result;
		if (source==Submit)
		{
			try
			{
				checkitems();
				String sex;
				if (Male.isSelected())
					sex="Male";
				else
					sex="Female";
				SignUpRequest UserInf= new SignUpRequest(Name.getText(),Family.getText(),sex,Id.getText(),new String(Pass.getPassword())); 
				try
				{
					toServer= new Socket(SignIn.serverIP,20202);
					outPut= new ObjectOutputStream(toServer.getOutputStream());
					outPut.writeObject(UserInf);
					Input= new ObjectInputStream(toServer.getInputStream());
					
                                        try
					{
						result=(String)Input.readObject();
                                        }
					catch(ClassNotFoundException CNF)
					{
						JOptionPane.showMessageDialog(null,  " Invalid response form server"   ,"Error", JOptionPane.WARNING_MESSAGE);
						toServer.close();
						return;
					}
					if(result.equals("URreg"))
					{
						JOptionPane.showMessageDialog(null,  " Thanks , your Registration was successfully   "    ,"Registration is complete", JOptionPane.INFORMATION_MESSAGE);
						toServer.close();
                                                dispose();
                                                return;
					}
					else
					{
						JOptionPane.showMessageDialog(null,  " Your Inputed ID was Reserved By Someone , Try agin ! \n "   ,"Error", JOptionPane.WARNING_MESSAGE);
						toServer.close();
                                                Name.setText("");
                                                Family.setText("");
                                                Noun.setSelected(true);
                                                Id.setText("");
                                                Pass.setText("");
                        			Repass.setText("");
                                                Name.requestFocus();
						return;
					}
				}
				catch(UnknownHostException InvalidIP)
				{
					JOptionPane.showMessageDialog(null,  " Can not find Server /n check server ip and try again "  ,"Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				catch (IOException IOE) 
				{
					JOptionPane.showMessageDialog(null,  " Server is Down "  ,"Error", JOptionPane.WARNING_MESSAGE);
					try{
						toServer.close();
					}
					catch(IOException socketError){}
					return;
				}
			}
			catch (InputError e) 
			{
				JOptionPane.showMessageDialog(null,  " Your data is incorect \n " + Item  ,"Error", JOptionPane.WARNING_MESSAGE);
			
			}
		
		}
		if (source==Reset)
		{
			Name.setText("");
			Family.setText("");
			Noun.setSelected(true);
			Id.setText("");
			Pass.setText("");
			Repass.setText("");
                        Name.requestFocus();
			
		}
		if (source== Back)
		{
			dispose();
		}	
			
	}	
	
	public void checkitems() throws InputError
	{
		int nameLe,familyLe,idLe,passLe,RePassLe;
		boolean maleVal,femaleVal;
		
		nameLe=Name.getText().length();
		familyLe=Family.getText().length();
		idLe= Id.getText().length();
		maleVal= Male.isSelected();
		femaleVal= Female.isSelected();
		
		Item=null;
		
		
		if ( Item==null && nameLe==0 )
		{
			Item =" Name  shuldnot be empty ";
		}
		if(Item==null && familyLe== 0)
		{	
			Item =" Family  shuldnot be empty  ";
		}	
		if (Item==null && maleVal==femaleVal )
		{	
			Item =" Specify your Gender  ";
		}	
		if(	Item==null && idLe==0 )
		{	
			Item =" Specify your ID   ";
		}	
		if (Item==null  )
		{	
			int pl,rpl;
			pl= Pass.getPassword().length;
			rpl= Repass.getPassword().length;
			if ( (pl>5) && (pl<11) )
			{	
				if (pl==rpl)
				{
					for (int i=0;i<pl;i++)
					{
						if (Pass.getPassword()[i]!=Repass.getPassword()[i])
						{
							Item="Password`s are not equal  ";
                                                        Pass.setText("");
                                                        Repass.setText("");
                                                        Pass.requestFocus();
						}
					}
				}
				else
                                {
                                    Item="password`s are not equal ";
                                    Pass.setText("");
                                    Repass.setText("");
                                    Pass.requestFocus();
                                }
			}
			else
                        {
				Item="password is too short or long ";
                                Pass.setText("");
                                Repass.setText("");
                                Pass.requestFocus();
                        }	
		
		if (Item != null)
		{	
			throw new InputError();
		}
		
				
	}

		
}
class InputError extends Exception
{
	
}
}