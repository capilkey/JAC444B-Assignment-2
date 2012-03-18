package custom.mapkit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

@SuppressWarnings("serial")
public class CustomMapKit extends JXMapViewer {
	private JXMapViewer miniMap;
	private boolean createWayPoint;
	
	public CustomMapKit() {
		super();
		
		createWayPoint = true;
		
		setRestrictOutsidePanning(true);
		setLayout(new BorderLayout());
		addMouseListener(new MouseClick());
		
		setUpMiniMap();
		
		setUpMapSource();
        
		setZoom(12);
		setAddressLocation( new GeoPosition( 0.00, 0.00) );
		miniMap.setAddressLocation( new GeoPosition( 0.00, 0.00) );
	}
	
	private void setUpMiniMap() {
		miniMap = new JXMapViewer();
		
		miniMap.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		miniMap.setMinimumSize( new Dimension(150, 100) );
		miniMap.setPreferredSize( new Dimension(150, 100) );
		miniMap.setZoom(15);
		
		miniMap.setRestrictOutsidePanning(true);
		
		miniMap.setMinimumSize(new java.awt.Dimension(200, 150));
        miniMap.setPreferredSize(new java.awt.Dimension(200, 150));
        
        JPanel jp = new JPanel();
        jp.setOpaque(false);
        jp.setLayout(new BorderLayout());
        jp.add(miniMap, BorderLayout.EAST);
        
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
                "http://tile.openstreetmap.org",
                "x","y","z") {
            public String getTileUrl(int x, int y, int zoom) {
                zoom = max-zoom;
                String url = this.baseURL +"/"+zoom+"/"+x+"/"+y+".png";
                return url;
            }

        };
        TileFactory tf = new DefaultTileFactory(info);
        setTileFactory(tf);
        tf = new DefaultTileFactory(info);
        miniMap.setTileFactory(tf);
	}

	
	private class MouseClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// user wants to create a waypoint and they left-clicked
			if (createWayPoint && e.getButton() == MouseEvent.BUTTON1) {
				System.out.println(e.getX());
				System.out.println(e.getY());
				
				setAddressLocation( convertPointToGeoPosition(new Point2D.Double(e.getX(), e.getY())) );
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
