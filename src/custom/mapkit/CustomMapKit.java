package custom.mapkit;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

public class CustomMapKit extends JXMapViewer {
	private JXMapViewer miniMap;
	
	public CustomMapKit() {
		super();
		
		setUpMiniMap();
		
		setUpMapSource();
        
        
	}
	
	private void setUpMiniMap() {
		miniMap = new JXMapViewer();
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
