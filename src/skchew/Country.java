package skchew;

import java.util.ArrayList;

public class Country {
	private String name;
	private Double latitude;
	private Double longitude;
	private String[] countries= { "Australia", "Belgium", "Canada", "China", "Denmark", "Egypt", "France", "Germany", "Greece",
			"Hong Kong", "Hungary", "India", "Italy", "Japan", "Kenya", "Libya", "Mexico", "New Zealand", "North Korean", "Philippines", "Russia", 
			"South Africa", "South Korea", "Spain", "Turkey", "United Kingdom", "United States", "Vietnam"	};
	private Double[] lati = { -25.274398, 50.503887 };
	private Double[] longi = {133.775136, 4.469936};

	public Country (String name, Double lati, Double longi) {
		this.name = name;
		this.latitude = lati;
		this.longitude = longi;
				
	}


}
