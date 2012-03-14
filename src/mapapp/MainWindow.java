/**
 * 
 */
package mapapp;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import java.io.*;


/**
 * @author Chad
 *
 */
public class MainWindow extends JFrame {
	JXMapKit mapKit;
	JMenuBar menuBar;
	
	/**
	 * 
	 */
	public MainWindow(){
		super("Map App");
		setVisible(true);
		setUpMenuBar();
		setUpMap();
		setSize(400, 400);
		
		try {
			createTxtFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
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
	private void createTxtFile() throws IOException{
		Writer writer = null;
		
		try {
			String text = "This is some text,and some more";
			File file = new File("settings.txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		try {
		if (writer != null) {
			writer.close();
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
	}
	
	private void setUpMap() {
		mapKit = new JXMapKit();		
		mapKit.setVisible(true);
		mapKit.setDefaultProvider(DefaultProviders.OpenStreetMaps);
		

		mapKit.setDataProviderCreditShown(true);
		mapKit.setMiniMapVisible(false);
		mapKit.setZoom(4);
		mapKit.setAddressLocation(new GeoPosition(41.881944,-87.627778));
	
		
		add(mapKit);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainWindow mw = new MainWindow();
		
	}
}
