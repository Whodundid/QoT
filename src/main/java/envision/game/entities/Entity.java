package envision.game.entities;

import java.util.HashMap;
import java.util.Map;

import envision.Envision;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.abilities.ActiveAbilityTracker;
import envision.game.abilities.EntitySpellbook;
import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.types.death.OnDeathComponent;
import envision.game.entities.inventory.EntityInventory;
import envision.game.entities.movement.MovementCollisionHelper;
import envision.game.entities.util.EntityHealthBar;
import envision.game.entities.util.EntityLevel;
import envision.game.items.Item;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Direction;
import eutil.strings.EStringBuilder;
import qot.abilities.Abilities;
import qot.particles.FloatingTextEntity;

public abstract class Entity extends ComponentBasedObject {
	
	//========
	// Fields
	//========
	
	public String headText = "";
	public String activeChat = "";
	public boolean passable = false;
	public boolean allowNoClip = false;
	
	/** This entity's current level. */
	public int level;
	/** The amount of experience rewarded when killed. */
	public long experienceRewarded = 0;
	/** This entity's total amount of experience earned. */
	public long experience;
	/** The amount of experience needed for the next immediate level up. */
	public long xpNeeded;
	/** The amount XP an entity would earn upon killing this entity. */
	public long experienceWorth;
	/** The maximum amount of health this entity can have. 'Determined by hitpoints level' */
	public int maxHealth;
	/** The actual current amount of health this entity has. */
	public int health;
	/** The maximum amount of mana this entity can have. 'Determined by magic level' */
	public int maxMana;
	/** The actual amount of mana this entity has. */
	public int mana;
	
	/** The amount of inventory slots this entity has. Default is 3. */
	public int baseInventorySize = 3;
	/** The entity's inventory. */
	public EntityInventory inventory;
	/** Instead of making the gold an entity has an inventory item, gold is stored directly
	 *  on the character themselves. */
	public int gold;
	
	/**
	 * The rate (in ms) that hitpoints will regen on their own without potions.
	 * Default is 1 hp every 20 seconds.
	 */
	public long hpRegenRate = 20000;
	/** The amount of hp that an entity will heal each time they regen. */
	public int hpRegenAmount = 1;
	/** The point in time that hp last regenerated. */
	public long lastRegenUpdate = 0l;
	
	public boolean invincible = false;
	
	public int strengthLevel = 0;
	public int hitpointsLevel = 0;
	public int magicLevel = 0;
	
	/**
	 * An entity's melee damage is calculated from their base strength
	 * level. Additional modifiers are added in later.
	 */
	public int baseMeleeDamage;
	
	// Stuff for keeping track of entity attacks
	public boolean attacking = false;
	public boolean recentlyAttacked = false;
	public long attackDrawStart;
	public long attackStart;
	public long timeUntilNextAttack = 300;
	public long recentlyAttackedTimeout = 3000l;
	
	// Stuff for keeping track of when an entity was last hit
	public boolean healthChanged = false;
	public int lastHealthChangeAmount = 0;
	public boolean drawnHealthChange = false;
	public long healthChangedTime;
	public long healthChangedTimeout = 3000l;
	
	public EntityHealthBar healthBar;
	public EList<IWindowObject> drawnObjects = EList.newList();
	
	public final MovementCollisionHelper collisionHelper;
	
	/**
	 * I am.. SPEED
	 * <p>
	 * Measured in pixels per ms.
	 */
	private double speed = (32 * 4.5) / 1000.0;
	
	public ActiveAbilityTracker abilityTracker;
	public EntitySpellbook spellbook;
	
	/** Temp thing to test out stuff. */
	public Map<String, Double> activeEffectsTracker = new HashMap<>();
	
	//--------------
	// Constructors
	//--------------
	
	public Entity() { this(null); }
	public Entity(String nameIn) {
		super(nameIn);
		
		inventory = new EntityInventory(this, baseInventorySize);
		
		//determine initial next level
		xpNeeded = EntityLevel.getXPNeededForNextLevel(level + 1);
		healthBar = new EntityHealthBar(this);
		
		collisionHelper = new MovementCollisionHelper(this);
		abilityTracker = new ActiveAbilityTracker(this, 5);
		spellbook = new EntitySpellbook(this);
	}
	
	//------------------
	// Back-end Methods
	//------------------
	
	public void init(int posX, int posY, int widthIn, int heightIn) {
		startX = posX;
		startY = posY;
		endX = posX + widthIn;
		endY = posY + heightIn;
		midX = posX + (widthIn) / 2;
		midY = posY + (heightIn) / 2;
		width = widthIn;
		height = heightIn;
		collisionBox = new Dimension_d(startX, startY, endX, endY);
	}
	
	/** Called from the world whenever an entity collides with another entity. */
	public void onEntityCollide(Entity collidingEntity) {}
	
	//---------
	// Methods
	//---------
	
	protected void healthChanged(int amount) {
		healthChanged = true;
		lastHealthChangeAmount = amount;
		healthChangedTime = System.currentTimeMillis();
	}
	
