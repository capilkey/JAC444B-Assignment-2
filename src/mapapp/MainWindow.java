/*
*
*/
package mapapp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeEvent;  
import javax.swing.event.ChangeListener;  


import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;

import skchew.*;
//import settings.*;

/**
* @author Chad Pilkey
* Nick Russell
*
*/
public class MainWindow extends JFrame {
JXMapKit mapKit;				
settingsWindow setpanel;
Container temp = this;
public MainWindow(){

super("Map App");
setLayout (new BorderLayout());
setVisible(true);
setUpMenuBar();
setUpMap();								//mapkit is created	
setUpSettingsScreen();					//settings screen is created

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


//Container temp = this;
private void setUpSettingsScreen(){
     setpanel = new settingsWindow();  //settings screen created and set up
}


// all listeners and logic 
public class settingsWindow extends JPanel{
	// define private radio buttons here for settings window
	  JRadioButton miniMapOff, miniMapOn, ZoomButtonOn, ZoomButtonOff,ZoomsliderOn,ZoomsliderOff;
	//button to save options and close window
	  JButton settingOk,settingClose;
	//Slider bar for settings window  
	  JSlider zoomLevel;
	// array for options selected, changes when screen options are changed by user
      String[] optionsSet	 =  {"true","true","true","8"};	//default the optionsSet array
      String[] savedOptions;							//array for setting in text file to be read into
      JPanel p2;										//panel for all options settings
    
    settingsWindow(){	
	try {												//try to read file containing settings
		savedOptions = readSetting();					// read the text file into string
		System.out.println("Settings read from file are:: " + savedOptions[0] +savedOptions[1] +savedOptions[2]+savedOptions[3] );
	} catch (IOException e2) {							//file could not be read
		// TODO Auto-generated catch block
		e2.printStackTrace();
		try {
			createTxtFile();							//create a new file because could not read or find one
			savedOptions = readSetting();				//read in the saved options from file just created
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("file not found on computer");
		}
	

	
	// inner classes
	   class RadioHandler implements ItemListener { // an inner class for radio buttons
	             public void itemStateChanged(ItemEvent e) {
          	       if ( (e.getSource() == miniMapOff)  &&
	                      (e.getStateChange() == ItemEvent.SELECTED) ) {
                    	 System.out.println("mm off");
                    	optionsSet[0] = "false";  
					}else if ( (e.getSource() == miniMapOn) &&
	                          (e.getStateChange() == ItemEvent.SELECTED) ){
	                	       System.out.println("mm on"); 
	                	       optionsSet[0] = "true"; 
					}else if ( (e.getSource() == ZoomButtonOn) &&
	                          (e.getStateChange() == ItemEvent.SELECTED) ){
	                	       System.out.println("zoom on"); 
	                	       optionsSet[1] = "true"; 
					}else if ( (e.getSource() == ZoomButtonOff) &&
	                          (e.getStateChange() == ItemEvent.SELECTED) ){
	                	       System.out.println("zoom off"); 
	                	       optionsSet[1] = "false"; 
					}else if ( (e.getSource() == ZoomsliderOn) &&
	                          (e.getStateChange() == ItemEvent.SELECTED) ){
	                	       System.out.println("zoomslider on"); 
	                	       optionsSet[2] = "true"; 
					}else if ( (e.getSource() == ZoomsliderOff) &&
	                          (e.getStateChange() == ItemEvent.SELECTED) ){
	                	       System.out.println("zoomslider off"); 
	                	       optionsSet[2] = "false"; 
					}}
	   }// end of RadioHandler class
	   
	   
	   // inner class for button handler in settings window
	   class ButtonHandler implements ActionListener {
	    	  public void actionPerformed(ActionEvent e)
	    	  {
	    		  
	    		  // determine which button was pressed
	    		  if (e.getSource() == settingClose){
	    			 temp.remove(setpanel);
	    			 setpanel.setVisible(false);
	    			 mapKit.setVisible(true);
	    			 temp.validate();
	     			 System.out.println("close window");
	    		  }
	    		  if (e.getSource() == settingOk){
	    			  refreshMap();						//make the mapkik method called with array settings
	    		    try {
					  createTxtFile();					//create a text file to save settings						                               		  
			    	} catch (IOException e1) {
					// TODO Auto-generated catch block
				    	e1.printStackTrace();
				  }
	    		  }
	          }
	      };// end of button handler 
	      
// set up slider
	      zoomLevel  = new JSlider(JSlider.HORIZONTAL,1,16,8);  // min,max,default level
	      zoomLevel.setBorder(BorderFactory.createTitledBorder("Zoom Level"));
	      zoomLevel.setMajorTickSpacing(3);
	      zoomLevel.setMinorTickSpacing(1);
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
	                  System.out.println("Slider Value: " + value);  
	                  optionsSet[3] = "" + value;
	              }  
	          }  
	      });
	      
	      
	
      
	// set up okay button
	   settingOk = new JButton("OK");
	   settingClose = new JButton("Close");
   // create a panel for the button , may add a second button
	   JPanel setButton = new JPanel();   
	   setButton.setLayout(new GridLayout(3,2));  
	   setButton.add(settingOk);
	   setButton.add(settingClose);
	   add(setButton);						// add panel
	   
