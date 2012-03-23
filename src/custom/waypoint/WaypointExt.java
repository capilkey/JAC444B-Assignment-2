package custom.waypoint;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;

public class WaypointExt extends Waypoint {
	String address = null;
	
	public WaypointExt() {
		super();
	}
	
	public WaypointExt(GeoPosition coord) {
		super(coord);
	}
	
	public WaypointExt(double latitude, double longitude) {
		super(latitude, longitude);
	}
	
	public void setAddress(String a) {
		address = a;
	}
	
	public String getAddress() {
		return address;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof WaypointExt && 
				((WaypointExt) o).getPosition().equals(getPosition());
	}
}
