/* JAC444B Assignment 2
 * Group: 4
 * Authors:Chad Pilkey, Sabrina Chew, Nick Russell
 * Date: 31-March-2012
 */

package mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The Geocode class serves as an interface between the geocoder.us 
 * and geocoder.ca geocoding services and reverse-geocoding services
 * @author Chad
 *
 */
public class Geocode {
	private double lat;
	private double lon;
	
	private String address;
	
	private String errCode;
	private String errDesc;
	private String suggestion;
	
	private DocumentBuilderFactory factory;	
	private DocumentBuilder builder;
	
	/**
	 * The default constructor handles the start up of the XML parser that 
	 * is used to interpret the returned XML pages.
	 * @author Chad
	 */
	public Geocode() {
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The reverseLookup method reverse-geocodes the passed latitude and 
	 * longitude and uses the geocoder.ca service. It works with both 
	 * Canadian and American locations.
	 * @author Chad
	 * @param la the latitude that you want to reverse-geocode
	 * @param lo the longitude that you want to reverse-geocode
	 * @return returns true if the lookup is successful, false otherwise
	 */
	public boolean reverseLookup(double la, double lo) {
		boolean good = false;
		
		clear();
		
		String URL = "http://geocoder.ca/?latt="+la+"&longt="+lo+ 
							"&reverse=1&allna=1decimal=6&geoit=XML";
		
	    try {
			Document doc = builder.parse(URL);
			
			Element root = doc.getDocumentElement();
			
			NodeList geodata = root.getChildNodes();
			Node currNode = geodata.item(0);
			if (currNode.getNodeType() == Node.TEXT_NODE)
				currNode = geodata.item(1); // if the first one is text take the second one
			
			geodata = currNode.getChildNodes();
			good = true;
			
			String stnumber = null, staddress = null, prov = null, city = null, postal = null;
			
			// Go through all XML nodes and get the address info
			currNode = geodata.item(0);
			for (int i=0; currNode != null && good; currNode=geodata.item(++i)) {
				short t = currNode.getNodeType();
				String t2 = currNode.getNodeName();

				// Skip nodes that are "whitespace" nodes
				if (currNode.getNodeType() != Node.TEXT_NODE) {
					
					switch (currNode.getNodeName()) {
					case "stnumber":
						stnumber = currNode.getFirstChild().getNodeValue();
						break;
					case "staddress":
						staddress = currNode.getFirstChild().getNodeValue();
						break;
					case "city":
						city = currNode.getFirstChild().getNodeValue();
						break;
					case "prov":
						prov = currNode.getFirstChild().getNodeValue();
						break;
					case "postal":
						postal = currNode.getFirstChild().getNodeValue();
						break;
					case "code":
						errCode = currNode.getFirstChild().getNodeValue();
						break;
					case "description":
						errDesc = currNode.getFirstChild().getNodeValue();
						break;
					}
				}
			}
			
			if (errCode == null) {
				address = stnumber + " " + staddress + ", " + city + ", " + prov;
			} else {
				good = false;
			}
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return good;
	}
	
	/**
	 * The parseAddress method breaks down the passed address into it's parts
	 * and sends them on to the lookUpLatLon methods. Works for both Canadian 
	 * and American addresses.
	 * @param address the address that you want to parse
	 * @param country the country where the address is located
	 * @return returns true if the parse and subsequent lookup was successful, false otherwise
	 */
	public boolean parseAddress(String address, String country) {
		
		if (country.equals("CAN")) {
			String [] parts = { "", "", "", "", "" };
			ArrayList<String> addSplit = new ArrayList<String>(Arrays.asList(address.split(",")));
			
			// trim the whitespace for each element
			// look for a postal code
			for (int i=0; i < addSplit.size(); i++) {
				addSplit.set(i, addSplit.get(i).trim());
				
				// remove the postal code from wherever it's found
				if (addSplit.get(i).matches("[a-zA-Z]\\d[a-zA-Z]\\d[a-zA-Z]\\d")) {
					parts[4] = addSplit.get(i);
					addSplit.remove(i);
					i--;
				}
			}
			
			switch (addSplit.size()){
			case 2:
				parts[2] = replaceSpaces(addSplit.get(0)); 		//city
				parts[3] = addSplit.get(1); 		//prov
				break;
			case 3:
				if (addSplit.get(0).matches("\\d* .*")) { // see if street number was provided
					String [] streetparts = addSplit.get(0).split(" ", 2);
					
					parts[0] = streetparts[0];					//number
					parts[1] = replaceSpaces(streetparts[1]);	//street
				} else {
					parts[1] = replaceSpaces(addSplit.get(1));	//street
				}
				
				parts[2] = replaceSpaces(addSplit.get(1)); 		//city
				parts[3] = addSplit.get(2); 		//prov
				
				break;
			}
			
			return lookUpLatLonCAN(parts);
		} else if (country.equals("USA")) {
			// if it's a US address just pass it the input address
			return lookUpLatLonUSA(replaceSpaces(address));
		} else
			return false;
	}
	
	/**
	 * The replaceSpaces method finds all the spaces in a string and replaces them with a '+'
	 * @param s the string you want to search
	 * @return returns the modified string
	 */
	private String replaceSpaces(String s) {
		StringBuffer temp = new StringBuffer(s);
		int i = temp.indexOf(" ");
		while (i != -1) {
			temp.replace(i, i+1, "+");
			i = temp.indexOf(" ");
		}
		
		return temp.toString();
	}
	
	/**
	 * the lookUpLatLonCAN method takes the parsed Canadian address and 
	 * sends the pieces to geocoder.ca. It them parses the returned XML 
	 * file and populates the instance variables of the Geocode class.
	 * @param parts the array of strings that contain the parts of the address
	 * @return returns true if the lookup was successful, false if there were errors
	 */
	private boolean lookUpLatLonCAN(String [] parts) {
		boolean good = false;
		
		clear();
		
		//String URL = "http://geocoder.ca/?stno=&addresst=&city=&prov=ON&postal=&decimal=6&geoit=XML";
		
		String URL = "http://geocoder.ca/?stno="+parts[0]+"&addresst="+parts[1]+"&city="+parts[2]
						+"&prov="+parts[3]+"&postal="+parts[4]+"&decimal=6&geoit=XML";

	    try {
			Document doc = builder.parse(URL);
			
			Element root = doc.getDocumentElement();
			
			// Get the latt and longt child nodes
			NodeList geodata = root.getChildNodes();
			good = true;
			
			// Go through all nodes and get the info
			Node currNode = geodata.item(0);
			for (int i=0; currNode != null; currNode=geodata.item(++i)) {
					
				switch (currNode.getNodeName()) {
				case "latt":
					if (good)
						lat = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					break;
				case "longt":
					if (good)
						lon = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					break;
				case "error":
					NodeList error = currNode.getChildNodes();
					
					Node errorDetails = error.item(0);
					for (int j=0; errorDetails != null; errorDetails=error.item(++j)) {
						
							// grab error info
						switch (errorDetails.getNodeName()) {
						case "code":
							errCode = errorDetails.getFirstChild().getNodeValue();
							break;
						case "description":
							errDesc = errorDetails.getFirstChild().getNodeValue();
							break;
						}
					}
					good = false;
					
					break;
				case "suggestion":
					NodeList suggest = currNode.getChildNodes();
					
					String stno = null, street = null, city = null, prov = null;
					
					Node suggestionDetails = suggest.item(0);
					for (int j=0; suggestionDetails != null; suggestionDetails=suggest.item(++j)) {
						
						switch (suggestionDetails.getNodeName()) {
						case "stno":
							stno = suggestionDetails.getFirstChild().getNodeValue();
							break;
						case "addresst":
							street = suggestionDetails.getFirstChild().getNodeValue();
							break;
						case "city":
							city = suggestionDetails.getFirstChild().getNodeValue();
							break;
						case "prov":
							prov = suggestionDetails.getFirstChild().getNodeValue();
							break;	
						}
					}
					suggestion = stno + " " + street + ", " + city + ", " + prov;
					
					break;
				}
			}
		
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return good;
	}
	
	/**
	 * the lookUpLatLonUSA method takes an American address and sends 
	 * it to geocoder.us. It them parses the returned XML file and 
	 * populates the instance variables of the Geocode class.
	 * @param address the American address to geocode
	 * @return returns true if the lookup was successful, false if there were errors
	 */
	private boolean lookUpLatLonUSA(String address) {
		
		clear();
		
		String URL = "http://geocoder.us/service/rest/geocode?address=" + address;
		
		try {
			builder.setErrorHandler(null); 		// Hide the [Fatal Error] that 
												//shows even with the catch statement
			Document doc = builder.parse(URL);
			
			Element root = doc.getDocumentElement();
			NodeList geodata = root.getChildNodes();
			
			Node currNode = geodata.item(1);
			geodata = currNode.getChildNodes();
			
			currNode = geodata.item(0);
			for (int i=0; currNode != null; currNode=geodata.item(++i)) {
					
				switch (currNode.getNodeName()) {
				case "geo:long":
					lon = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					break;
				case "geo:lat":
					lat = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					break;
				}
			}
			return true;
		} catch (SAXException e) { // the geocode.us site returns an improper file if there's a problem
			errCode = "004";
			errDesc = "Address not found, please check your spelling.";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * The clear method resets the instance variables to their starting values.
	 */
	private void clear() {
		lat=0;
		lon=0;
		address=null;
		errCode=null;
		errDesc=null;
		suggestion=null;
	}
	
	/**
	 * The getLat method retrieves the geocoded latitude
	 * @return returns the geocoded latitude
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * The getLon method retrieves the geocoded longitude
	 * @return returns the geocoded longitude
	 */
	public double getLon() {
		return lon;
	}
	
	/**
	 * The getErrCode method retrieves the  error code
	 * @return returns the error code
	 */
	public String getErrCode() {
		return errCode;
	}
	
	/**
	 * The getErrDesc method retrieves the error description
	 * @return returns the error description
	 */
	public String getErrDesc() {
		return errDesc;
	}
	
	/**
	 * The getAddress method retrieves the reverse-geocoded address
	 * @return returns the reverse-geocoded address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * The getSuggestion method retrieves the geocoded suggestion for partial matches
	 * @return returns the geocoded suggestion
	 */
	public String getSuggestion() {
		return suggestion;
	}
}
