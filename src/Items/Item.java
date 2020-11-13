package items;

public abstract class Item {
	
	protected String name;
	private double price;
	private String description;
	private double weight;

	public Item (String nameIn, double priceIn, String descriptionIn, double weightIn) {
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
	
	public double getWeight() {
		return weight;
	}
	
}
