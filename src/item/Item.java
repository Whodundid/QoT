package item;

public abstract class Item {
	protected int cost;
	protected int weight;
	protected String name;
	
	// Item Constructors
	
	public Item (int costIn, int weightIn, String nameIn) {
		cost = costIn;
		weight = weightIn;
		name = nameIn;
	}
	
	// Abstract Methods
	
	public abstract String getDescription();
	
	// Item Methods
	
	// Takes the player's barter level and returns a sell price based off of the level.
	public int getBarterPrice(int levelIn) {
		
		double percent = 0.3;
		
		if (levelIn < 25) {
			percent = 0.3;
		}
		
		else if (levelIn >= 25 && levelIn < 50) {
			percent = 0.45;
		}
		
		else if (levelIn >= 50 && levelIn < 75) {
			percent = 0.6;
		}
		
		else if (levelIn >= 75 && levelIn < 100) {
			percent = 0.75;
		}
		
		int sellPrice = (int) percent * cost;
		
		return sellPrice;
	}
	
	// Item Getters
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getWeight() {
		return weight;
	}
	
	// Item Setters
	
}
