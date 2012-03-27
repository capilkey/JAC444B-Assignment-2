/**
 * 
 */
package mapapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import org.jdesktop.swingx.mapviewer.GeoPosition;

import custom.mapkit.CustomMapKit;

/**
 * @author Chad
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private CustomMapKit mapKit;
	private JMenuBar menuBar;
	private	JTextField searchField;
	private JComboBox<String> country;

	/**
	 * 
	 */
	public MainWindow(){
		super("Map App");
		setVisible(true);
		
		setUpMenuBar();
		
		setUpMap();
		
		setUpSearchPanel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		this.setLocationRelativeTo(null);
		searchField.requestFocusInWindow();
		
		this.setMinimumSize(new Dimension(700, 700));
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
		mapKit = new CustomMapKit();		
		mapKit.setVisible(true);
        
		//mapKit.setDefaultProvider(DefaultProviders.OpenStreetMaps);
		
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
		country = new JComboBox<String>( countries );
		
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		jp.add(searchField, BorderLayout.WEST);
		jp.add(country, BorderLayout.CENTER);
		jp.add(searchButton, BorderLayout.EAST);
		add(jp, BorderLayout.SOUTH);
	}
	
	public void search() {		
		if (!searchField.getText().equals("") && country.getSelectedIndex() != -1) {
			if (mapKit.getCoder().parseAddress(searchField.getText(), (String) country.getSelectedItem())) {
				mapKit.setAddressLocation(new GeoPosition(mapKit.getCoder().getLat(), mapKit.getCoder().getLon()));
				
				//System.out.println(mapKit.getCoder().getLat());
				//System.out.println(mapKit.getCoder().getLon());
			} else {
				
				System.out.println(mapKit.getCoder().getErrCode());
				System.out.println(mapKit.getCoder().getErrDesc());
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
