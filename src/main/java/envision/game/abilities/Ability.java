package envision.game.abilities;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.game.entities.Entity;

public abstract class Ability {
	
	protected String name;
	protected GameTexture texture;
	
	/**
	 * The maximum number of times this specific ability can be upgraded.
	 */
	protected int maxTiers;
	
	/**
	 * The requirements to unlock each tier of this ability.
	 */
	protected AbilityTier[] tiers;
	
	//==============
	// Constructors
	//==============
	
	public Ability(String nameIn) { this(nameIn, null); }
	public Ability(String nameIn, GameTexture textureIn) {
		name = nameIn;
		texture = textureIn;
	}
	
	//===========
	// Abstracts
	//===========
	
	/** @return True if the ability was used by the entity. */
	public boolean use(Entity e) { return use(e, 0); }
	/** @return True if the ability was used by the entity. */
	public abstract boolean use(Entity e, int tierIn);
	
	public abstract boolean canEntityUse(Entity e, int tierIn);
	
	public abstract boolean canEntityUpgrade(Entity e);
	
	public void updateAbility(long dt) {}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "ability:" + name;
	}
	
	//=========
	// Methods
	//=========
	
	protected void setNumTiers(int tiersIn) {
		this.maxTiers = tiersIn - 1;
		this.tiers = new AbilityTier[tiersIn];
	}
	
	protected AbilityTier tier(int tierIn) {
	    if (tierIn >= tiers.length) {
	        return tiers[tiers.length - 1];
	    }
		AbilityTier t = tiers[tierIn];
		if (t != null) return t;
		return tiers[tierIn] = new AbilityTier();
	}
	
	//=========
	// Getters
	//=========
	
	public String getAbilityName() { return name; }
	public GameTexture getTexture() { return texture; }
	
	public int getMaxTiers() { return maxTiers; }
	public AbilityTier[] getAbilityTiers() { return tiers; }
	public AbilityTier getDetailsForTier(int tier) { return tiers[tier];}
	
	public int getManaCostForTier(int tier) { return tiers[tier].manaCost(); }
	public int getCooldownForTier(int tier) { return tiers[tier].cooldown(); }
	public int getCastTimeForTier(int tier) { return tiers[tier].castTime(); }
	public int getDurationForTier(int tier) { return tiers[tier].duration(); }
	public int getStaminaCostForTier(int tier) { return tiers[tier].staminaCost(); }
	
}
