package skchew;

import java.awt.*;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class WPConfig extends JPanel {
	private String name;
	private Double latitude;
	private Double longitude;
	//private Integer zoomFactor;
	private JList listAll, listSelect;
	private ArrayList<Object> allCountriesList;
	private WayPoint[] wpBList;
	private JButton jb1, jb2, jb3, jb4, jb5;
	private String[] selectedC= { "" };
	private String[] countries= { "Afghanistan", "Algeria", "Argentina", "Australia", "Bahamas", "Barbados",
			"Belgium", "Brazil", "Canada", "China", "Costa Rica", "Cuba", "Denmark", "Egypt", "Ethiopia", 
			"Finland", "France", "Germany", "Greece", "Haiti", "Hong Kong", "Hungary", "India", "Indonesia",
			"Italy", "Jamaica", "Japan", "Kenya", "Libya", "Malaysia", "Mexico", "New Zealand", "Nigeria", 
			"North Korean", "Pakistan", "Philippines", "Russia", "South Africa", "South Korea", "Spain",
			"Sweden", "Turkey", "United Kingdom", "United States", "Vietnam"	};

	
	public WPConfig() {
		
		JPanel wpLPanel = new JPanel();
		wpLPanel.setLayout(new BorderLayout());

		wpLPanel.add(new JLabel ("WayPoint Configuration"), BorderLayout.NORTH);
		wpLPanel.add (new JLabel ("Select your countries"), BorderLayout.WEST);
		wpLPanel.add (new JLabel ("Your quick link list"), BorderLayout.EAST);

		add(wpLPanel);
		WPConfig2 panel2 = new WPConfig2();
		wpLPanel.add(panel2, BorderLayout.SOUTH);
				
	}
	
}
