package custom.mapkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.painter.Painter;

@SuppressWarnings("serial")
public class CustomMapKit extends JXMapViewer {
	private JXMapViewer miniMap;
	private boolean createWayPoint;
	private JSlider jsZoom;
	
	public CustomMapKit() {
		super();
		
		createWayPoint = true;
		
		setRestrictOutsidePanning(true);
		setLayout(new BorderLayout());
		addMouseListener(new MouseClick());
		try {
			this.setLoadingImage(ImageIO.read(new File("resources/loading.png")));
		} catch (IOException e) {
			System.out.println("loading.png not found");
		}
		
		setUpMiniMap();
		
		setUpZoomButtons();
		
		setUpMapSource();
        
		setZoom(10); // start at zoom level 7
		setAddressLocation(new GeoPosition(51.5,0));
	}
	
	private void setUpZoomButtons() {
		JButton plus = new JButton();
        
		plus.setIcon( new ImageIcon("resources/plus.png") );
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getZoom() > 0)
					setZoom(getZoom() - 1);
			}
		});
		
		JButton minus = new JButton();
		minus.setOpaque(false);
		minus.setIcon( new ImageIcon("resources/minus.png") );
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getZoom() < 15)
					setZoom(getZoom() + 1);
			}
		});
		
		jsZoom = new JSlider(JSlider.VERTICAL, 1, 15, 10);
		jsZoom.setMajorTickSpacing(1);
		jsZoom.setMinorTickSpacing(1);
		jsZoom.setPaintTicks(true);
		jsZoom.setOpaque(false);
		jsZoom.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setZoom(((JSlider) e.getSource()).getValue());
			}
		});

		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		
		jp.setLayout(new BorderLayout());
		jp.add(minus, BorderLayout.NORTH);
		jp.add(jsZoom, BorderLayout.CENTER);
		jp.add(plus, BorderLayout.SOUTH);
		jp.setOpaque(false);
		
		//jp2.setLayout(new BorderLayout());
		jp2.add(jp, BorderLayout.CENTER);
		jp2.setOpaque(false);
		
		add(jp2, BorderLayout.WEST);
	}
	
	private void setUpMiniMap() {
		miniMap = new JXMapViewer();
		
		miniMap.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		miniMap.setMinimumSize( new Dimension(250, 200) );
		miniMap.setPreferredSize( new Dimension(250, 200) );
		miniMap.setZoom(15);
		
		miniMap.setRestrictOutsidePanning(true);
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
                
				// make the minimap move with the main map
				map.setCenterPosition(getCenterPosition());
			}
		};
		
		// add the viewport painter to be painted
		miniMap.setOverlayPainter(viewportBox);
		
		add(jp, BorderLayout.SOUTH);
	}
	
	/**
	 * The TileFactory code was taken from the website 
	 * http://javafx.com/samples/ExercisingSwing/index.html
	 */
	private void setUpMapSource() {
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
	
	@Override
	public void setZoom(int zoom) {
		super.setZoom(zoom);
		
		miniMap.setZoom(zoom+3);
		jsZoom.setValue(zoom);
	}
	
	private class MouseClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// user wants to create a waypoint and they left-clicked
			if (createWayPoint && e.getButton() == MouseEvent.BUTTON1) {
				System.out.println(e.getX());
				System.out.println(e.getY());
				
				System.out.println(getZoom());
				System.out.println(miniMap.getZoom());
				
				// not accurate - off by a couple points
				//setAddressLocation( convertPointToGeoPosition(new Point2D.Double(e.getX(), e.getY())) );
				Point2D topcorner = getViewportBounds().getLocation(); // find the point to offset by
				setAddressLocation(getTileFactory().pixelToGeo(new Point2D.Double(
																	topcorner.getX()+e.getX(), 
																	topcorner.getY()+e.getY()),
															   getZoom()));
			}
			
		}

		// empty methods to satisfy the interface
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
}
