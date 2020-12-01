package entities;

import entities.player.Player;
import gameSystems.mapSystem.GameWorld;
import gameSystems.questSystem.RouteTracker;
import gameSystems.textureSystem.GameTexture;
import openGL_Util.GLObject;
import util.mathUtil.Direction;
import util.mathUtil.NumUtil;
import util.storageUtil.EDimension;

public abstract class Entity extends GLObject {
	
	public GameWorld world;
	public GameTexture sprite;
	public double startX, startY, endX, endY;
	public double midX, midY;
	public double width, height;
	public EDimension collisionBox = new EDimension();
	public int worldX, worldY;
	protected String name;
	protected int level;
	protected double maxHealth;
	protected double health;
	protected double maxMana;
	protected double mana;
	protected double damage;
	protected double gold;
	protected boolean isDead;
	
	public Entity(String nameIn, int levelIn, double maxHealthIn, double healthIn, double maxManaIn, double manaIn, double damageIn, double goldIn) {
		name = nameIn;
		level = levelIn;
		maxHealth = maxHealthIn;
		health = healthIn;
		maxMana = maxManaIn;
		mana = manaIn;
		damage = damageIn;
		gold = goldIn;
		isDead = false;
	}
	
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
	
	public abstract void drawEntity();
	
	protected void drawTexture() {
		if (sprite != null) {
			drawTexture(startX, startY, width, height, sprite);
		}
	}
	
	public void onLivingUpdate() {}
	
	//---------
	// Methods
	//---------
	
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
	
	public void move(double x, double y) {
		if (world != null) {
			startX += x;
			endX += x;
			startY += y;
			endY += y;
			
			startX = NumUtil.clamp(startX,  0, world.getPixelWidth() - width / 2);
			startY = NumUtil.clamp(startY, 0, world.getPixelHeight() - height / 2);
			endX = NumUtil.clamp(endX, width, world.getPixelWidth());
			endY = NumUtil.clamp(endY, height, world.getPixelHeight());
			
			midX = startX + (width) / 2;
			midY = startY + (height) / 2;
			
			double valX = (startX / (world.getTileWidth() * world.getZoom()));
			double valY = (startY / (world.getTileHeight() * world.getZoom()));
			
			worldX = (int) valX;
			worldY = (int) valY;
			
			//System.out.println(valX + " " + valY);
			
			worldX = NumUtil.clamp(worldX, 0, world.getWidth() - 1);
			worldY = NumUtil.clamp(worldY, 0, world.getHeight() - 1);
		}
	}
	
	public void hurt(double amount) {
		health = NumUtil.clamp(health - amount, 0, health);
		//addObject(new DamageSplash(midX, startY, amount));
		if (health <= 0) { kill(); }
	}
	
	public void heal(double amount) { health = NumUtil.clamp(health + amount, 0, maxHealth); }
	public void drainMana(double amount) { mana = NumUtil.clamp(mana - amount, 0, Double.MAX_VALUE); }
	
	public void kill() {
		health = 0;
		isDead = true;
	}
	
	public void kill(Player player) {
		health = 0;
		isDead = true;
		player.getBackgroundStats().setEnemiesKilled(1);
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return name; }
	public int getLevel() { return level; }
	public double getHealth() { return health; }
	public double getMana() { return mana; }
	public double getGold() { return gold; }
	public double getDamage() { return damage; }
	public boolean isDead() { return isDead; }
	public GameTexture getTexture() { return sprite; }
	public EDimension getCollision() { return collisionBox; }
	
	//---------
	// Setters
	//---------
	
	public Entity setCollisionBox(double sX, double sY, double eX, double eY) { collisionBox = new EDimension(sX, sY, eX, eY); return this; }
	public Entity setSprite(GameTexture in) { sprite = in; return this; }
	public Entity setWorldPos(int x, int y) {
		if (world != null) {
			worldX = x;
			worldY = y;
			
			startX = worldX * world.getTileWidth();
			endX = startX + width;
			startY = worldY * world.getTileHeight();
			endY = startY + height;
			
			midX = startX + (width) / 2;
			midY = startY + (height) / 2;
		}
		return this;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	/** Returns true if this entity had enough mana to use the given spell. */
	protected boolean manaCheck(double spellCost) {
		if (mana >= spellCost) {
			drainMana(spellCost);
			return true;
		}
		return false;
	}

	public abstract RouteTracker getBackgroundStats();
	
}
