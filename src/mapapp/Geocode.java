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
			
			// Get the latt and longt child nodes
			NodeList geodata = root.getChildNodes();
			errorFound = false;
			
			// Go through all XML nodes and get the latt and longt values
			// TODO Should only be recieving one node per request
			Node currNode = geodata.item(0);
			for (int i=0; currNode != null && !errorFound; currNode=geodata.item(++i)) {
				
				// Skip nodes that are "whitespace" nodes
				if (currNode.getNodeType() != Node.TEXT_NODE) {
					if (currNode.getNodeName().equals("latt")) {
						System.out.println ("Latitude: ");
						System.out.println (currNode.getFirstChild().getNodeValue());
					} else if (currNode.getNodeName().equals("longt")) {
						System.out.println ("Longitude: ");
						System.out.println (currNode.getFirstChild().getNodeValue());
					} else if (currNode.getNodeName().equals("error")) {
						// Get the error 'code' and 'description' nodes
						NodeList error = currNode.getChildNodes();
						System.out.println ("Not a valid address");
						
						Node errorDetails = error.item(0);
						for (int j=0; errorDetails != null; errorDetails=error.item(++j)) {
							
							// Skip nodes that are "whitespace" nodes
							if (errorDetails.getNodeType() != Node.TEXT_NODE) {
								
								// show error code or description (depends on which node)
								if (errorDetails.getNodeName().equals("code"))
									System.out.println ("Error code: ");
								if (errorDetails.getNodeName().equals("description"))
									System.out.println ("Error description: ");
		
								System.out.println (errorDetails.getFirstChild().getNodeValue());
							}
						}  // end for
	
						errorFound = true;
					}
				}

			}  // end for
			
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
		
		// TODO Write code to split up the address
		
		// temp code
		String [] parts = new String[10];
		return lookUpLatLon(parts);
	}
	
	private boolean lookUpLatLon(String [] parts) {
		boolean good = false;
		
		clear();
		
		String URL = "http://geocoder.ca/?stno=&address=Younge+Street&city=Toronto&prov=ON&postal=&decimal=6&geoit=XML";
		
	    try {
			Document doc = builder.parse(URL);
			
			Element root = doc.getDocumentElement();
			
			// Get the latt and longt child nodes
			NodeList geodata = root.getChildNodes();
			good = true;
			
			// Go through all XML nodes and get the latt and longt values
			// TODO Should only be recieving one node per request
			Node currNode = geodata.item(0);
			for (int i=0; currNode != null && good; currNode=geodata.item(++i)) {
				
				// Skip nodes that are "whitespace" nodes
				if (currNode.getNodeType() != Node.TEXT_NODE) {
					if (currNode.getNodeName().equals("latt")) {
						lat = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					} else if (currNode.getNodeName().equals("longt")) {
						lon = Double.parseDouble(currNode.getFirstChild().getNodeValue());
					} else if (currNode.getNodeName().equals("error")) {
						// Get the error 'code' and 'description' nodes
						NodeList error = currNode.getChildNodes();
						
						Node errorDetails = error.item(0);
						for (int j=0; errorDetails != null; errorDetails=error.item(++j)) {
							
							// Skip nodes that are "whitespace" nodes
							if (errorDetails.getNodeType() != Node.TEXT_NODE) {
								// show error code or description (depends on which node)
								if (errorDetails.getNodeName().equals("code"))
									errCode = errorDetails.getFirstChild().getNodeValue();
								if (errorDetails.getNodeName().equals("description"))
									errDesc = errorDetails.getFirstChild().getNodeValue();
							}
						}
	
						good = false;
					}
				}

			}  // end for
			
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
}
