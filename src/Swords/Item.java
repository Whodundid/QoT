package Swords;

public abstract class Item {
	public String name;
	public double price;
	public String description;
	public int weight;

	public Item (String nameIn, double priceIn, String descriptionIn, int weightIn) {
		name = nameIn;
		price = priceIn;
		description = descriptionIn;
		weight = weightIn;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getWeight() {
		return weight;
	}
	
}
