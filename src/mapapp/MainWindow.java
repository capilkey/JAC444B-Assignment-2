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
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
private JTextField searchField;
private JComboBox<String> country;
private SettingsWindow setpanel;

//TODO FOLLOWING 6

private WPConfig wpConfig; 
private JButton jb1, jb2, jb3, jb4, jb5;
private WayPoint wpQuick;
private String[] countries = { "Australia", "Belgium", "Canada", "China", "England",
		"France", "Germany", "Greece", "Hong Kong", "Hungary", "India", "Italy",
		"Japan", "Mexico", "North Korea", "Philippines", "Russia", "South Africa",
		"South Korea", "Spain", "Turkey", "United States"	};
private double[] lati = { -25.274398, 50.503887, 56.130366, 35.86166, 52.355518, 
		46.227638, 51.165691, 39.074208, 22.396428,  47.162494, 
		20.593684, 41.87194, 36.204824, 23.634501, 40.339852,
		12.879721, 61.52401, -30.559482, 35.907757,
		40.463667, 38.963745, 37.09024 };
private double[] longi = {133.775136, 4.469936, -106.346771, 104.195397, -1.17432,
		2.213749, 10.451526, 21.824312, 114.109497, 19.503304,
		78.96288, 12.56738, 138.252924, -102.552784, 127.510093,
		121.774017, 105.318756, 22.937506, 127.766922,
		-3.74922, 35.243322, -95.712891 };
// END TO-DO 

/**
*
*/
public MainWindow(){
super("Map App");
setVisible(true);

setUpMenuBar();

setUpMap();

setUpSearchPanel();

setpanel = new SettingsWindow(); //settings screen created and set up

//TODO FOLLOWING SETUP

wpConfig = new WPConfig(); // waypoint configurations
wpQuick = new WayPoint(); // waypoint quick panel
add(wpQuick, BorderLayout.EAST);
// END TO-DO WAYPOINT SETUP

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

searchField.requestFocusInWindow();

setSize(800, 800);
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
menuItem.addActionListener(new ActionListener(){
@Override
public void actionPerformed(ActionEvent e) {
swapMainPanel(2);
}
});
menu.add(menuItem);

//TODO FOLLOWING MENU ITEM 
// MENU ITEM FOR WAYPOINT CONFIG
menuItem = new JMenuItem("WayPoint Configuration");
menu.add(menuItem);
menuItem.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e){
swapMainPanel(3);
}
}); 
// END TO DO FOR MENU ITEM

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

// TODO ADD CASE 3 FOR SWAP AND ADD MAPKIT TO CENTER
public void swapMainPanel(int panelToShow) {
switch (panelToShow) {
// show map
case 1:
remove(setpanel);
remove(wpConfig);
add(wpQuick, BorderLayout.EAST); // re-add quick links
setpanel.setVisible(false); //turn on settings panel
mapKit.setVisible(true); // turn off mapkit panel
break;
// show settings
case 2:
remove(setpanel);
remove(wpConfig);
wpQuick.setVisible(false);
add(setpanel, BorderLayout.CENTER); // panel is removed when closed so need to add
setpanel.setVisible(true); //turn on settings panel
mapKit.setVisible(false); // turn off mapkit panel
break;
// show wp config
case 3:
remove(setpanel);
wpQuick.setVisible(false);
add(wpConfig, BorderLayout.CENTER); // waypoint config panel
wpConfig.setVisible(true);
mapKit.setVisible(false); // turn off mapkit panel
break;
}
this.repaint();
// validate(); //update the window with latest info
}

