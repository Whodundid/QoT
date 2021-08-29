package assets.items;

import renderEngine.textureSystem.GameTexture;

public abstract class Item {
	
	protected String name;
	private double price;
	private String description;
	private double weight;
	private GameTexture texture;

	public Item (String nameIn, double priceIn, String descriptionIn, double weightIn) {
		name = nameIn;
		price = priceIn;
		description = descriptionIn;
		weight = weightIn;
	}
	
	public String getName() { return name; }
	public double getPrice() { return price; }
	public String getDescription() { return description; }
	public double getWeight() { return weight; }
	public GameTexture getTexture() { return texture; }
	
	public Item setTexture(GameTexture textureIn) { texture = textureIn; return this; }
	
}
