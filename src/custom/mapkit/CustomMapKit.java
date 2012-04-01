/* JAC444B Assignment 2
 * Group: 4
 * Authors:Chad Pilkey, Sabrina Chew, Nick Russell
 * Date: 31-March-2012
 */

package custom.mapkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mapapp.Geocode;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.Painter;

import custom.waypoint.WaypointExt;

/**
 * The CustomMapKit class extends the JXMapViewer class and
 * seeks to extend the latter's functionality by handling
 * Waypoint management as well as adding zoom buttons and
 * a minimap.
 * @author Chad
 * @see org.jdesktop.swingx.JXMapViewer
 */
@SuppressWarnings("serial")
public class CustomMapKit extends JXMapViewer {
	final private JXMapViewer miniMap = new JXMapViewer();
	final private JButton plus = new JButton(), minus = new JButton();
	final private JSlider jsZoom = new JSlider();
	final private JPopupMenu popupAdd = new JPopupMenu();
	final private JPopupMenu popupRemove = new JPopupMenu();
	final private JLabel address = new JLabel("test");
	private WaypointExt lastClicked;
	private Set wps = new HashSet();
	final private Geocode coder = new Geocode();
	
	/**
	 * This is the only constructor and takes no arguments.
	 * It handles all the set up of the map kit on its own.
	 * @author Chad
	 * 
	 */
	public CustomMapKit() {
		super();
		
		setRestrictOutsidePanning(true); // make sure you cant pan too far up or down
		setLayout(new BorderLayout());
		
		URL imgURL = getClass().getClassLoader().getResource("resources/loading.png");
		Image loadingImg = Toolkit.getDefaultToolkit().getImage(imgURL);
		setLoadingImage(loadingImg);
		
		address.setVisible(false);
		add(address);
		
		// different initialization sections broken up 
		// to increase readability
		setupMiniMap();
		setupZoomButtons();
		setupMapSource();
		setupPopupMenu();
		setupMouseEvents();
		
		setZoom(10); // start at zoom level 10
		setAddressLocation(new GeoPosition(43.701637,-79.392929)); // start with the map centered on Toronto
	}
	
