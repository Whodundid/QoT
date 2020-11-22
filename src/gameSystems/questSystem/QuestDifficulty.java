package gameSystems.questSystem;

public enum QuestDifficulty {
	
	// todo ~
	
	EASY(""),
	MEDIUM(""),
	HARD(""),
	INSANE("");
	
	private String desc;
	
	QuestDifficulty(String description) {
		desc = description;
	}
	
	public String getDescription() { return desc; }
	
}
