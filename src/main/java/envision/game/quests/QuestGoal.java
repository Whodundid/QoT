package envision.game.quests;

public class QuestGoal {
	
	private String name, description;
	private boolean completed = false;
	
	public QuestGoal(String nameIn, String descriptionIn) {
		name = nameIn;
		description = descriptionIn;
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public boolean isComplete() { return completed; }
	
	//---------
	// Setters
	//---------
	
	public QuestGoal setComplete(boolean val) { completed = val; return this; }
	
}
