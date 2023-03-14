package envision.game.objects.entities;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.objects.CollisionHelper;
import envision.game.objects.GameObject;
import envision.game.objects.abilities.ActiveAbilityTracker;
import envision.game.objects.abilities.EntitySpellbook;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.WorldRenderer;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.EDimension;
import eutil.misc.Direction;
import eutil.misc.Rotation;
import eutil.strings.EStringBuilder;
import qot.abilities.Abilities;
import qot.particles.FloatingTextEntity;

public abstract class Entity extends GameObject {
	
	//--------
	// Fields
	//--------
	
	protected String headText = "";
	protected boolean passable = false;
	protected boolean allowNoClip = false;
	
	/** This entity's current level. */
	protected int level;
	/** The amount of experience rewarded when killed. */
	protected long experienceRewarded = 0;
	/** This entity's total amount of experience earned. */
	protected long experience;
	/** The amount of experience needed for the next immediate level up. */
	protected long xpNeeded;
	/** The amount XP an entity would earn upon killing this entity. */
	protected long experienceWorth;
	/** The maximum amount of health this entity can have. 'Determined by hitpoints level' */
	protected int maxHealth;
	/** The actual current amount of health this entity has. */
	protected int health;
	/** The maximum amount of mana this entity can have. 'Determined by magic level' */
	protected int maxMana;
	/** The actual amount of mana this entity has. */
	protected int mana;
	
	/** The amount of inventory slots this entity has. Default is 3. */
	protected int baseInventorySize = 3;
	/** The entity's inventory. */
	protected EntityInventory inventory;
	/** Instead of making the gold an entity has an inventory item, gold is stored directly
	 *  on the character themselves. */
	protected int gold;
	
	/**
	 * The rate (in ms) that hitpoints will regen on their own without potions.
	 * Default is 1 hp every 20 seconds.
	 */
	protected long hpRegenRate = 20000;
	/** The amount of hp that an entity will heal each time they regen. */
	protected int hpRegenAmount = 1;
	/** The point in time that hp last regenerated. */
	protected long lastRegenUpdate = 0l;
	
	protected boolean invincible = false;
	
	protected int strengthLevel = 0;
	protected int hitpointsLevel = 0;
	protected int magicLevel = 0;
	
	/**
	 * An entity's melee damage is calculated from their base strength
	 * level. Additional modifiers are added in later.
	 */
	protected int baseMeleeDamage;
	
	// Stuff for keeping track of entity attacks
	protected boolean attacking = false;
	protected boolean recentlyAttacked = false;
	protected long attackDrawStart;
	protected long attackStart;
	protected long timeUntilNextAttack = 300;
	protected long recentlyAttackedTimeout = 3000l;
	
	// Stuff for keeping track of when an entity was last hit
	protected boolean healthChanged = false;
	protected int lastHealthChangeAmount = 0;
	protected boolean drawnHealthChange = false;
	protected long healthChangedTime;
	protected long healthChangedTimeout = 3000l;
	
	protected EntityHealthBar healthBar;
	protected EList<IWindowObject> drawnObjects = EList.newList();
	
	protected final CollisionHelper collisionHelper;
	/** I am.. SPEED */
	public double speed = 32 * 4.5;
	
	protected ActiveAbilityTracker abilityTracker;
	protected EntitySpellbook spellbook;
	
	//--------------
	// Constructors
	//--------------
	
	public Entity(String nameIn) {
		super(nameIn);
		
		inventory = new EntityInventory(this, baseInventorySize);
		
		//determine initial next level
		xpNeeded = EntityLevel.getXPNeededForNextLevel(level + 1);
		healthBar = new EntityHealthBar(this);
		
		collisionHelper = new CollisionHelper(this);
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
		collisionBox = new EDimension(startX, startY, endX, endY);
	}
	
	/** Called from the world whenever an entity collides with another entity. */
	public void onEntityCollide(Entity collidingEntity) {}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		if (tex == null) return;
		
		double zoom = camera.getZoom();
		
		//pixel width of each tile
		double w = (int) (world.getTileWidth() * zoom);
		//pixel height of each tile
		double h = (int) (world.getTileHeight() * zoom);
		
