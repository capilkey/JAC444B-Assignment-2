package custom.mapkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

public class CustomMapKit extends JXMapViewer {
	private JXMapViewer miniMap;
	
	public CustomMapKit() {
		super();
		
		setRestrictOutsidePanning(true);
		setLayout(new BorderLayout());
		
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

}
