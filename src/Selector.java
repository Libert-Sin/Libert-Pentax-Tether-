/**
 * @author Libert
 */

import static lpt.CameraControll.CONNECTION_MODE.MODE_USB;


import lpt.CameraControll.CONNECTION_MODE;
import lpt.gui.MainWindow;

import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Toolkit;
import java.awt.event.ActionListener;


public class Selector {

	private JFrame frame;
	private final Action selectusb = new SwingAction();

	public static void main(String[] args) 
	{		
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try {
					Selector window = new Selector();
					window.frame.setVisible(true);
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public Selector() 
	{
		initialize();
	}

	
	
	private void initialize() 
	{		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Selector.class.getResource("/resources/logo.png")));
		frame.setBounds(100, 100, 335, 215);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		
		JLabel photoguraphy = new JLabel("");
		photoguraphy.setIcon(new ImageIcon(Selector.class.getResource("/resources/black.jpg")));
		photoguraphy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	try
                {
                    Desktop.getDesktop().browse(new URI("https://www.photoguraphy.com/"));
                }
                catch (IOException ex)
                {
                	
                }       
        		catch(URISyntaxException e)
        		{
        		
        		};
            }
        });
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(photoguraphy, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(23, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(photoguraphy, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		panel.setLayout(null);
		
		JButton usb = new JButton("USB");
		usb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		usb.setBounds(12, 46, 120, 45);
		panel.add(usb);
		usb.setAction(selectusb);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	
	
	private class SwingAction extends AbstractAction 
	{
		public SwingAction() 
		{
			putValue(NAME, "연  결");

		}
		
		
		
		public void actionPerformed(ActionEvent e) 
		{
			final CONNECTION_MODE mode;
			mode = MODE_USB;
			java.awt.EventQueue.invokeLater(new Runnable()
	        {
	            public void run() 
	            {	
	                new MainWindow(mode).setVisible(true);
	            }
	        });
		}
	}

}