		//the left most x coordinate for map drawing
		double x = (int) (midX - (distX * w) - (w / 2));
		//the top most y coordinate for map drawing
		double y = (int) (midY - (distY * h) - (h / 2));
		
		double entityOffsetX = (startX % world.getTileWidth()) * zoom;
		double entityOffsetY = (startY % world.getTileWidth()) * zoom;
		
		//transform the world coordinates of the entity to screen x/y coordinates
		double drawX = (worldX * w) + x;
		double drawY = (worldY * h) + y;
		
		//translate to the middle drawn world tile
		drawX += (distX - midDrawX) * w;
		drawY += (distY - midDrawY) * h;
		
		drawX += entityOffsetX;
		drawY += entityOffsetY;
		drawX -= camera.getOffsetX();
		drawY -= camera.getOffsetY();
		
		//calculate the entity's draw width and height based off of actual dims and zoom
		double drawW = width * zoom;
		double drawH = height * zoom;
		
		double cmx = collisionBox.midX; //collision mid x
		double cmy = collisionBox.midY; //collision mid y
		double tw = world.getTileWidth(); //tile width
		double th = world.getTileHeight(); //tile height
		
		//offset world coordinates to middle of collision box
		int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
		int mwcy = (int) Math.floor(cmy / th); //mid world coords y
		
		//light render position
		int lightx = worldX + mwcx;
		int lighty = worldY + mwcy;
		
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		boolean mouseOver = (mX >= drawX && mX <= drawX + drawW && mY >= drawY && mY <= drawY + drawH);
		
		drawEntity(world, drawX, drawY, drawW, drawH, calcBrightness(lightx, lighty), mouseOver);
		
		if (WorldRenderer.drawEntityHitboxes) {
			double colSX = drawX + (collisionBox.startX * zoom);
			double colSY = drawY + (collisionBox.startY * zoom);
			double colEX = colSX + (collisionBox.width * zoom);
			double colEY = colSY + (collisionBox.height * zoom);
			
			drawHRect(colSX - 1, colSY, colEX, colEY - 1, 1, EColors.yellow);
		}
		
		if (WorldRenderer.drawEntityOutlines) {
			drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
		}
	}
	
	public void drawEntity(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		boolean flip = facing == Rotation.RIGHT || facing == Rotation.DOWN;
		
		drawTexture(tex, x, y, w, h, flip, brightness);
		healthBar.setDimensions(x, y - 7, w, 7);
		healthBar.drawObject(Mouse.getMx(), Mouse.getMy());
		
		if ((recentlyAttacked || healthChanged) && !invincible && (health < maxHealth)) {
			healthBar.keepDrawing();
		}
		
		//headText = startX + " : " + startY;
		drawStringC(headText, x + w/2, y - h/4);
	}
	
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
		
		// TODO this should eventually be a game setting
		boolean drawDamageSplash = true;
		if (drawDamageSplash) {
			var dmg = new FloatingTextEntity(worldX, worldY, amount, 1500).setColor(EColors.red);
			Envision.getWorld().getEntitiesInWorld().add(dmg);
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
	}
	
	public void dropItem(int index) {
		System.out.println("DROPPING: " + index);
		inventory.setItem(index, null);
		
		// more needs to happen here lol
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
	
	public CollisionHelper getCollisionHelper() { return collisionHelper; }
	public ActiveAbilityTracker getAbilityTracker() { return abilityTracker; }
	public EntitySpellbook getSpellbook() { return spellbook; }
	
	//---------
	// Setters
	//---------
	
	public Entity setNoClipAllowed(boolean val) { allowNoClip = val; return this; }
	public Entity setPassable(boolean val) { passable = val; return this; }
	public Entity setCollisionBox(double sX, double sY, double eX, double eY) { collisionBox = new EDimension(sX, sY, eX, eY); return this; }
	public Entity setHeadText(Object... textIn) {
		var sb = new EStringBuilder();
		sb.a(textIn);
		return setHeadText(sb.toString());
	}
	public Entity setHeadText(String textIn) { headText = textIn; return this; }
	
	/** Instantaneously moves this entity to the target world coordinates.
	 *  Note: the entity must actually exist in a world for this to have any effect. */
	public Entity setWorldPos(int x, int y) {
		if (world != null) {
			worldX = x;
			worldY = y;
			
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
	
}