public void close() {
WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
}

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
System.out.println("Settings read from file are:: " + savedOptions[0] +savedOptions[1] +savedOptions[2]+savedOptions[3] );
} catch (IOException e2) { //file could not be read
e2.printStackTrace();
try {
createTxtFile(); //create a new file because could not read or find one
savedOptions = readSetting(); //read in the saved options from file just created
} catch (IOException e) {
e.printStackTrace();
}
System.out.println("file not found on computer");
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
System.out.println("Slider Value: " + value);
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
System.out.println("text file is valid");
} else{
System.out.println("text file is NOT valid");
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
System.out.println(text);
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
String[] readSetting() throws IOException {
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
System.out.println("here" + java.util.Arrays.toString(nick.split(",")));
String[] settings = nick.split(",");
return settings;
}

private boolean ValidateTextfile(String[] savedOptions) { // check text file for valid entries
boolean valid = true;
if (savedOptions.length != 5){
System.out.println("number of elements is array: " + savedOptions.length);
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
System.out.println("mm off");
optionsSet[0] = "false";
} else if ( (e.getSource() == miniMapOn) &&
(e.getStateChange() == ItemEvent.SELECTED) ) {
System.out.println("mm on");
optionsSet[0] = "true";
} else if ( (e.getSource() == ZoomButtonOn) &&
(e.getStateChange() == ItemEvent.SELECTED) ) {
System.out.println("zoom on");
optionsSet[1] = "true";
} else if ( (e.getSource() == ZoomButtonOff) &&
(e.getStateChange() == ItemEvent.SELECTED) ){
System.out.println("zoom off");
optionsSet[1] = "false";
} else if ( (e.getSource() == ZoomsliderOn) &&
(e.getStateChange() == ItemEvent.SELECTED) ){
System.out.println("zoomslider on");
optionsSet[2] = "true";
} else if ( (e.getSource() == ZoomsliderOff) &&
(e.getStateChange() == ItemEvent.SELECTED) ){
System.out.println("zoomslider off");
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

//TODO WPCONFIG PANEL CLASS
/* author Sabrina
 * The WPConfig class extends the JPanel class and implements ListSelectionListener
 * forming the necessary components dedicated to customizing the waypoint quick links. 
 */
public class WPConfig extends JPanel implements ListSelectionListener {
	
private JList listAll;
private JList listSelect;
private DefaultListModel allModel;
private DefaultListModel selModel;

private JButton addBtn, remBtn, closeBtn, clearBtn;
private static final String addString = "Add >>";
private static final String remString = "<< Remove";
private static final String closeString = "Close";
private static final String clearString = "Clear all";

public WPConfig() {
//TODO WPCONFIG CONSTRUCTOR
/* author Sabrina
 * The only constructor for the WPConfig class and takes no arguments.
 * It handles the setup of the waypoint configurations panel
 */
JPanel jpMain = new JPanel();
jpMain.setLayout(new BorderLayout());

/////// HEADER ///////
JPanel jpHead = new JPanel();
jpHead.setLayout(new GridLayout(3,3));

jpHead.add(new JLabel ("")); // centering
jpHead.add(new JLabel ("WayPoint Configuration"));
jpHead.add(new JLabel (""));

jpHead.add(new JLabel ("")); // white line
jpHead.add(new JLabel (""));
jpHead.add(new JLabel (""));

jpHead.add (new JLabel ("Select your countries"));
jpHead.add(new JLabel (""));
jpHead.add (new JLabel ("Your quick link list"));

jpMain.add(jpHead, BorderLayout.NORTH);

////// LIST ALL////////
JPanel jpList1 = new JPanel(); // list all, button panel, list select
jpList1.setLayout(new BorderLayout());

allModel = new DefaultListModel();
selModel = new DefaultListModel();

for (int i = 0; i < countries.length; i++){
allModel.addElement(countries[i]);
}

listAll = new JList(allModel);

listAll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
listAll.setLayoutOrientation(JList.VERTICAL);
listAll.setVisibleRowCount(5);
listAll.addListSelectionListener(this);
JScrollPane listScroller = new JScrollPane(listAll);
listScroller.setPreferredSize(new Dimension(150,95));
jpList1.add (listScroller, BorderLayout.WEST);

jpMain.add(jpList1, BorderLayout.WEST);

///// BUTTONS ADD REMOVE ////

JPanel jpButtons1 = new JPanel();
jpButtons1.setLayout(new BorderLayout());

addBtn = new JButton (addString);
AddListener addListener = new AddListener(addBtn);
addBtn.setActionCommand(addString);
addBtn.addActionListener(addListener);
jpButtons1.add(addBtn, BorderLayout.NORTH);
addBtn.setEnabled(false);

remBtn = new JButton (remString);
RemListener remListener = new RemListener(remBtn);
remBtn.setActionCommand(remString);
remBtn.addActionListener(remListener);
jpButtons1.add(remBtn, BorderLayout.SOUTH);
remBtn.setEnabled(false);

jpMain.add(jpButtons1, BorderLayout.CENTER);

////// LIST SELECTED ////
JPanel jpList2 = new JPanel(); // list all, button panel, list select
jpList2.setLayout(new BorderLayout());
listSelect = new JList (selModel);
listSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
listSelect.setLayoutOrientation(JList.VERTICAL);
listSelect.setVisibleRowCount(5);
JScrollPane listScroller2 = new JScrollPane(listSelect);
listScroller2.setPreferredSize(new Dimension(150,95));
jpList2.add (listScroller2, BorderLayout.EAST);

listSelect.addListSelectionListener(new ListSelectionListener() {

	
	/**
	 * The setupMouseEvents() method assigns the mouseClicked and mouseMoved events. 
	 * When the user right-clicks on the map an appropriate popup menu is opened 
	 * depending on whether or not the user clicked on an existing waypoint. When 
	 * the mouse is moved a label is displayed if near a waypoint that has an address.
	 * @author Chad
	 */
	
/* The valueChanged method handles changes to the lists in the 
 * waypoint configuration panel, enabling the remove button when a
 * index has been selected from the listSelect list
 * author Sabrina
 */
	public void valueChanged(ListSelectionEvent e) {	
		if (e.getValueIsAdjusting() == false){
			if (listSelect.getSelectedIndex() == -1) {
				remBtn.setEnabled(false);
			}
			else {
				remBtn.setEnabled(true);
			}
		}
	}
	
});

jpMain.add(jpList2, BorderLayout.EAST);

JPanel jpButtons2 = new JPanel();
jpButtons2.setLayout(new GridLayout(2,3));

//BUTTON RESET //
clearBtn = new JButton(clearString);
ClearListener clearListener = new ClearListener(clearBtn);
clearBtn.setActionCommand(clearString);
clearBtn.addActionListener(clearListener);
clearBtn.setEnabled(false);

//BUTTON CLOSE //
closeBtn = new JButton (closeString);
CloseListener closeListener = new CloseListener(closeBtn);
closeBtn.setActionCommand(closeString);
closeBtn.addActionListener(closeListener);

jpButtons2.add(new JLabel(""));
jpButtons2.add(new JLabel(""));
jpButtons2.add(new JLabel(""));
jpButtons2.add(new JLabel(""));

jpButtons2.add(clearBtn);
jpButtons2.add(closeBtn);
jpMain.add(jpButtons2, BorderLayout.SOUTH);

add(jpMain);
}

//TODO VALUECHANGED METHOD
/* The valueChanged method handles changes to the lists in the 
 * waypoint configuration panel, enabling the add button when a
 * index has been selected from the listAll list
 * author Sabrina
 */
public void valueChanged(ListSelectionEvent e) {
	if (e.getValueIsAdjusting() == false){
		if (listAll.getSelectedIndex() == -1) {
			// nothing selected
			addBtn.setEnabled(false);
		}
		else {
			// selected
			addBtn.setEnabled(true);
		}
	}
}

//TODO CLASS REMLISTENER
/* author Sabrina
 * The RemListener class implements ActionListener and handles the
 * remove button action event. When the remove button is triggered,
 * the selected index is removed from the listSelect. If all selections are 
 * manually removed, the clear button is disabled. 
 */
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
else if (selModel.getSize() == 0){
	clearBtn.setEnabled(false);
}
}
} // RemListener

//TODO CLASS ADDLISTENER
/* author Sabrina
 * The AddListener class implements ActionListener and handles the
 * add button action event. When the add button is triggered,
 * the selected index is added to the listSelect and the clear button 
 * is enabled. Once there are 10 selections, the add button is disabled.
 */
class AddListener implements ActionListener {
private boolean status = false;
private JButton button;

public AddListener(JButton button) {
this.button = button;
}
public void actionPerformed (ActionEvent e)
{
Object temp;

if (!listAll.isSelectionEmpty() && selModel.getSize() < 10){
temp = listAll.getSelectedValue();
selModel.addElement(temp);
clearBtn.setEnabled(true);
}
else if (selModel.getSize() == 10){
	addBtn.setEnabled(false);
}
}
} // AddListener

//TODO CLASS CLEARLISTENER
/* author Sabrina
 * The ClearListener class implements ActionListener and handles the
 * clear button action event. When the clear button is triggered,
 * the selected index is cleared from the listSelect
 */
class ClearListener implements ActionListener {
private JButton button;

public ClearListener(JButton button) {
this.button = button;
}

public void actionPerformed(ActionEvent e) {
selModel.clear();
clearBtn.setEnabled(false);
}
} // ClearListener

//TODO CLASS CLOSELISTENER
/* author Sabrina
 * The CloseListener class implements ActionListener and handles the
 * close button action event. When the close button is triggered,
 * the WayPoint(selModel) is constructed and returns to
 * the main map view using the swapMainPanel(1) method.
 */
class CloseListener implements ActionListener {
private JButton button;

public CloseListener(JButton button){
this.button = button;
}

public void actionPerformed(ActionEvent e) {
wpQuick = new WayPoint(selModel);
swapMainPanel(1);
}
} // CloseListener

} // END CLASS WPCONFIG

//TODO WAYPOINT CLASS
/* author Sabrina
 * The Waypoint class extends the JPanel class and handles the quick link list
 * of the custom selection through the WPConfig panel.
 */
public class WayPoint extends JPanel{
private ArrayList<JButton> wpBtnArray;
private JPanel panel;

/* author Sabrina
 * The WayPoint() default constructor sets the quick links as empty slots 
 * and redirection is not set. 
 */
public WayPoint () {
//TODO WAYPOINT DEFAULT CONSTRUCTOR
wpBtnArray = new ArrayList<JButton>();

JLabel label = new JLabel ("Country Quick Links");

wpBtnArray.add(new JButton("Empty Slot"));
wpBtnArray.add(new JButton("Empty Slot"));
wpBtnArray.add(new JButton("Empty Slot"));
wpBtnArray.add(new JButton("Empty Slot"));
wpBtnArray.add(new JButton("Empty Slot"));

panel = new JPanel();

panel.add(label);
panel.setLayout (new GridLayout (6,1));

for (int i = 0; i < wpBtnArray.size(); i++) {
panel.add((JButton)wpBtnArray.get(i));
}

panel.setSize(100, 700);
add(panel);
}

/* author Sabrina
 * The WayPoint(DefaultListModel) constructor sets the quick links with the 
 * selected, and adds the listeners. If the size of the selected list is not 
 * greater than 0, this constructor sets the links back to the default links.
 */
public WayPoint(DefaultListModel selModel) {
//TODO WAYPOINT CONSTRUCTOR
wpBtnArray = new ArrayList<JButton>();
JLabel label = new JLabel ("Country Quick Links");
panel = new JPanel();
panel.add(label);
panel.setLayout (new GridLayout (11,1));

if (selModel.size() > 0){
	for (int i = 0; i < selModel.size() && i < 10; i++) {
		wpBtnArray.add(new JButton((String)selModel.getElementAt(i)));
	}

	for (int i = 0; i < wpBtnArray.size(); i++) {
		panel.add((JButton)wpBtnArray.get(i));
		BtnListener btnListener = new BtnListener(wpBtnArray.get(i));
		wpBtnArray.get(i).addActionListener(btnListener);
	}
}
else{
	wpBtnArray.add(new JButton("Empty Slot"));
	wpBtnArray.add(new JButton("Empty Slot"));
	wpBtnArray.add(new JButton("Empty Slot"));
	wpBtnArray.add(new JButton("Empty Slot"));
	wpBtnArray.add(new JButton("Empty Slot"));

	for (int i = 0; i < wpBtnArray.size(); i++) {
		panel.add((JButton)wpBtnArray.get(i));
	}
}

panel.setSize(100, 700);
panel.setMinimumSize(new Dimension(100, 700));
add(panel);
}
}

//TODO BTNLISTENER FOR QUICK LINKS
/* author Sabrina
 * The BtnListener class implements ActionListener and handles redirection of the map
 * when the quick list links are clicked. 
 */
class BtnListener implements ActionListener {
private JButton button;

	public BtnListener (JButton button) {
		this.button = button;
	}

	public void actionPerformed(ActionEvent e) {
		for (int j = 0; j < countries.length; j++) {
			if (countries[j].equals(e.getActionCommand()) ){
				mapKit.setAddressLocation(new GeoPosition(lati[j], longi[j]));
			}
		}
		mapKit.setZoom(12);
	}
}

/**
* @param args
*/
public static void main(String[] args) {
MainWindow mw = new MainWindow();
mw.search();

}

}