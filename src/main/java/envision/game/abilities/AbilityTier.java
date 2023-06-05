package envision.game.abilities;

public class AbilityTier {
	
	//========
	// Fields
	//========
	
	private int requiresLevel;
	private String requiresQuestCompletion;
	private String requiresQuestCompletionStage;
	
	/**
	 * The amount of mana it takes to cast this ability.
	 */
	private int manaCost;
	
	/**
	 * The number of game ticks that must pass before the ability can be used
	 * again.
	 */
	private int cooldown;
	
	/**
	 * The number of game ticks that must pass once casting has initiated
	 * before the ability actually gets activated.
	 */
	private int castTime;
	
	/**
	 * The amount of game ticks that this ability lasts for before going on cooldown.
	 */
	private int duration;
	
	//==============
	// Constructors
	//==============
	
	public AbilityTier() {}
	
	//===================
	// Getters | Setters
	//===================
	
	public int requiresLevel() { return requiresLevel; }
	public AbilityTier requiresLevel(int levelIn) {
		requiresLevel = levelIn;
		return this;
	}
	
	public String requiresQuestCompletion() { return requiresQuestCompletion; }
	public AbilityTier requiresQuestCompletion(String questName) {
		requiresQuestCompletion = questName;
		return this;
	}
	
	public String requiresQuestCompletionStage() { return requiresQuestCompletionStage; }
	public AbilityTier requiresQuestCompletionStage(String questName, String stage) {
		requiresQuestCompletion = questName;
		requiresQuestCompletionStage = stage;
		return this;
	}
	
	public int manaCost() { return manaCost; }
	public AbilityTier manaCost(int manaCost) {
		this.manaCost = manaCost;
		return this;
	}
	
	public int cooldown() { return cooldown; }
	public AbilityTier cooldown(int cooldown) {
		this.cooldown = cooldown;
		return this;
	}
	
	public int castTime() { return castTime; }
	public AbilityTier castTime(int castTime) {
		this.castTime = castTime;
		return this;
	}
	
	public int duration() { return duration; }
	public AbilityTier duration(int duration) {
		this.duration = duration;
		return this;
	}
	
}
