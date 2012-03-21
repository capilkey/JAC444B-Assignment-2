package settings;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.*;

import skchew.WPConfig2;
import mapapp.*;



public class settingsWindow extends JPanel{
	// define private radio buttons here
	private JRadioButton miniMapOff, miniMapOn;
	 	
	public settingsWindow(){

		
		// inner classes
		   class RadioHandler implements ItemListener { // an inner class
								                        // event sources: 2 radio buttons
								                       

		             public void itemStateChanged(ItemEvent e) {

						 if ( (e.getSource() == miniMapOff)  &&
		                      (e.getStateChange() == ItemEvent.SELECTED) ) 
                         	 System.out.println("mm off");
		                       
		                else if ( (e.getSource() == miniMapOn) &&
		                          (e.getStateChange() == ItemEvent.SELECTED) )
		                	       System.out.println("mm on"); 
	                     
		             }
		   }// end of class
		   
		
		// mini map panel
			 miniMapOn = new JRadioButton("On");
			 miniMapOn.setSelected(true);
			 miniMapOff = new JRadioButton("Off");
		//group radio buttons
		ButtonGroup miniMapgroup = new ButtonGroup();
		miniMapgroup.add(miniMapOn);
		miniMapgroup.add(miniMapOff);

		 // register one listener with 2 radio buttons
	      RadioHandler miniMaprh = new RadioHandler();

	      miniMapOn.addItemListener( miniMaprh );
	      miniMapOff.addItemListener( miniMaprh );
		
		JPanel miniMap = new JPanel(); 	
		miniMap.setLayout(new GridLayout(3,1));
		miniMap.add(new JLabel ("Mini Map"));
		miniMap.add(miniMapOn);
		miniMap.add(miniMapOff);
		add(miniMap);
		
		

		
		// Zoombuttons panel
				JRadioButton ZoomButtonOn = new JRadioButton("On");
				ZoomButtonOn.setSelected(true);
				JRadioButton ZoomButtonOff = new JRadioButton("Off");
				//group radio buttons
				ButtonGroup ZoomButtongroup = new ButtonGroup();
				ZoomButtongroup.add(ZoomButtonOn);
				ZoomButtongroup.add(ZoomButtonOff);
		     
				
				
				
				//miniMapOn.addActionListener();
				
				JPanel Zoombutton = new JPanel(); 	
				Zoombutton.setLayout(new GridLayout(3,1));
				Zoombutton.add(new JLabel ("Zoom Buttons"));
				Zoombutton.add(ZoomButtonOn);
				Zoombutton.add(ZoomButtonOff);
				add(Zoombutton);
				
				
		// Zoomslider panel
				JRadioButton ZoomsliderOn = new JRadioButton("On");
				ZoomsliderOn.setSelected(true);
				JRadioButton ZoomsliderOff = new JRadioButton("Off");
				//group radio buttons
				ButtonGroup Zoomslidergroup = new ButtonGroup();
				Zoomslidergroup.add(ZoomsliderOn);
				Zoomslidergroup.add(ZoomsliderOff);
			    //Register a listener for the radio buttons.
				//miniMapOn.addActionListener(this);
				
				JPanel Zoomslider = new JPanel(); 	
				Zoomslider.setLayout(new GridLayout(3,1));
				Zoomslider.add(new JLabel ("Zoom Slider"));
				Zoomslider.add(ZoomsliderOn);
				Zoomslider.add(ZoomsliderOff);
				add(Zoomslider);
		
				
				
				//panel p2 holds the panels for each group of items
				JPanel p2 = new JPanel();
				p2.setLayout(new GridLayout(1,5));
				p2.add(miniMap);
				p2.add(new JLabel (""));
				p2.add(Zoombutton);
				p2.add(new JLabel (""));
				p2.add(Zoomslider);
				p2.add(new JLabel (""));
				
				add(p2);
				
				
		
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
	
	
	
	

}
