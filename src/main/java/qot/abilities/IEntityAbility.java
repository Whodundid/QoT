package qot.abilities;

/**
 * An ability that an entity can activate, perform, has passively active
 * at all times or when specific conditions are met.
 * 
 * @author Hunter Bragg
 */
public interface IEntityAbility {
	
	/** The ability name. */
	String getAbilityName();
	/** Returns the tier of this ability in terms of the techtree. */
	int getAbilityTier();
	/** Returns true if this ability is active all the time. */
	boolean isPassive();
	/** Returns the duration (in ms) that this ability takes to recharge after being used. */
	long getAbilityCooldown();
	
	/**
	 * Returns the entity's level of this ability.
	 * Abilities which only have 1 level always return 1.
	 */
	int getAbilityLevel();
	
}
