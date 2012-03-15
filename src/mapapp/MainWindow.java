/**
 * 
 */
package mapapp;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * @author Chad
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private JXMapKit mapKit;
	private JMenuBar menuBar;
	Geocode coder;
	/**
	 * 
	 */
	public MainWindow(){
		super("Map App");
		setVisible(true);
		
		setUpMenuBar();
		
		setUpMap();
		
		setSize(400, 400);
		
		coder = new Geocode();
	}
	
	private void setUpMenuBar() {
		JMenu menu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();
		
		// File Menu
		menu = new JMenu("File");
		menuBar.add(menu);
		menuItem = new JMenuItem("Settings");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
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
		
		add(mapKit);
	}
	
	public void search() {		
		
		if (coder.parseAddress("Quebec City, QC", "boo")) {
			mapKit.setAddressLocation(new GeoPosition(coder.getLat(), coder.getLon()));
			
			System.out.println(coder.getLat());
			System.out.println(coder.getLon());
		} else {
			System.out.println(coder.getErrCode());
			System.out.println(coder.getErrDesc());
		}
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainWindow mw = new MainWindow();
		mw.search();
		
	}

}
