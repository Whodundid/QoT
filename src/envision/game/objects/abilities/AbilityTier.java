package envision.game.objects.abilities;

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
	private int tierManaCost;
	
	/**
	 * The number of game ticks that must pass before the ability can be used
	 * again.
	 */
	private int tierCooldown;
	
	/**
	 * The number of game ticks that must pass once casting has initiated
	 * before the ability actually gets activated.
	 */
	private int tierCastTime;
	
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
	
	public int tierManaCost() { return tierManaCost; }
	public AbilityTier tierManaCost(int cost) {
		tierManaCost = cost;
		return this;
	}
	
	public int tierCooldown() { return tierCooldown; }
	public AbilityTier tierCooldown(int cooldown) {
		tierCooldown = cooldown;
		return this;
	}
	
	public int tierCastTime() { return tierCastTime; }
	public AbilityTier tierCastTime(int castTime) {
		tierCastTime = castTime;
		return this;
	}
	
}
