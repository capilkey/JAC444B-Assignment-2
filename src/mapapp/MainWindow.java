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
setpanel = new settingsWindow(); 
setVisible(true);
setUpMenuBar();
setUpSettingsScreen();
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


//Container temp = this;
private void setUpSettingsScreen(){
     setpanel = new settingsWindow(); 
}


// all listeners and logic ******************************************************************************
public class settingsWindow extends JPanel{
	// define private radio buttons here for settings window
	  JRadioButton miniMapOff, miniMapOn, ZoomButtonOn, ZoomButtonOff,ZoomsliderOn,ZoomsliderOff;
	//button to save options and close window
	  JButton settingOk,settingClose;
	// array for options selected
      String[] optionsSet	 =  {"true","true","true"};	//default the optionsSet array
      String savedOptions;							//constructor for settings window
      JPanel p2;
    
    settingsWindow(){	
	try {												//try to read file containing settings
		savedOptions = readSetting();					// read the text file into string
		System.out.println("Settings read from file are: "+ savedOptions);
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
		//settings file not found
		System.out.println("file not found on computer");
		//savedOptions = "true,true,true,";						// file not found, use default settings
	}
	
	// check to see if the saved options are valid for the setting being set 
 if (ValidateTextfile(savedOptions)){ // true is saved options are valid
	 
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
	    			 //temp.remove(setpanel, BorderLayout.CENTER); 
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
	      }; 
	   
	   
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
		 miniMapOn = new JRadioButton("On",true);
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

}	// end of constructor for setpanel
	
    
    
private void refreshMap(){
	  mapKit.setMiniMapVisible(Boolean.parseBoolean(optionsSet[0]));
	  mapKit.setZoomButtonsVisible(Boolean.parseBoolean(optionsSet[1]));
	  mapKit.setZoomSliderVisible(Boolean.parseBoolean(optionsSet[2]));
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
	  String readSetting() throws IOException {
	 
	    StringBuilder text = new StringBuilder();
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
    
	   return text.toString(); 
	  }
}


private boolean ValidateTextfile(String s){ //**********************************************************
	return true; // temp setting, to be updated
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