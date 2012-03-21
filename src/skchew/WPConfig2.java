package skchew;

import javax.swing.*;

import java.awt.*;

public class WPConfig2 extends JPanel {
private JList listAll, listSelect;
private String[] selectedC= { "" };
private String[] countries= { "Afghanistan", "Algeria", "Argentina", "Australia", "Bahamas", "Barbados",
"Belgium", "Brazil", "Canada", "China", "Costa Rica", "Cuba", "Denmark", "Egypt", "Ethiopia",
"Finland", "France", "Germany", "Greece", "Haiti", "Hong Kong", "Hungary", "India", "Indonesia",
"Italy", "Jamaica", "Japan", "Kenya", "Libya", "Malaysia", "Mexico", "New Zealand", "Nigeria",
"North Korean", "Pakistan", "Philippines", "Russia", "South Africa", "South Korea", "Spain",
"Sweden", "Turkey", "United Kingdom", "United States", "Vietnam" };

public WPConfig2() {

JPanel wpLPanel = new JPanel();
wpLPanel.setLayout(new BorderLayout());

listAll = new JList (countries);
listAll.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
listAll.setLayoutOrientation(JList.VERTICAL);
listAll.setVisibleRowCount(-1);
JScrollPane listScroller = new JScrollPane(listAll);
listScroller.setPreferredSize(new Dimension(150,80));
wpLPanel.add (listScroller, BorderLayout.WEST);

JPanel wpLPanel2 = new JPanel();
wpLPanel2.setLayout(new BorderLayout());
wpLPanel2.add(new JButton ("add >>"), BorderLayout.NORTH);
wpLPanel2.add(new JButton("<< remove"), BorderLayout.SOUTH);
wpLPanel.add(wpLPanel2, BorderLayout.CENTER);

listAll = new JList (selectedC);
listAll.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
listAll.setLayoutOrientation(JList.VERTICAL);
listAll.setVisibleRowCount(-1);
JScrollPane listScroller2 = new JScrollPane(listSelect);
listScroller2.setPreferredSize(new Dimension(150,80));
wpLPanel.add(listScroller2, BorderLayout.EAST);

add(wpLPanel);

}

}
