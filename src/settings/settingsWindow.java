package settings;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import javax.swing.*;

import skchew.WPConfig2;
import mapapp.*;
import java.util.Scanner;



//public class settingsWindow extends JPanel{
public class settingsWindow extends JPanel{
	// define private radio buttons here for settings window
	private JRadioButton miniMapOff, miniMapOn, ZoomButtonOn, ZoomButtonOff,ZoomsliderOn,ZoomsliderOff;
	//button to save options and close window
	 private JButton settingOk,settingClose;
	// array for options selected
    private String[] optionsSet	 =  {"true","true","true"};	//default the optionsSet array
    
    private String savedOptions;
    
    private JPanel p2;
	
	public settingsWindow(){   								//constructor for settings window
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
		    			 //p2.setVisible(false);//********************************************************problem
		    			 //p2.validate();	
		    			  System.out.println("close window");
		    		  }
		    		  if (e.getSource() == settingOk){
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