	/**
	 * The setupMouseEvents() method assigns the mouseClicked and mouseMoved events. 
	 * When the user right-clicks on the map an appropriate popup menu is opened 
	 * depending on whether or not the user clicked on an existing waypoint. When 
	 * the mouse is moved a label is displayed if near a waypoint that has an address.
	 * @author Chad
	 */
	private void setupMouseEvents() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) { // user right-clicked
					Point2D topcorner = getViewportBounds().getLocation(); // find the point to offset by
					// calculate the actual clicked point on the map
					lastClicked = new WaypointExt(getTileFactory().pixelToGeo(new Point2D.Double(topcorner.getX()+e.getX(),topcorner.getY()+e.getY()), getZoom()));
					
					// determine if the user clicked near and existing waypoint
					WaypointExt near = waypointNear(lastClicked);
					if (near == null) 	// if they didnt click on a waypoint
										// let them create a new waypoint
						popupAdd.show(e.getComponent(), e.getX(), e.getY());
					else {				// if they did click on a waypoint
										// change where they clicked to register as 
										// them having clicked on the nearest waypoint
						lastClicked = near;
						popupRemove.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
		
		addMouseMotionListener( new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Rectangle view = getViewportBounds(); // find the point to offset by
				// calculate the actual on the map
				WaypointExt position = new WaypointExt(getTileFactory().pixelToGeo(new Point2D.Double(view.x+e.getX(),view.y+e.getY()), getZoom()));
				
				// determine if the mouse is near an existing waypoint
				WaypointExt near = waypointNear(position);
				
				if (near != null) {
					String a = near.getAddress();
					if (a != null) {
						Point2D gp_pt = getTileFactory().geoToPixel(near.getPosition(), getZoom());
						
						address.setBounds((int)gp_pt.getX()-view.x,(int)gp_pt.getY()-view.y, 400,20);
						address.setVisible(true);
						address.setText(a);
					} else
						address.setVisible(false);
				} else 
					address.setVisible(false);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// make the minimap move with the main map
				miniMap.setCenterPosition(getCenterPosition());
			}
			
			
		});
	}
	
	/**
	 * The setUpPopupMenu() method is solely for initializing the popup menus
	 * that show when the user right-clicks on the map and allow for addition
	 * of a waypoint at that location or removal of an already existing waypoint
	 * @author Chad
	 */
	private void setupPopupMenu() {
		//Create the popup menu.
	    JMenuItem menuItem = new JMenuItem("Add a waypoint");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addWaypoint(lastClicked);
				if (coder.reverseLookup(lastClicked.getPosition().getLatitude(),lastClicked.getPosition().getLongitude())) {
					lastClicked.setAddress(coder.getAddress());
				} //else {
					//System.out.println(coder.getErrCode() + " : " + coder.getErrDesc());
				//}
			}
	    });
	    popupAdd.add(menuItem);
	    menuItem = new JMenuItem("Center map here");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setAddressLocation(lastClicked.getPosition());
			}
	    });
	    popupAdd.add(menuItem);
	    
	    menuItem = new JMenuItem("Remove this waypoint");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeWaypoint(lastClicked);
			}
	    });
	    popupRemove.add(menuItem);
	    menuItem = new JMenuItem("Center map here");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setAddressLocation(lastClicked.getPosition());
			}
	    });
	    popupRemove.add(menuItem);
	}
	
	/**
	 * The setupZoomButtons() method is solely used for initialization of the
	 * two zoom buttons and the zoom slider bar. For zooming 0 means that you're
	 * zoomed all the way in and 15 (the max for OpenStreetMaps) is zoomed all
	 * the way out.
	 * @author Chad
	 */
	private void setupZoomButtons() {
		// the plus button increases the zooms one more level in
		plus.setIcon( new ImageIcon("src/resources/plus.png") ); // icon grabbed from JXMapKit
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getZoom() > 0)
					setZoom(getZoom() - 1);
			}
		});
		
		// the minus button reduces the zoom by one level
		minus.setIcon( new ImageIcon("src/resources/minus.png") ); // icon grabbed from JXMapKit
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getZoom() < 15)
					setZoom(getZoom() + 1);
			}
		});
		
		// vertical slider, min=1, max=15, initial=10
		jsZoom.setInverted(true);
		jsZoom.setMaximum(15);
		jsZoom.setMinimum(1);
		jsZoom.setValue(10);
		jsZoom.setOrientation(JSlider.VERTICAL);
		jsZoom.setMajorTickSpacing(1);
		jsZoom.setMinorTickSpacing(1);
		jsZoom.setPaintTicks(true);		// show the ticks
		jsZoom.setOpaque(false);		// make it see through
		jsZoom.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setZoom(((JSlider) e.getSource()).getValue()); // change the zoom level
			}
		});

		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		
		jp.setLayout(new BorderLayout());
		jp.add(plus, BorderLayout.NORTH);
		jp.add(jsZoom, BorderLayout.CENTER);
		jp.add(minus, BorderLayout.SOUTH);
		jp.setOpaque(false);
		
		jp2.add(jp, BorderLayout.CENTER);
		jp2.setOpaque(false);
		
		add(jp2, BorderLayout.WEST);
	}
	
	/**
	 * The setupMiniMap() method handles initialization of the minimap in the 
	 * bottom right hand corner.
	 * @author Chad
	 */
	private void setupMiniMap() {
		miniMap.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.RAISED)); // add a border so you can easily see it
		miniMap.setMinimumSize( new Dimension(250, 200) );
		miniMap.setPreferredSize( new Dimension(250, 200) );
		miniMap.setZoom(15);
		
		miniMap.setRestrictOutsidePanning(true);
		miniMap.setPanEnabled(false);
        //miniMap.setZoomEnabled(false);
		
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		jp.setLayout(new BorderLayout());
		jp.add(miniMap, BorderLayout.EAST);
		
		// create the view port box over the minimap
		Painter<JXMapViewer> viewportBox = new Painter<JXMapViewer>() {
			public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
				Rectangle viewport = getViewportBounds();
				
				//find the two corners of the main map
				Point2D nw = viewport.getLocation();
				Point2D se = new Point2D.Double(nw.getX() + viewport.getWidth(),
												nw.getY() + viewport.getHeight());
				
				// convert the points to geographic locations and then back to 
				// the point as it corresponds to the minimap
				//Point2D nw = map.convertGeoPositionToPoint(convertPointToGeoPosition(nw)); // these dont work 
				//Point2D se = map.convertGeoPositionToPoint(convertPointToGeoPosition(se)); // for some reason
				nw = map.getTileFactory().geoToPixel(getTileFactory().pixelToGeo(nw, getZoom()), map.getZoom());
				se = map.getTileFactory().geoToPixel(getTileFactory().pixelToGeo(se, getZoom()), map.getZoom());

                // graphics code taken from second mashup blog
				g = (Graphics2D) g.create();
				Rectangle rect = map.getViewportBounds();
				g.translate(-rect.x, -rect.y);
				
				Rectangle2D.Double shp = new Rectangle2D.Double(
						nw.getX(), nw.getY(), 		// x, y
						se.getX()-nw.getX(), 		// width
						se.getY()-nw.getY());		// height
				
				g.setColor(new Color(255,0,0,255));
				g.draw(shp);						
				g.setColor(new Color(255,100,0,75));
				g.fill(shp);

				g.dispose();
                
				
			}
		};
		
		// add the viewport painter to be painted
		miniMap.setOverlayPainter(viewportBox);
		
		add(jp, BorderLayout.SOUTH);
	}
	
	/**
	 * The TileFactory code was taken from the website and without it the map wouldn't load
	 * http://javafx.com/samples/ExercisingSwing/index.html
	 */
	private void setupMapSource() {
		final int max = 17;
        TileFactoryInfo info = new TileFactoryInfo(1,max-2,max,
                256, true, true, // tile size is 256 and x/y orientation is normal
                "http://tile.openstreetmap.org",//5/15/10.png",
                "x","y","z") {
            public String getTileUrl(int x, int y, int zoom) {
                zoom = max-zoom;
                String url = this.baseURL +"/"+zoom+"/"+x+"/"+y+".png";
                return url;
            }
            
        };
        TileFactory tf = new DefaultTileFactory(info);
        setTileFactory(tf);
        setZoom(11);
        
        tf = new DefaultTileFactory(info);
		miniMap.setTileFactory(tf);
	}
	
	/**
	 * The setZoom(int) method overrides JXMapViewers setZoom(int) and
	 * adds functionality for continuity with the minimap and with the
	 * zoom slider on the side.
	 * @description When setting a new zoom level 0 is zoomed all the way in and 15 is all the way out.
	 * @author Chad
	 * @param zoom the integer value that you want to set the new zoom to.
	 */
	@Override
	public void setZoom(int zoom) {
		super.setZoom(zoom);
		
		miniMap.setZoom(zoom+3); // keep the minimap 3 levels zoomed out from the main map if possible
		jsZoom.setValue(zoom);	 // keep the slider up to date with everything else
	}
	
	/**
	 * The addWaypoint(WaypointExt) method adds functionality for handling of 
	 * the waypoint drawing to compartmentalize the waypoint code and take the
	 * focus from the main window. 
	 * @description The method checks for duplicate Waypoints and rejects any duplicates
	 * @author Chad
	 * @param wp is the new waypoint to draw
	 * @return returns true if the waypoint was successfully added, false otherwise
	 */
	public boolean addWaypoint(WaypointExt wp) {
		if (!wps.contains(wp)) {
			wps.add(wp);
			WaypointPainter t = new WaypointPainter();
			t.setWaypoints(wps);
			setOverlayPainter(t);
			return true;
		} else
			return false;
	}
	
	/**
	 * The removeWaypoint() method attempts to remove the passed WaypointExt object.
	 * @author Chad
	 * @param wp is the waypoint to try and remove
	 * @return returns true if the waypoint was successfully removed, false otherwise
	 */
	public boolean removeWaypoint(WaypointExt wp) {
		/*
		System.out.println(wps.contains(wp));
		System.out.println(wps.remove(wp));
		System.out.println(wps.iterator().next().equals(wp));
		//Dont know why the above won't work
		*/
		
		// I have to jump through some hoops to try and remove a Waypoint because
		// the built-in Set object won't call my overridden Waypoint.equals(o) method
		ArrayList<WaypointExt> t1 = new ArrayList<WaypointExt>();
		t1.addAll(wps);
		
		if (t1.remove(wp)) { // if the wp is found reset painter
			wps = new HashSet();
			wps.addAll(t1);
			WaypointPainter t = new WaypointPainter();
			t.setWaypoints(wps);
			setOverlayPainter(t);
			return true;
		} else
			return false;
	}
	
	/**
	 * The WaypointExt(Point2D) method determines if the given Point2D is within 6 pixels(?)
	 * of an already existing Waypoint,
	 * @author Chad
	 * @param pt is the Point2D that you want to search for
	 * @return returns the closest Waypoint that it can find, null if none are found
	 */
	public WaypointExt waypointNear(WaypointExt check) {
		WaypointExt w = null;
		
		Point2D checkpt = getTileFactory().geoToPixel(check.getPosition(), getZoom());
		double longestDistance = 6; // start looking for waypoints closer then 6
		Object [] wparray = wps.toArray(); 	// it doesnt like getting cast to a Waypoint[]
											// so I have to cast each item when I use it
		
		for(int i=0; i<wparray.length; i++) {
			// translate each waypoint to a pixel location
			Point2D wppt = getTileFactory().geoToPixel(((WaypointExt) wparray[i]).getPosition(), getZoom());
			if (wppt.distance(checkpt) < longestDistance ) {
				w = (WaypointExt) wparray[i]; // if the waypoint is the closest record it
			}
		}
		
		return w;
	}
	
	/**
	 * The setAddressLocation method passes the location onto the super class method
	 * and sets the minimap to the same location.
	 * @author Chad
	 * @param addressLocation the location that you want to send the map to
	 */
	@Override
	public void setAddressLocation(GeoPosition addressLocation) {
		super.setAddressLocation(addressLocation);
		miniMap.setAddressLocation(addressLocation);
	}
	
	/**
	 * The setCenterPosition method passes the location onto the super class method
	 * and sets the minimap to the same location.
	 * @author Chad
	 * @param geoPosition the location that you want to send the map to
	 */
	@Override
	public void setCenterPosition(GeoPosition geoPosition) {
		super.setCenterPosition(geoPosition);
		miniMap.setCenterPosition(geoPosition);
	}
	
	/**
	 * The setCenter method passes the location onto the super class method
	 * and sets the minimap to the same location after taking into account zoom.
	 * @author Chad
	 * @param pt the location that you want to send the map to
	 */
	@Override
	public void setCenter(Point2D pt) {
		super.setCenter(pt);
		this.getTileFactory().pixelToGeo(pt, getZoom());
		miniMap.setCenter(miniMap.getTileFactory().geoToPixel(getTileFactory().pixelToGeo(pt, getZoom()), miniMap.getZoom()));
	}
	
	/**
	 * getCoder() returns a reference to the Geocode instance object in the mapkit.
	 * @return returns a reference to a Geocode object
	 */
	public Geocode getCoder() {
		return coder;
	}
	
	/**
	 * The setMiniMapVisible method turns the minimap on or off depending on 
	 * the value of the passed boolean.
	 * @param visible
	 */
	public void setMiniMapVisible(boolean visible) {
		miniMap.setVisible(visible);
	}
	
	/**
	 * The setZoomButtonsVisible method turns the minimap on or off depending on 
	 * the value of the passed boolean.
	 * @param visible
	 */
	public void setZoomButtonsVisible(boolean visible) {
		plus.setVisible(visible);
		minus.setVisible(visible);
	}
	
	/**
	 * The setZoomSliderVisible method turns the minimap on or off depending on 
	 * the value of the passed boolean.
	 * @param visible
	 */
	public void setZoomSliderVisible(boolean visible) {
		jsZoom.setVisible(visible);
	}
}
