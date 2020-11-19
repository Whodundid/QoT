package gameSystems.questSystem;

public class Quest {
	
	private String name;
	private String description;
	
	public Quest(String nameIn, String descriptionIn) {
		name = nameIn;
		description = descriptionIn;
	}
	
	// Getters
	
	public String getName() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	
	// Setters
	
	public void setName(String nameIn) {
		name = nameIn;
	}
	
	public void setDescription(String descriptionIn) {
		description = descriptionIn;
	}

}
