package assets.entities;

import assets.entities.player.Player;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.questSystem.RouteTracker;
import gameSystems.textureSystem.GameTexture;
import util.mathUtil.Direction;
import util.mathUtil.NumUtil;
import util.openGL_Util.GLObject;
import util.storageUtil.EDimension;

public abstract class Entity extends GLObject {
	
	public GameWorld world;
	public GameTexture sprite;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
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
	protected boolean passable;
	protected boolean allowNoClip = false;
	
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
			x = Math.signum(x);
			y = Math.signum(y);
			boolean stopMove = false;
			
			if (!allowNoClip) {
				boolean left = false, right = false, up = false, down = false;
				double w = world.getTileWidth();
				double h = world.getTileHeight();
				
				if (x < 0) { left = true; }
				else if (x > 0) { right = true; }
				if (y < 0) { up = true; }
				else if (y > 0) { down = true; }

				//System.out.println(collisionBox.endX);
				
				double cSX = startX + collisionBox.startX;
				double cSY = startY + collisionBox.startY;
				double cEX = endX - (width - collisionBox.endX);
				double cEY = endY - (height - collisionBox.endY);
				
				EDimension col = EDimension.of(cSX, cSY, cEX, cEY);
				//System.out.println(col);
				//col = col.expand(1);
				
				int lVal = (left) ? -1 : 0;
				int uVal = (up) ? -1 : 0;
				int rVal = (right) ? 0 : -1;
				int dVal = (down) ? 0 : -1;
				
				int movingToSX = (int) ((col.startX + lVal) / w);
				int movingToSY = (int) ((col.startY + uVal) / h);
				int movingToEX = (int) ((col.endX + rVal) / w);
				int movingToEY = (int) ((col.endY + dVal) / h);
				
				//col.endX += 1;
				//col.endY += 1;
				//col = col.contract(1);

				movingToSX = NumUtil.clamp(movingToSX, 0, world.getWidth() - 1);
				movingToSY = NumUtil.clamp(movingToSY, 0, world.getHeight() - 1);
				movingToEX = NumUtil.clamp(movingToEX, 0, world.getWidth() - 1);
				movingToEY = NumUtil.clamp(movingToEY, 0, world.getHeight() - 1);
				
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
				
				EDimension tlDim = EDimension.of(sX, sY, sX + w, sY + h);
				EDimension blDim = EDimension.of(sX, eY, sX + w, eY + h);
				EDimension trDim = EDimension.of(eX, sY, eX + w, sY + h);
				EDimension brDim = EDimension.of(eX, eY, eX + w, eY + h);
				
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
						int aVal = (int) ((col.startY + y) - tlDim.endY);
						int bVal = (int) ((col.startY + y) - trDim.endY);
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
				
				if (left && !(clearLeft)) { stopMove = true; }
				if (right && !(clearRight)) { stopMove = true; }
				if (up && !(clearUp)) { stopMove = true; }
				if (down && !(clearDown)) { stopMove = true; }
			}
			
			if (allowNoClip || !stopMove) {
				startX += x;
				endX += x;
				startY += y;
				endY += y;
				
				startX = (int) NumUtil.clamp(startX, -collisionBox.startX, world.getPixelWidth() - collisionBox.endX);
				startY = (int) NumUtil.clamp(startY, -collisionBox.startY, world.getPixelHeight() - collisionBox.endY);
				endX = (int) NumUtil.clamp(endX, width - collisionBox.startX, world.getPixelWidth() + (width - collisionBox.endX));
				endY = (int) NumUtil.clamp(endY, height - collisionBox.startY, world.getPixelHeight() + (height - collisionBox.endY));
				
				midX = startX + (width / 2);
				midY = startY + (height / 2);
				
				double valX = (startX / (world.getTileWidth() * world.getZoom()));
				double valY = (startY / (world.getTileHeight() * world.getZoom()));
				
				worldX = (int) valX;
				worldY = (int) valY;
				
				worldX = NumUtil.clamp(worldX, 0, world.getWidth() - 1);
				worldY = NumUtil.clamp(worldY, 0, world.getHeight() - 1);
			}
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
	public boolean isPassable() { return passable; }
	public boolean isNoClipping() { return allowNoClip; }
	
	//---------
	// Setters
	//---------
	
	public Entity setNoClipAllowed(boolean val) { allowNoClip = val; return this; }
	public Entity setPassable(boolean val) { passable = val; return this; }
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
			
			//collisionBox.setPosition(startX + 20, startY + 20);
			
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