	/** Reduces health by amount. If result is less than or equal to 0, the entity dies. */
	public void drainHealth(int amount) {
		if (invincible) return;
		health = ENumUtil.clamp(health - amount, 0, health);
		
		if (health == 0 && maxHealth > 0) {
		    kill();
		}
		
		// TODO this should eventually be a game setting
		boolean drawDamageSplash = true;
		if (drawDamageSplash) {
			var dmg = new FloatingTextEntity(worldX, worldY, amount, 1500).setColor(EColors.red);
			Envision.getWorld().addEntity(dmg);
		}
		
		healthChanged(amount);
	}
	
	@Override
	public void onGameTick(float dt) {
		super.onGameTick(dt);
		
		if (healthChanged && (System.currentTimeMillis() - healthChangedTime >= healthChangedTimeout)) {
			healthChanged = false;
		}
		
		if (recentlyAttacked && (System.currentTimeMillis() - attackStart >= recentlyAttackedTimeout)) {
			recentlyAttacked = false;
		}
	}
	
	/** Reduces mana by amount. */
	public void drainMana(int amount) { mana = ENumUtil.clamp(mana - amount, 0, Integer.MAX_VALUE); }
	/** Heals the entity by the given amount. Note: does not exceed max health. */
	public void replenishHealth(int amount) { health = ENumUtil.clamp(health + amount, 0, maxHealth); healthChanged(amount); }
	/** Restores mana points by the given amount. Note: does not exceed max mana. */
	public void replenishMana(int amount) { mana = ENumUtil.clamp(mana + amount, Integer.MIN_VALUE, maxMana); healthChanged(amount); }
	/** Completely restores all hitpoints back to max health. Note: if max health = 0, the entity will still be dead! */
	public void fullHeal() {
		int diff = maxHealth - health;
		health = maxHealth;
		healthChanged(diff);
	}
	/** Completely restores all mana points back to max mana. */
	public void restoreMana() { mana = maxMana; }
	/** Reduces health to 0 effectively killing the entity. */
	public void kill() {
		int diff = health;
		health = 0;
		healthChanged(diff);
		if (this.hasComponent(ComponentType.ON_DEATH)) {
		    OnDeathComponent c = getComponent(ComponentType.ON_DEATH);
		    c.onDeath(null);
		}
	}
	
	public boolean giveItem(Item item) {
		if (item == null) return false;
		return getInventory().addItem(item);
	}
	
	public void dropItem(int index) {
		var item = inventory.getItemAtIndex(index);
		System.out.println("DROPPING: " + index + " : " + item);
		if (item == null) return;
		
		inventory.setItem(index, null);
		item.onItemUnequip(this);
	}
	
	public void move(Direction d) {
		switch (d) {
		case N: move(0, -1); break;
		case E: move(1, 0); break;
		case S: move(0, 1); break;
		case W: move(-1, 0); break;
		case NE: move(1, -1); break;
		case NW: move(-1, -1); break;
		case SE: move(1, 1); break;
		case SW: move(-1, 1); break;
		default: break;
		}
	}
	
	public void move(double x, double y) {
//		System.out.println("MOVING: " + x + " : " + y + " : " + Envision.getDeltaTime());
		collisionHelper.tryMove(x, y, Envision.getDeltaTime());
	}
	
	public void movePixel(double x, double y) {
	    collisionHelper.tryMovePixel(x, y);
	}
	
	/**
	 * Returns true if this entity actually exists within a loaded world.
	 * 
	 * @return true if in loaded world
	 */
	public boolean exists() {
		return world != null && world.isLoaded();
	}
	
	public void addXP(long xp) {
		experience += xp;
	}
	
	public void levelUp() {
		if (EntityLevel.checkLevelUp(level, experience)) {
			level++;
			xpNeeded = EntityLevel.getTotalXPNeeded(level + 1);
		}
	}
	
	/**
	 * @return True if this entity has less than or equal to zero '0' health remaining.
	 */
	public boolean isDead() {
		return health <= 0;
	}
	
	//---------
	// Getters
	//---------
	
	public boolean isPassable() { return passable; }
	public boolean isNoClipping() { return allowNoClip; }
	public String getHeadText() { return headText; }

	public int getLevel() { return level; }
	public long getExperience() { return experience; }
	public long getXPNeeded() { return xpNeeded; }
	
	public int getMaxHealth() { return maxHealth; }
	public int getHealth() { return health; }
	public int getHitpointsLevel() { return hitpointsLevel; }
	
	public int getMaxMana() { return maxMana; }
	public int getMana() { return mana; }
	public int getMagicLevel() { return magicLevel; }
	
	public int getBaseMeleeDamage() {
		int damage = baseMeleeDamage;
		for (int i = 0; i < inventory.size(); i++) {
			var item = inventory.getItemAtIndex(i);
			if (item != null) damage += item.getDamageBonus();
		}
		return damage;
	}
	
