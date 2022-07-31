package game.entities;

import eutil.debug.Broken;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import eutil.misc.Direction;
import eutil.misc.Rotation;
import game.GameObject;
import main.QoT;
import world.GameWorld;
import world.worldTiles.WorldTile;

public abstract class Entity extends GameObject {
	
	public GameWorld world;
	protected String headText = "";
	protected boolean passable = false;
	protected boolean allowNoClip = false;
	protected Rotation facing = Rotation.RIGHT;
	
	/** This entity's current level. */
	protected int level;
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
	/** Instead of making the gold an entity has an inventory item, gold is storred directly
	 *  on the character themselves. */
	protected int gold;
	
	protected boolean invulnerable = false;
	
	protected int strengthLevel = 0;
	protected int hitpointsLevel = 0;
	protected int magicLevel = 0;
	
	/**
	 * An entity's melee damage is calculated from their base strength
	 * level. Additional modifiers are added in later.
	 */
	protected int baseMeleeDamage;
	
	//--------------
	// Constructors
	//--------------
	
	public Entity(String nameIn) {
		super(nameIn);
		
		//determine initial next level
		xpNeeded = EntityLevel.getXPNeededForNextLevel(level + 1);
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
	
	/** Can be overridden in child classes to denote specific entity behavior. */
	public void onLivingUpdate() {}
	
	/** Called from the world whenever an entity collides with another entity. */
	public void onEntityCollide(Entity collidingEntity) {}
	
	//---------
	// Methods
	//---------
	
	/** Reduces health by amount. If result is less than or equal to 0, the entity dies. */
	public void drainHealth(int amount) {
		health = NumberUtil.clamp(health - amount, 0, health);
	}
	
	/** Reduces mana by amount. */
	public void drainMana(int amount) { mana = NumberUtil.clamp(mana - amount, 0, Integer.MAX_VALUE); }
	/** Heals the entity by the given amount. Note: does not exceed max health. */
	public void replenishHealth(int amount) { health = NumberUtil.clamp(health + amount, 0, maxHealth); }
	/** Restores mana points by the given amount. Note: does not exceed max mana. */
	public void replenishMana(int amount) { mana = NumberUtil.clamp(mana + amount, Integer.MIN_VALUE, maxMana); }
	/** Completely restores all hitpoints back to max health. Note: if max health = 0, the entity will still be dead! */
	public void fullHeal() { health = maxHealth; }
	/** Completely restores all mana points back to max mana. */
	public void restoreMana() { mana = maxMana; }
	/** Reduces health to 0 effectively killing the entity. */
	public void kill() { health = 0; }
	
	public void move(Direction d) {
		switch (d) {
		case N: move(0, -1); break;
		case E: move(1, 0); break;
		case S: move(0, 1); break;
		case W: move(-1, 0); break;
		case NE: move(1.41421356237, -1.41421356237); break;
		case NW: move(-1.41421356237, -1.41421356237); break;
		case SE: move(1.41421356237, 1.41421356237); break;
		case SW: move(-1.41421356237, 1.41421356237); break;
		default: break;
		}
	}
	
	// broken due to not allowing for movement in any direction greater than 1
	@Broken
	public void move(double x, double y) {
		if (world != null) {
			x = Math.signum(x);
			y = Math.signum(y);
			boolean stopMove = false;
			
			//if (this != QoT.thePlayer) return;
			
			if (!allowNoClip) {
				boolean left = false, right = false, up = false, down = false;
				double w = world.getTileWidth();
				double h = world.getTileHeight();
				
				if (x < 0) 			left = true;
				else if (x > 0) 	right = true;
				if (y < 0) 			up = true;
				else if (y > 0) 	down = true;
				
				if (left) 		facing = Rotation.LEFT;
				if (right) 		facing = Rotation.RIGHT;
				if (up) 		facing = Rotation.UP;
				if (down) 		facing = Rotation.DOWN;

				//System.out.println(collisionBox.endX);
				
				double cSX = startX + collisionBox.startX;
				double cSY = startY + collisionBox.startY;
				double cEX = endX - (width - collisionBox.endX);
				double cEY = endY - (height - collisionBox.endY);
				
				EDimension col = new EDimension(cSX, cSY, cEX, cEY);
				//System.out.println(col);
				//col = col.expand(1);
				
				int lVal = (left) ?		-1 :  0;
				int uVal = (up) ? 		-1 :  0;
				int rVal = (right) ?	 0 : -1;
				int dVal = (down) ?		 0 : -1;
				
				int movingToSX = (int) ((col.startX + lVal) / w);
				int movingToSY = (int) ((col.startY + uVal) / h);
				int movingToEX = (int) ((col.endX + rVal) / w);
				int movingToEY = (int) ((col.endY + dVal) / h);
				
				//col.endX += 1;
				//col.endY += 1;
				//col = col.contract(1);

				movingToSX = NumberUtil.clamp(movingToSX, 0, world.getWidth() - 1);
				movingToSY = NumberUtil.clamp(movingToSY, 0, world.getHeight() - 1);
				movingToEX = NumberUtil.clamp(movingToEX, 0, world.getWidth() - 1);
				movingToEY = NumberUtil.clamp(movingToEY, 0, world.getHeight() - 1);
				
				WorldTile tl = world.getTileAt(movingToSX, movingToSY);
				WorldTile tr = world.getTileAt(movingToEX, movingToSY);
				WorldTile bl = world.getTileAt(movingToSX, movingToEY);
				WorldTile br = world.getTileAt(movingToEX, movingToEY);
				
				boolean tlBlock = (tl == null) || tl.blocksMovement();
				boolean trBlock = (tr == null) || tr.blocksMovement();
				boolean blBlock = (bl == null) || bl.blocksMovement();
				boolean brBlock = (br == null) || br.blocksMovement();
				
				double sX = movingToSX * w;
				double sY = movingToSY * h;
				double eX = movingToEX * w;
				double eY = movingToEY * h;
				
				EDimension tlDim = new EDimension(sX, sY, sX + w, sY + h);
				EDimension blDim = new EDimension(sX, eY, sX + w, eY + h);
				EDimension trDim = new EDimension(eX, sY, eX + w, sY + h);
				EDimension brDim = new EDimension(eX, eY, eX + w, eY + h);
				
				//System.out.println("[" + movingToSX + ", " + movingToSY + "] [" + movingToEX + ", " + movingToSY + "]");
				//System.out.println("[" + movingToSX + ", " + movingToEY + "] [" + movingToEX + ", " + movingToEY + "]");
				
				//System.out.println("tl: " + tlDim);
				//System.out.println("tr: " + blDim);
				//System.out.println("bl: " + blDim);
				//System.out.println("br: " + brDim);
				//System.out.println("cl: " + col);
				
				boolean clearUp = true;
				boolean clearLeft = true;
				boolean clearDown = true;
				boolean clearRight = true;
				
				//System.out.println("[" + tlBlock + "] [" + trBlock + "]");
				//System.out.println("[" + blBlock + "] [" + brBlock + "]");
				
				//clearUp
				if (tlBlock) {
					if (trBlock) {
						//int aVal = (int) ((col.startY + y) - tlDim.endY);
						//int bVal = (int) ((col.startY + y) - trDim.endY);
						//System.out.println("u A: " + aVal + " " + bVal);
						
						clearUp = (col.startY > tlDim.endY || col.startX >= tlDim.endX) && (col.startY > trDim.endY || col.endX <= trDim.startX);
					}
					else {
						//System.out.println("u B: " + col.startY + " > " + tlDim.endY);
						clearUp = (col.startY > tlDim.endY || col.startX >= tlDim.endX);
					}
				}
				else if (trBlock) {
					//System.out.println("u C: " + col.startY + " > " + trDim.endY + " || " + col.endX + " < " + trDim.startX);
					clearUp = (col.startY > trDim.endY || col.endX <= trDim.startX);
				}
				
				//clearLeft
				if (tlBlock) {
					if (blBlock) {
						//System.out.println("l A: " + col.startX + " > " + tlDim.endX + " && " + col.startX + " > " + blDim.endX);
						clearLeft = (col.startX > tlDim.endX && col.startX > blDim.endX);
					}
					else {
						//System.out.println("l B: " + col.startX + " > " + tlDim.endX);
						clearLeft = (col.startX > tlDim.endX);
					}
				}
				else if (blBlock) {
					//System.out.println("l C: " + col.startX + " > " + blDim.endX);
					clearLeft = (col.startX > blDim.endX);
				}
				
				//clearDown
				if (blBlock) {
					if (brBlock) {
						//System.out.println("d A: " + col.endY + " < " + blDim.startY + " && " + col.endY + " < " + brDim.startY);
						clearDown = (col.endY < blDim.startY && col.endY < brDim.startY);
					}
					else {
						//System.out.println("d B: " + col.endY + " < " + blDim.startY);
						clearDown = (col.endY < blDim.startY);
					}
				}
				else if (brBlock) {
					//System.out.println("d C: " + col.endY + " < " + brDim.startY);
					clearDown = (col.endY < brDim.startY);
				}
				
				//clerRight
				if (trBlock) {
					if (brBlock) {
						//System.out.println("r A: " + col.endX + " < " + trDim.startX + " && " + col.endX + " < " + brDim.startX);
						clearRight = (col.endX < trDim.startX || col.startY >= trDim.endY) && (col.endX < brDim.startX || col.endY <= brDim.startY);
					}
					else {
						//System.out.println("r B: " + col.endX + " < " + trDim.startX + " || " + col.startY + " >= " + trDim.endY);
						clearRight = (col.endX < trDim.startX || col.startY >= trDim.endY);
					}
				}
				else if (brBlock) {
					//System.out.println("r C: " + col.endX + " < " + brDim.startX + " || " + col.endY + " <= " + brDim.startY);
					clearRight = (col.endX < brDim.startX || col.endY <= brDim.startY);
				}
				
				//System.out.println("   [" + clearUp + "]");
				//System.out.println("[" + clearLeft + "] [" + clearRight + "]");
				//System.out.println("   [" + clearDown + "]");
				
				if (left && !(clearLeft)) 		stopMove = true;
				if (right && !(clearRight)) 	stopMove = true;
				if (up && !(clearUp)) 			stopMove = true;
				if (down && !(clearDown)) 		stopMove = true;
			}
			
			if (!stopMove || (this == QoT.thePlayer && allowNoClip)) {
				startX += x;
				endX += x;
				startY += y;
				endY += y;
				
				startX = (int) NumberUtil.clamp(startX, -collisionBox.startX, world.getPixelWidth() - collisionBox.endX);
				startY = (int) NumberUtil.clamp(startY, -collisionBox.startY, world.getPixelHeight() - collisionBox.endY);
				endX = (int) NumberUtil.clamp(endX, width - collisionBox.startX, world.getPixelWidth() + (width - collisionBox.endX));
				endY = (int) NumberUtil.clamp(endY, height - collisionBox.startY, world.getPixelHeight() + (height - collisionBox.endY));
				
				midX = startX + (width / 2);
				midY = startY + (height / 2);
				
				double valX = (startX / (world.getTileWidth() * world.getZoom()));
				double valY = (startY / (world.getTileHeight() * world.getZoom()));
				
				worldX = (int) valX;
				worldY = (int) valY;
				
				worldX = NumberUtil.clamp(worldX, 0, world.getWidth() - 1);
				worldY = NumberUtil.clamp(worldY, 0, world.getHeight() - 1);
			}
		}
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
			xpNeeded = EntityLevel.getXPNeededForNextLevel(level);
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
	
	public EDimension getCollision() { return collisionBox; }
	public boolean isPassable() { return passable; }
	public boolean isNoClipping() { return allowNoClip; }
	public Rotation getFacing() { return facing; }
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
	
	public int getBaseMeleeDamage() { return baseMeleeDamage; }
	public int getStrengthLevel() { return strengthLevel; }
	
	public int getGold() { return gold; }
	
	//---------
	// Setters
	//---------
	
	public Entity setNoClipAllowed(boolean val) { allowNoClip = val; return this; }
	public Entity setPassable(boolean val) { passable = val; return this; }
	public Entity setCollisionBox(double sX, double sY, double eX, double eY) { collisionBox = new EDimension(sX, sY, eX, eY); return this; }
	public Entity setFacing(Rotation dir) { facing = dir; return this; }
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
	public Entity setHealth(int healthIn) { health = healthIn; return this; }
	public void setHitpointsLevel(int levelIn) {
		hitpointsLevel = levelIn;
		maxHealth = EntityLevel.calculateMaxHealth(hitpointsLevel);
		health = NumberUtil.clamp(health, 0, maxHealth);
	}
	
	public Entity setMaxMana(int maxManaIn) { maxMana = maxManaIn; return this; }
	public Entity setMana(int manaIn) { mana = manaIn; return this; }
	public void setMagicLevel(int levelIn) {
		magicLevel = levelIn;
		maxMana = EntityLevel.calculateMaxMana(magicLevel);
		mana = NumberUtil.clamp(mana, 0, maxMana);
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
		baseMeleeDamage = 2 + (levelIn * 2);
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
	
	//------------------
	// Internal Methods
	//------------------
	
	/** Returns true if this entity had enough mana to use the given spell. */
	protected boolean manaCheck(int spellCost) {
		if (mana >= spellCost) {
			drainMana(spellCost);
			return true;
		}
		return false;
	}
	
}
