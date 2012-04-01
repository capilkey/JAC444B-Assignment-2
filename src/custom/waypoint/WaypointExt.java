/* JAC444B Assignment 2
 * Group: 4
 * Authors:Chad Pilkey, Sabrina Chew, Nick Russell
 * Date: 31-March-2012
 */

package custom.waypoint;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;

/**
 * The WaypointExt class extends the Waypoint class to allow 
 * for a string to be added to facilitate mouseover functionality.
 * Also implements functionality to test for equality between
 * Waypoints.
 * @author Chad
 * @see org.jdesktop.swingx.mapviewer.Waypoint
 */
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
	
	/**
	 * The setAddress method is used to set the address 
	 * for a Waypoint .
	 * @param a is the string you want to set
	 */
	public void setAddress(String a) {
		address = a;
	}
	
	/**
	 * The getAddress method retrieves the address.
	 * @return returns the address of the Waypoint, might be null
	 */
	public String getAddress() {
		return address;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof WaypointExt && 
				((WaypointExt) o).getPosition().equals(getPosition());
	}
}