	public int getStrengthLevel() { return strengthLevel; }
	
	public int getBaseInventorySize() { return baseInventorySize; }
	public EntityInventory getInventory() { return inventory; }
	public int getGold() { return gold; }
	public boolean isInvincible() { return invincible; }
	public long getExperienceRewardedOnKill() { return experienceRewarded; }
	
	public MovementCollisionHelper getCollisionHelper() { return collisionHelper; }
	public ActiveAbilityTracker getAbilityTracker() { return abilityTracker; }
	public EntitySpellbook getSpellbook() { return spellbook; }
	
	//---------
	// Setters
	//---------
	
	public Entity setNoClipAllowed(boolean val) { allowNoClip = val; return this; }
	public Entity setPassable(boolean val) { passable = val; return this; }
	public Entity setCollisionBox(double sX, double sY, double eX, double eY) { collisionBox = new Dimension_d(sX, sY, eX, eY); return this; }
	public Entity setHeadText(Object... textIn) {
		var sb = new EStringBuilder();
		sb.a(textIn);
		return setHeadText(sb.toString());
	}
	public Entity setHeadText(String textIn) { headText = textIn; return this; }
	
	/** Instantaneously moves this entity to the target world coordinates.
	 *  Note: the entity must actually exist in a world for this to have any effect. */
	public Entity setWorldPos(int x, int y) {
		worldX = x;
		worldY = y;
		
		if (world != null) {
			startX = worldX * world.getTileWidth();
			endX = startX + width;
			startY = worldY * world.getTileHeight();
			endY = startY + height;
			
			//collisionBox.setPosition(startX + 20, startY + 20);
			
			midX = startX + (width) / 2;
			midY = startY + (height) / 2;
		}
		return this;
	}
	
	public Entity setPixelPos(double x, double y) {
	    startX = x;
	    startY = y;
	    endX = startX + width;
	    endY = startY + height;
	    midX = startX + width * 0.5;
	    midY = startY + height * 0.5;
	    
	    if (world != null) {
	        worldX = (int) (startX / world.getTileWidth());
	        worldY = (int) (startY / world.getTileHeight());
	    }
	    
	    return this;
	}
	
	public Entity setMaxHealth(int maxHealthIn) { maxHealth = maxHealthIn; return this; }
	public Entity setHealth(int healthIn) {
		int diff = healthIn - health;
		health = healthIn;
		healthChanged(diff);
		return this;
	}
	public void setHitpointsLevel(int levelIn) {
		hitpointsLevel = levelIn;
		maxHealth = EntityLevel.calculateMaxHealth(hitpointsLevel);
		fullHeal();
	}
	
	public Entity setMaxMana(int maxManaIn) { maxMana = maxManaIn; return this; }
	public Entity setMana(int manaIn) { mana = manaIn; return this; }
	public void setMagicLevel(int levelIn) {
		magicLevel = levelIn;
		maxMana = EntityLevel.calculateMaxMana(magicLevel);
		mana = ENumUtil.clamp(mana, 0, maxMana);
		
		// TODO remove this
		if (levelIn == 1) {
			this.spellbook.learnAbility(Abilities.selfHeal);
			this.abilityTracker.addAbility(Abilities.selfHeal);
		}
	}
	
	/**
	 * Sets this entity's melee damage to the exact amount specified.
	 * <p>
	 * Note: This completely disregards the entity's strength level.
	 * 
	 * @param damageIn
	 */
	public void setBaseMeleeDamage(int damageIn) {
		baseMeleeDamage = damageIn;
	}
	
	public void setStrengthLevel(int levelIn) {
		strengthLevel = levelIn;
		baseMeleeDamage = 1 + EntityLevel.calculateBaseDamage(levelIn);
	}
	
	public void setGold(int amountIn) {
		gold = amountIn;
	}
	
	public void setExperience(long expIn) {
		experience = expIn;
		level = EntityLevel.getLevelFromXP(experience);
		xpNeeded = EntityLevel.getTotalXPNeeded(level);
	}
	
	public void setLevel(int levelIn) {
		level = levelIn;
		experience = EntityLevel.getTotalXPNeeded(level);
		xpNeeded = EntityLevel.getTotalXPNeeded(level);
	}
	
	public Entity setDead(boolean val) {
		if (val) kill();
		else health = maxHealth; //if maxHealth is 0, the entity is still dead!
		return this;
	}
	
	public void setInvincible(boolean val) { invincible = val; }
	public void setExperienceRewardedOnKill(long amount) { experienceRewarded = amount; }
	
	/** Returns true if this entity had enough mana to use the given spell. */
	public boolean manaCheck(int spellCost) {
		if (mana >= spellCost) {
			return true;
		}
		return false;
	}
	
	public double getSpeed() { return speed; }
	
	/**
	 * Sets entity movement speed in terms of pixels traveled per second.
	 * 
	 * @param speed pixels per second
	 */
	public void setSpeed(double speed) {
	    this.speed = speed / 1000.0;
	}
	
}
