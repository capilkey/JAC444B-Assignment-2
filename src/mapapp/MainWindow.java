/**
 * 
 */
package mapapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	private SettingsWindow setpanel;
	/**
	 * 
	 */
	public MainWindow(){
		super("Map App");
		setVisible(true);
		
		setUpMenuBar();
		
		setUpMap();
		
		setUpSearchPanel();
		
		setpanel = new SettingsWindow();  //settings screen created and set up
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		searchField.requestFocusInWindow();
		// TODO get screen resolution and set resolution 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
		this.setMinimumSize(new Dimension((int)(screenSize.width * 0.5),(int)(screenSize.height * 0.5)));
		setSize((int)(screenSize.width * 0.75),(int)(screenSize.height * 0.75));
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
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				swapMainPanel(2);
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();	
			}
		});
		menu.add(menuItem);
		
		// Help Menu
		menu = new JMenu("Help");
		menuBar.add(menu);
		//TODO
		
		menuItem = new JMenuItem("About");
		menuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mapKit, 
					    "Right-mouse click on map\n - to add a waypoint at current location\n- to re-center map\n" +
					    "Use Mouse wheel to Zoom\n" +
					    "\nMap Viewer Version 1.0\n" +
					    "Written by:\nChad Pilkey\nSabrina Chew\nNick Russell","Map App Help",JOptionPane.PLAIN_MESSAGE);
		
			}});// end of About menu item
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
				
				//System.out.println(mapKit.getCoder().getErrCode());
				//System.out.println(mapKit.getCoder().getErrDesc());
			}
		}
		
		
	}
	
	public void swapMainPanel(int panelToShow) {
		switch (panelToShow) {
		// show map
		case 1:
			//TODO change Sabrina's code to match this stuff
			//remove(setpanel);
			add(mapKit, BorderLayout.CENTER); // panel is removed when closed so need to add
			setpanel.setVisible(false); //turn on settings panel
			mapKit.setVisible(true); // turn off mapkit panel
			break;
		// show settings
		case 2:
			//remove(mapKit);		
			add(setpanel, BorderLayout.CENTER); // panel is removed when closed so need to add
			setpanel.setVisible(true); //turn on settings panel
			mapKit.setVisible(false); // turn off mapkit panel
			break;
		}
		
		validate(); //update the window with latest info
	}
	
	public void close() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	/**
	 *  @author Nick
	 *  @param optionsSet    An array containing the options selected for the settings of the map viewer. They are defined to a default state
	 *                       and updated each time the 'OK' button is pressed
	 *  @param savedOptions  An array containing the contents of a text file called settings.txt.                  
	 */
	public class SettingsWindow extends JPanel {
		// define private radio buttons here for settings window
		private JRadioButton miniMapOff, miniMapOn, ZoomButtonOn, ZoomButtonOff,ZoomsliderOn,ZoomsliderOff;
		//button to save options and close window
		private JButton settingOk,settingClose;
		//Slider bar for settings window
		private JSlider zoomLevel;
		// array for options selected, changes when screen options are changed by user
		private String[] optionsSet = {"true","true","true","8"}; //default the optionsSet array
		private String[] savedOptions; //array for setting in text file to be read into
		private JPanel p2; //panel for all options settings
		    
		public SettingsWindow() {
			try { //try to read file containing settings
				savedOptions = readSetting(); // read the text file into string
				//TODO
				//System.out.println("Settings read from file are:: " + savedOptions[0] +savedOptions[1] +savedOptions[2]+savedOptions[3] );
			} catch (IOException e2) { //file could not be read
				e2.printStackTrace();
				try {
					createTxtFile(); //create a new file because could not read or find one
					savedOptions = readSetting(); //read in the saved options from file just created
				} catch (IOException e) {
					e.printStackTrace();
				}
				//TODO
				//System.out.println("file not found on computer");
			}

			// set up slider
			zoomLevel = new JSlider(JSlider.HORIZONTAL,1,16,8); // min,max,default level
			zoomLevel.setBorder(BorderFactory.createTitledBorder("Zoom Level"));
			zoomLevel.setMajorTickSpacing(3);
			zoomLevel.setMinorTickSpacing(1);
			zoomLevel.setToolTipText("Change Magnification of Map View");
			zoomLevel.setSnapToTicks(true);
	
			zoomLevel.setPaintTicks(true);
			zoomLevel.setPaintLabels(true);
			add(zoomLevel);
		
		
				// Register a change listener
			zoomLevel.addChangeListener(new ChangeListener() {
				// This method is called whenever the slider's value is changed
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider)evt.getSource();
	
					if (!slider.getValueIsAdjusting()) {
						// Get new value
						int value = slider.getValue();
						//TODO
						//System.out.println("Slider Value: " + value);
						optionsSet[3] = "" + value;
					}
				}
			});
	
	
	
			      
			// set up okay button
			settingOk = new JButton("OK");
			settingOk.setToolTipText("Apply Current Settings and Save");
			settingClose = new JButton("Close");
			settingClose.setToolTipText("Return To Map With Last Saved Settings");
			// create a panel for the button , may add a second button
			JPanel setButton = new JPanel();
			setButton.setLayout(new GridLayout(3,2,1,1));
			setButton.add(settingOk);
			setButton.add(settingClose);
			add(setButton); // add panel

			//***** set up radio buttons in panel
			// mini map panel on/off radio buttons
			miniMapOn = new JRadioButton("On");
			miniMapOn.setSelected(true);
			miniMapOff = new JRadioButton("Off");
			//optionsSet[0] = "on"; //update setting array
			//group radio buttons
			ButtonGroup miniMapgroup = new ButtonGroup();
			miniMapgroup.add(miniMapOn);
			miniMapgroup.add(miniMapOff);
			//create a panel for the minimap radio buttons
			// panel is 3 rows x 1 column
			JPanel miniMap = new JPanel();
			miniMap.setLayout(new GridLayout(3,1));
			miniMap.add(new JLabel ("Mini Map"));
			miniMap.add(miniMapOn);
			miniMap.add(miniMapOff);
			add(miniMap); // add panel
			
			// Zoombuttons panel
			ZoomButtonOn = new JRadioButton("On",true);
			ZoomButtonOff = new JRadioButton("Off");
			//group radio buttons
			ButtonGroup ZoomButtongroup = new ButtonGroup();
			ZoomButtongroup.add(ZoomButtonOn);
			ZoomButtongroup.add(ZoomButtonOff);
			//create a panel for the Zoombutton radio buttons
			// panel is 3 rows x 1 column
			JPanel Zoombutton = new JPanel();
			Zoombutton.setLayout(new GridLayout(3,1));
			Zoombutton.add(new JLabel ("Zoom Buttons"));
			Zoombutton.add(ZoomButtonOn);
			Zoombutton.add(ZoomButtonOff);
			add(Zoombutton);


			// Zoomslider panel
			ZoomsliderOn = new JRadioButton("On",true);
			ZoomsliderOff = new JRadioButton("Off");
			//group radio buttons
			ButtonGroup Zoomslidergroup = new ButtonGroup();
			Zoomslidergroup.add(ZoomsliderOn);
			Zoomslidergroup.add(ZoomsliderOff);
			//create a panel for the Zoombutton radio buttons
			// panel is 3 rows x 1 column
			JPanel Zoomslider = new JPanel();
			Zoomslider.setLayout(new GridLayout(3,1));
			Zoomslider.add(new JLabel ("Zoom Slider"));
			Zoomslider.add(ZoomsliderOn);
			Zoomslider.add(ZoomsliderOff);
			add(Zoomslider);

			// register one listener with all radio buttons
			RadioHandler options = new RadioHandler();
			miniMapOn.addItemListener( options );
			miniMapOff.addItemListener( options );
			ZoomButtonOn.addItemListener( options );
			ZoomButtonOff.addItemListener( options );
			ZoomsliderOn.addItemListener( options );
			ZoomsliderOff.addItemListener( options );
					
			// register button listener
			ButtonHandler settingsButtons = new ButtonHandler();
			settingOk.addActionListener(settingsButtons);
			settingClose.addActionListener(settingsButtons);
			
			//panel p2 holds the panels for each group of items
			p2 = new JPanel();
			// one row and 5 columns
			p2.setLayout(new GridLayout(3,1,22,22));
			p2.add(miniMap);
			p2.add(Zoombutton);
			p2.add(Zoomslider);
			p2.add(zoomLevel);
			p2.add(setButton);
			add(p2);

			// check to see if the saved options are valid for the setting being set
			if (ValidateTextfile(savedOptions)){ //if saved options array is valid
				System.arraycopy(savedOptions, 0, optionsSet, 0, 4); //copy the it to the array used in memory
				refreshMap(); //call methods to update mapkit
				setRadioButtons();
				setSlider();
				//TODO 				
				//System.out.println("text file is valid");
			} else{
				//System.out.println("text file is NOT valid");
			}

		} // end of constructor for setpanel
   
		private void refreshMap(){
			mapKit.setMiniMapVisible(Boolean.parseBoolean(optionsSet[0]));
			mapKit.setZoomButtonsVisible(Boolean.parseBoolean(optionsSet[1]));
			mapKit.setZoomSliderVisible(Boolean.parseBoolean(optionsSet[2]));
			mapKit.setZoom(Integer.parseInt(optionsSet[3]));
		}
		
		private void setSlider(){
			zoomLevel.setValue(Integer.parseInt(optionsSet[3]));
		}
		
		private void setRadioButtons() { //sets the state of the radio buttons
			if (optionsSet[0].equals("true")){ //miniMap radio button is on
				miniMapOn.setSelected(true);
			} else{
				miniMapOff.setSelected(true);
			}
			if (optionsSet[1].equals("true")){ //Zoombutton radio button is on
				ZoomButtonOn.setSelected(true);
			} else{
				ZoomButtonOff.setSelected(true);
			}
			if (optionsSet[2].equals("true")){ //Zoomslider radio button is on
				ZoomsliderOn.setSelected(true);
			}else{
				ZoomsliderOff.setSelected(true);
			}
		}
		
		// write settings to text file on local drive
		private void createTxtFile() throws IOException{
			Writer writer = null;
			String text = "";
			try {
				for(int i = 0; i< optionsSet.length; i++){
					text += ( optionsSet[i] ); //optionsSet contains last saved settings
					text += ",";
				}
				//TODO
				//System.out.println(text);
				File file = new File("settings.txt");
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(text);
			} catch (FileNotFoundException e) {
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

		//TODO is this method supposed to be public or private?
		// read settings from text file saved on local drive
		/**@author Nick
		 * 
		 * @param  text is the variable which stores the text from the file settings.txt
		 * @return returns a comma delimited string containing the settings read from the file settings.txt
		 * @throws IOException if there is an error while reading the file settings.yxy
		 */
		private String[] readSetting() throws IOException {
			//String localArray[];
			StringBuffer text = new StringBuffer();
			String NL = System.getProperty("line.separator");
			Scanner scanner = new Scanner(new FileInputStream("settings.txt"));
			try {
				while (scanner.hasNextLine()) {
					text.append(scanner.nextLine() + NL);
				}
			}
			finally{
				scanner.close();
			}
			String nick = text.toString();
			//TODO
			//System.out.println("here" + java.util.Arrays.toString(nick.split(",")));
			String[] settings = nick.split(",");
			return settings;
		}
		/**
		 * @author Nick
		 * @param savedOptions is an array passed to the method.  The array contains the string read from the settings.txt file.  
		 *                     the file was comma delimited so the array should have 5 elements from the file. Only the first 4 are used as settings
		 * @return valid  is a boolean value return <code>true</code> if array contains valid settings for the program
		 *                will return <code> false </code> if there is an invalid number of elements or invalid values
		 */
		private boolean ValidateTextfile(String[] savedOptions) { // check text file for valid entries
			boolean valid = true;
			if (savedOptions.length != 5){
				//TODO
				//System.out.println("number of elements is array: " + savedOptions.length);
				valid = false; // wrong number of elements
			}
			for (int i=0; i< 3;i++) { // check first 3 elements for true or false
				if (savedOptions[i].equals("true") || savedOptions[i].equals("false")){
					// do nothing
				} else{
					valid = false; //option is not valid for the radio buttons
				}
				
			}
			
			return valid;
		}
		
		// inner classes
		class RadioHandler implements ItemListener { // an inner class for radio buttons
			public void itemStateChanged(ItemEvent e) {
				if ( (e.getSource() == miniMapOff) &&
						(e.getStateChange() == ItemEvent.SELECTED) ) {
					//TODO comment out all SYSTEM.out
					
					//System.out.println("mm off");
					optionsSet[0] = "false";
				} else if ( (e.getSource() == miniMapOn) &&
						(e.getStateChange() == ItemEvent.SELECTED) ) {
					//System.out.println("mm on");
					optionsSet[0] = "true";
				} else if ( (e.getSource() == ZoomButtonOn) &&
						(e.getStateChange() == ItemEvent.SELECTED) ) {
					//System.out.println("zoom on");
					optionsSet[1] = "true";
				} else if ( (e.getSource() == ZoomButtonOff) &&
						(e.getStateChange() == ItemEvent.SELECTED) ){
					//System.out.println("zoom off");
					optionsSet[1] = "false";
				} else if ( (e.getSource() == ZoomsliderOn) &&
						(e.getStateChange() == ItemEvent.SELECTED) ){
					//System.out.println("zoomslider on");
					optionsSet[2] = "true";
				} else if ( (e.getSource() == ZoomsliderOff) &&
						(e.getStateChange() == ItemEvent.SELECTED) ){
					//System.out.println("zoomslider off");
					optionsSet[2] = "false";
				}
			}
		}// end of RadioHandler class

		// inner class for button handler in settings window
		class ButtonHandler implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// determine which button was pressed
				if (e.getSource() == settingClose){
					swapMainPanel(1);
					//TODO
					System.out.println("close window");
				}
				if (e.getSource() == settingOk){
					refreshMap(); //make the mapkik method called with array settings
					try {
						createTxtFile(); //create a text file to save settings
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		};// end of button handler
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
		mw.search();
		
	}

}
