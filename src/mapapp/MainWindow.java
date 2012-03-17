/**
 * 
 */
package mapapp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	private	JTextField searchField;
	private JComboBox country;
	private Geocode coder;
	/**
	 * 
	 */
	public MainWindow(){
		super("Map App");
		setVisible(true);
		
		setUpMenuBar();
		
		setUpMap();
		
		setUpSearchPanel();
		
		setSize(400, 400);
		searchField.requestFocusInWindow();
		
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
		
		add(mapKit, BorderLayout.CENTER);
	}
	
	private void setUpSearchPanel() {
		JButton searchButton = new JButton( "Search" );
		searchButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		
		searchField = new JTextField(20);	
		searchField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		
		String [] countries = { "CAN", "USA" };
		country = new JComboBox( countries );
		
		JPanel jp = new JPanel();
		
		jp.add(searchField, BorderLayout.WEST);
		jp.add(country, BorderLayout.CENTER);
		jp.add(searchButton, BorderLayout.EAST);
		add(jp, BorderLayout.SOUTH);
	}
	
	public void search() {		
		if (!searchField.getText().equals("") && country.getSelectedIndex() != -1) {
			if (coder.parseAddress(searchField.getText(), (String) country.getSelectedItem())) {
				mapKit.setAddressLocation(new GeoPosition(coder.getLat(), coder.getLon()));
				
				System.out.println(coder.getLat());
				System.out.println(coder.getLon());
			} else {
				
				System.out.println(coder.getErrCode());
				System.out.println(coder.getErrDesc());
			}
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
