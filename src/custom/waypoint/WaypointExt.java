package custom.waypoint;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;

public class WaypointExt extends Waypoint {
	
	public WaypointExt() {
		super();
	}
	
	public WaypointExt(GeoPosition coord) {
		super(coord);
	}
	
	public WaypointExt(double latitude, double longitude) {
		super(latitude, longitude);
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof WaypointExt && 
				((WaypointExt) o).getPosition().equals(getPosition());
	}
}
