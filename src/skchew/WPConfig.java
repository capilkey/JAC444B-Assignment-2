package skchew;

import java.awt.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WPConfig extends JPanel {
	private String name;
	private Double latitude;
	private Double longitude;
	//private Integer zoomFactor;
	private JList listAll, listSelect;
	private ArrayList<Object> allCountriesList;
	private WayPoint[] wpBList;
	private JButton jb1, jb2, jb3, jb4, jb5;
	
	private String[] countries= { "Afghanistan", "Algeria", "Argentina", "Australia", "Bahamas", "Barbados",
			"Belgium", "Brazil", "Canada", "China", "Costa Rica", "Cuba", "Denmark", "Egypt", "Ethiopia", 
			"Finland", "France", "Germany", "Greece", "Haiti", "Hong Kong", "Hungary", "India", "Indonesia",
			"Italy", "Jamaica", "Japan", "Kenya", "Libya", "Malaysia", "Mexico", "New Zealand", "Nigeria", 
			"North Korean", "Pakistan", "Philippines", "Russia", "South Africa", "South Korea", "Spain",
			"Sweden", "Turkey", "United Kingdom", "United States", "Vietnam"	};
	
	
	public WPConfig() {
		
		JLabel label = new JLabel ("WayPoint Configuration");
		
		JPanel wpCPanel = new JPanel();
		wpCPanel.setLayout (new BorderLayout());
			
		wpCPanel.add(label);
		
		
		JPanel wpLPanel = new JPanel();
		
		listAll = new JList (countries);
		listAll.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listAll.setLayoutOrientation(JList.VERTICAL);
		listAll.setVisibleRowCount(5);
		
		JScrollPane listScroller = new JScrollPane(listAll);
		listScroller.setPreferredSize(new Dimension(250,80));
		
		wpLPanel.add (listScroller);
		

		add(wpCPanel, BorderLayout.SOUTH);
		add(wpLPanel, BorderLayout.WEST);
		
		
	}
	
}
