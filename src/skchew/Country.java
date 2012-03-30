package skchew;

import java.util.ArrayList;

public class Country {
	private String name;
	private double latitude;
	private double longitude;
/*	private String[] countries= { "Australia", "Belgium", "Canada", "China", "England",
			"France", "Germany", "Greece", "Hong Kong", "Hungary", "India", "Italy",
			"Japan", "Mexico", "North Korea", "Philippines", "Russia", "South Africa",
			"South Korea", "Spain", "Turkey", "United States"	};
	public double[] lati = { -25.274398, 50.503887, 56.130366, 35.86166, 52.355518, 
							46.227638, 51.165691, 39.074208, 22.396428,  47.162494, 
							20.593684, 41.87194, 36.204824, 23.634501, 40.339852,
							12.879721, 61.52401, -30.559482, 35.907757,
							40.463667, 38.963745, 37.09024 };
	private double[] longi = {133.775136, 4.469936, -106.346771, 104.195397, -1.17432,
							2.213749, 10.451526, 21.824312, 114.109497, 19.503304,
							78.96288, 12.56738, 138.252924, -102.552784, 127.510093,
							121.774017, 105.318756, 22.937506, 127.766922,
							-3.74922, 35.243322, -95.712891 };
*/
	public Country (String name, double lati, double longi) {
		this.name = name;
		this.latitude = lati;
		this.longitude = longi;
				
	}
	

}
