package skchew;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class WPConfig2 extends JPanel implements ListSelectionListener {

	private JList listAll;
	private JList listSelect;
	private DefaultListModel allModel;
	private DefaultListModel selModel;
	private ArrayList<Country> CountriesList;
	
	private static final String addString = "Add >>";
	private static final String remString = "<< Remove";
	private static final String closeString = "Close";
	private static final String clearString = "Clear all";
	private JButton addBtn, remBtn, closeBtn, clearBtn, temp;
	
	private String[] selectedC= { "" };
	private String[] countries= { "Australia", "Belgium", "Canada", "China", "Denmark", "Egypt", "France", "Germany", "Greece",
			"Hong Kong", "Hungary", "India", "Italy", "Japan", "Kenya", "Libya", "Mexico", "New Zealand", "North Korean", "Philippines", "Russia", 
			"South Africa", "South Korea", "Spain", "Turkey", "United Kingdom", "United States", "Vietnam"	};
	private Double[] latitude = { -25.274398, 50.503887 };
	private Double[] longitude = {133.775136, 4.469936};

	public WPConfig2() {
		
//		CountriesList = new ArrayList<Country>();
		
		//for (int i = 0; i < latitude.length; i++) {
		//	PopulateCountries(countries[i], latitude[i], longitude[i]);
		//}
		
		JPanel wpLPanel = new JPanel();			// list all, button panel, list select
		wpLPanel.setLayout(new BorderLayout());
		
		allModel = new DefaultListModel();
		selModel = new DefaultListModel();
		for (int i = 0; i < countries.length; i++){
//			allModel.addElement(CountriesList.get(i));
			allModel.addElement(countries[i]);
		}
		listAll = new JList(allModel);

		listAll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAll.setLayoutOrientation(JList.VERTICAL);
		listAll.setVisibleRowCount(5);
		listAll.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(listAll);
		listScroller.setPreferredSize(new Dimension(150,95));
//		wpLPanel.add (listScroller, BorderLayout.WEST);
		
		JPanel wpLPanel2 = new JPanel(); 		// buttons add rem
		wpLPanel2.setLayout(new BorderLayout());
		
//		wpLPanel2.add(new JButton ("add >>"), BorderLayout.NORTH);
		addBtn = new JButton (addString);
		AddListener addListener = new AddListener(addBtn);
		addBtn.setActionCommand(addString);
		addBtn.addActionListener(addListener);
//		addBtn.setEnabled(false);
		
		remBtn = new JButton (remString);
		RemListener remListener = new RemListener(remBtn);
		remBtn.setActionCommand(remString);
		remBtn.addActionListener(remListener);
//		remBtn.setEnabled(false);
//		wpLPanel2.add(addBtn, BorderLayout.NORTH);
		
		
//		wpLPanel2.add(new JButton("<< remove"), BorderLayout.SOUTH);
//		wpLPanel2.add(remBtn, BorderLayout.SOUTH);
		
//		wpLPanel.add(wpLPanel2, BorderLayout.CENTER);
		
		listSelect = new JList (selModel);
		listSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelect.setLayoutOrientation(JList.VERTICAL);
		listSelect.setVisibleRowCount(5);
		
		JScrollPane listScroller2 = new JScrollPane(listSelect);
		listScroller2.setPreferredSize(new Dimension(150,95));
		
		JPanel wpLPanel3 = new JPanel(); 	// button clear close
		wpLPanel3.setLayout(new GridLayout(2,3));
		clearBtn = new JButton(clearString);
		ClearListener clearListener = new ClearListener(clearBtn);
		clearBtn.setActionCommand(clearString);
		clearBtn.addActionListener(clearListener);
		
		closeBtn = new JButton (closeString);
		CloseListener closeListener = new CloseListener(closeBtn);
		closeBtn.setActionCommand(closeString);
		closeBtn.addActionListener(closeListener);
		
		wpLPanel.add (listScroller, BorderLayout.WEST);
		wpLPanel2.add(addBtn, BorderLayout.NORTH);
		wpLPanel2.add(remBtn, BorderLayout.SOUTH);
		wpLPanel.add(wpLPanel2, BorderLayout.CENTER);
		wpLPanel.add(listScroller2, BorderLayout.EAST);
		wpLPanel3.add(new JLabel("")); 
		wpLPanel3.add(new JLabel("")); 
		wpLPanel3.add(new JLabel("")); 
		wpLPanel3.add(new JLabel("")); 
		wpLPanel3.add(clearBtn);
		wpLPanel3.add(closeBtn);
		wpLPanel.add(wpLPanel3, BorderLayout.SOUTH);
		
		add(wpLPanel);
		
		
		}
	

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	class RemListener implements ActionListener {
		private JButton button;
		
		public RemListener(JButton button) {
			this.button = button;
			
		}
		public void actionPerformed (ActionEvent e) {
			int index = listSelect.getSelectedIndex();
			selModel.remove(index); 
			
		
			if (!listSelect.isSelectionEmpty()) {
				remBtn.setEnabled(true);
			
				listSelect.setSelectedIndex(index);
				listSelect.ensureIndexIsVisible(index);
				}
			
		} // actionperformed
		
	}
	
	class AddListener implements ActionListener {
		private boolean status = false;
		private JButton button;
		
		public AddListener(JButton button) {
			this.button = button;
			
		}
		public void actionPerformed (ActionEvent e)
		{
			Object temp;
			
			if (!listAll.isSelectionEmpty()){
				if (listSelect.getLastVisibleIndex() <= 5){
					temp = listAll.getSelectedValue();
					selModel.addElement(temp);
				}
			}
		}
	}

	
	class ClearListener implements ActionListener {
		private JButton button;
		
		public ClearListener(JButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
				selModel.clear();
		}
		
	}

	class CloseListener implements ActionListener {
		private JButton button;
		
		public CloseListener(JButton button){
			this.button = button;
			
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		
				
		}

	}

	public static void main (String args[]){
		
	}
	


}
