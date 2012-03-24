package skchew;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import mapapp.*;

public class WayPoint extends JPanel{
private String name;
private Double latitude;
private Double longitude;
//private Integer zoomFactor;
private ArrayList<Object> allCountriesList;
private WayPoint[] wpBList;
private JButton jb1, jb2, jb3, jb4, jb5;

public WayPoint () {

wpBList = new WayPoint[10];
JLabel label = new JLabel ("WayPoint Quick Links");


jb1 = new JButton("1");
jb2 = new JButton("2");
jb3 = new JButton("3");
jb4 = new JButton("4");
jb5 = new JButton("5");

JPanel wpbPanel = new JPanel();

wpbPanel.add(label);
wpbPanel.setLayout (new GridLayout (6,1));

wpbPanel.add(jb1);
wpbPanel.add(jb2);
wpbPanel.add(jb3);
wpbPanel.add(jb4);
wpbPanel.add(jb5);

// Container c = getContentPane();

setSize(100, 500);

// c.setLayout(new FlowLayout());
add(wpbPanel);


}


public boolean setWayPoint(String n, Double lati, Double longi){
/*Integer zoom*/
name = n;
latitude = lati;
longitude = longi;
//zoomFactor = zoom;
boolean result = false;

return result;

}





}