	//***** set up radio buttons in panel
	// mini map panel on/off radio buttons
		 miniMapOn = new JRadioButton("On");
		 miniMapOn.setSelected(true);
		 miniMapOff = new JRadioButton("Off");
		 //optionsSet[0] = "on";						//update setting array		
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
		 add(miniMap);								// add panel 

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
			p2.setLayout(new GridLayout(3,1));
			p2.add(miniMap);
			p2.add(Zoombutton);
			p2.add(Zoomslider);
			p2.add(setButton);
			add(p2);
			
			  // check to see if the saved options are valid for the setting being set 
		    if (ValidateTextfile(savedOptions)){							//if saved options array is valid
		    	System.arraycopy(savedOptions, 0, optionsSet, 0, 4);		//copy the it to the array used in memory
		        refreshMap();												//call methods to update mapkit
		        setRadioButtons();		
		        setSlider();
		       System.out.println("text file is valid");
		     }else{
		    	 System.out.println("text file is NOT valid");
		     }
		     
}	// end of constructor for setpanel
	
    
    
private void refreshMap(){
	  mapKit.setMiniMapVisible(Boolean.parseBoolean(optionsSet[0]));
	  mapKit.setZoomButtonsVisible(Boolean.parseBoolean(optionsSet[1]));
	  mapKit.setZoomSliderVisible(Boolean.parseBoolean(optionsSet[2]));
	  mapKit.setZoom(Integer.parseInt(optionsSet[3]));
}
private void setSlider(){
	zoomLevel.setValue(Integer.parseInt(optionsSet[3]));
}
private void setRadioButtons() {  //sets the state of the radio buttons 	
	if (optionsSet[0].equals("true")){											//miniMap radio button is on
		miniMapOn.setSelected(true);
	}else{
		miniMapOff.setSelected(true);
	}
	if (optionsSet[1].equals("true")){											//Zoombutton radio button is on
		ZoomButtonOn.setSelected(true);
	}else{
		ZoomButtonOff.setSelected(true);
	}
	if (optionsSet[2].equals("true")){											//Zoomslider radio button is on
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
				text += ( optionsSet[i] );		//optionsSet contains last saved settings
				text += ",";
			}
			System.out.println(text);
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
	

// read settings from text file saved on local drive
	  String[] readSetting() throws IOException {
	    //String localArray[];
		StringBuffer text = new StringBuffer();
	    String NL = System.getProperty("line.separator");
	    Scanner scanner = new Scanner(new FileInputStream("settings.txt"));
	    try {
	      while (scanner.hasNextLine()){
	         text.append(scanner.nextLine() + NL);
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    String nick = text.toString();
        System.out.println("here" + java.util.Arrays.toString(nick.split(",")));
        String[] settings = nick.split(",");
	   return settings;
	  }
}


private boolean ValidateTextfile(String[] savedOptions){ // check text file for valid entries
	boolean valid = true;
	if (savedOptions.length != 5){
		System.out.println("number of elements is array: " + savedOptions.length);
		valid = false;									// wrong number of elements
	}
	for (int i=0; i< 3;i++) {							// check first 3 elements for true or false
		if (savedOptions[i].equals("true") || savedOptions[i].equals("false")){
			//  do nothing
		}else{
			valid = false;								//option is not valid for the radio buttons
		}
		
	}
	return valid;
}

private void setUpMenuBar() {
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;

		// create menu bar
	      menuBar = new JMenuBar();

		// create  File Menu
	    menu = new JMenu("File");
		menuBar.add(menu);
		// **create item to add to File Menu
	      menuItem = new JMenuItem("Settings");
		  menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				  temp.add(setpanel, BorderLayout.CENTER); // panel is removed when closed so need to add
     			  setpanel.setVisible(true);			//turn on settings panel
				  mapKit.setVisible(false);				// turn off mapkit panel
				  temp.validate();						//update the window with latest info
	 			}
					catch (Exception e){
					e.printStackTrace();
					}
			}
			  
		  });
		 // add settings menu item to File Menu
		  menu.add(menuItem);
		  
		// **create item to add to File Menu
		  menuItem = new JMenuItem("WayPoint Configuration");
		  menu.add(menuItem);
		// **create item to add to File Menu
		  menuItem = new JMenuItem("Exit");
		  menu.add(menuItem);

		// create Help Menu
		  menu = new JMenu("Help");
		// add to menu bar
		  menuBar.add(menu);
		  
		// **create item to add to Help Menu
		  menuItem = new JMenuItem("About");
		  menu.add(menuItem);

		//enable menu bar on screen
		menuBar.setVisible(true);
		setJMenuBar(menuBar);
		}


	
private void setUpMap() {
mapKit = new JXMapKit();
mapKit.setVisible(true);
mapKit.setDefaultProvider(DefaultProviders.OpenStreetMaps);
add(mapKit, BorderLayout.CENTER);
temp.validate();	
}


/**
* @param args
*/
public static void main(String[] args) {
// TODO Auto-generated method stub
MainWindow mw = new MainWindow();


}

}