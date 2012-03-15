package mapapp;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Geocode {
	private double lat;
	private double lon;
	
	private String address;
	
	private String errCode;
	private String errDesc;
	private String suggestion;
	
	private DocumentBuilderFactory factory;	
	private DocumentBuilder builder;
	
	public Geocode() {
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean reverseLookup(double la, double lo) {
		boolean errorFound = true;
		
		clear();
		
		String URL = "http://geocoder.ca/?latt="+la+"&longt="+lo+ 
							"&reverse=1&decimal=6&geoit=XML";
		
	    try {
			Document doc = builder.parse(URL);
			
			Element root = doc.getDocumentElement();
			
			NodeList geodata = root.getChildNodes();
			errorFound = false;
			
			String stnumber = null, staddress = null, prov = null, city = null, postal = null;
			
			// Go through all XML nodes and get the address info
			Node currNode = geodata.item(0);
			for (int i=0; currNode != null && !errorFound; currNode=geodata.item(++i)) {
				
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
					case "error":
						// Get the error 'code' and 'description' nodes
						NodeList error = currNode.getChildNodes();
						System.out.println ("Not a valid address");
						
						Node errorDetails = error.item(0);
						for (int j=0; errorDetails != null; errorDetails=error.item(++j)) {
							
							// Skip nodes that are "whitespace" nodes
							if (errorDetails.getNodeType() != Node.TEXT_NODE) {
								
								// show error code or description (depends on which node)
								switch (errorDetails.getNodeName()) {
								case "code":
									errCode = errorDetails.getFirstChild().getNodeValue();
									break;
								case "description":
									errDesc = errorDetails.getFirstChild().getNodeValue();
									break;
								}
								
								System.out.println (errorDetails.getFirstChild().getNodeValue());
							}
						}
	
						errorFound = true;
						
						break;
					}
				}
			}
			
			address = stnumber + " " + staddress + ", " + city + ", " + prov + ", " + postal;
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return errorFound;
	}
	
	public boolean parseAddress(String address, String country) {
		String [] parts = { "", "", "", "", "" };
		String [] addsplit = address.split(",");
		
		// trim the whitespace for each element
		// look for a postal code
		for (int i=0; i < addsplit.length; i++) {
			addsplit[i] = addsplit[i].trim();

			if (addsplit[i].matches("[a-zA-Z]\\d[a-zA-Z]\\d[a-zA-Z]\\d")) {
				parts[4] = addsplit[i];
			}
		}
		
		
		// no postal code found
		if (parts[4].equals("")) {
			switch (addsplit.length){
			case 2:
				parts[2] = replaceSpaces(addsplit[0]); 		//city
				parts[3] = replaceSpaces(addsplit[1]); 		//prov
				break;
			case 3:
				if (addsplit[0].matches("\\d* .*")) { // see if street number was provided
					String [] streetparts = addsplit[0].split(" ", 2);
					
					parts[0] = streetparts[0];					//number
					parts[1] = replaceSpaces(streetparts[1]);	//street
				} else {
					parts[1] = replaceSpaces(addsplit[0]);	//street
				}
				
				parts[2] = replaceSpaces(addsplit[1]); 		//city
				parts[3] = replaceSpaces(addsplit[2]); 		//prov
				
				break;
			}
		}
		
		System.out.println(parts[0]);
		System.out.println(parts[1]);
		System.out.println(parts[2]);
		System.out.println(parts[3]);
		System.out.println(parts[4]);
		
		return lookUpLatLon(parts);
	}
	
	private String replaceSpaces(String s) {
		StringBuffer temp = new StringBuffer(s);
		int i = temp.indexOf(" ");
		while (i != -1) {
			temp.replace(i, i+1, "+");
			i = temp.indexOf(" ");
		}
		
		return temp.toString();
	}
	
	private boolean lookUpLatLon(String [] parts) {
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
			for (int i=0; currNode != null && good; currNode=geodata.item(++i)) {
					
				switch (currNode.getNodeName()) {
				case "latt":
					lat = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					break;
				case "longt":
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return good;
	}
	
	private void clear() {
		lat=0;
		lon=0;
		address=null;
		errCode=null;
		errDesc=null;
		suggestion=null;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public String getErrCode() {
		return errCode;
	}
	
	public String getErrDesc() {
		return errDesc;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getSuggestion() {
		return suggestion;
	}
}
