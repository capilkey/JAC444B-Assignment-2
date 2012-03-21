/*
*
*/
package mapapp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;

import skchew.*;
import settings.*;

/**
* @author Chad
*
*/
public class MainWindow extends JFrame {
JXMapKit mapKit;
settingsWindow setpanel;
public MainWindow(){

super("Map App");
setLayout (new BorderLayout());
setpanel = new settingsWindow(); 
setpanel.setVisible(true);

setVisible(true);
setUpMenuBar();
setUpMap();


/*******************add back in 
/*try {
WPConfig panel = new WPConfig(); // waypoint config panel all
add(panel, BorderLayout.CENTER);
}
catch (Exception e){
e.printStackTrace();
}
**************************************/

try {
WayPoint panel = new WayPoint(); // waypoint right panel
add(panel, BorderLayout.EAST);
}
catch (Exception e){
e.printStackTrace();
}

setSize(800, 400);
}


JFrame temp = this;

private void setUpMenuBar() {
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;

		// create menu bar
	      menuBar = new JMenuBar();

		// create  File Menu
	    menu = new JMenu("File");
		menuBar.add(menu);

	    menuItem = new JMenuItem("Settings");
		menuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				  temp.add(setpanel, BorderLayout.CENTER);
				  setpanel.setVisible(true);
				  mapKit.setVisible(false);
				  temp.validate();
	 			}
					catch (Exception e){
					e.printStackTrace();
					}
			}
			  
		  });
		
		  menu.add(menuItem);

		  menuItem = new JMenuItem("WayPoint Configuration");
		  menu.add(menuItem);

		  menuItem = new JMenuItem("Exit");
		  menu.add(menuItem);

		// Help Menu
		menu = new JMenu("Help");
		menuBar.add(menu);

		  menuItem = new JMenuItem("About");
		  menu.add(menuItem);


		menuBar.setVisible(true);
		setJMenuBar(menuBar);
		}


	
private void setUpMap() {
mapKit = new JXMapKit();
mapKit.setVisible(true);
mapKit.setDefaultProvider(DefaultProviders.OpenStreetMaps);
add(mapKit, BorderLayout.CENTER);
}


/**
* @param args
*/
public static void main(String[] args) {
// TODO Auto-generated method stub
MainWindow mw = new MainWindow();


}

}