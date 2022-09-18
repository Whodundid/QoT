package game.entities.player;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameObjects.entity.Player;
import envision.gameEngine.gameObjects.entity.PlayerStats;
import envision.gameEngine.world.gameWorld.IGameWorld;
import envision.gameEngine.world.worldUtil.WorldCamera;
import envision.inputHandlers.Mouse;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.misc.Rotation;
import game.QoT;
import game.assets.textures.entity.EntityTextures;
import game.assets.textures.item.ItemTextures;

public class QoT_Player extends Player {
	
	private PlayerStats stats;
	
	public QoT_Player() { this("Player", 0, 0); }
	public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
	public QoT_Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
		//init(posX, posY, 48, 48);
		init(posX, posY, 40, 40);
		
		setMaxHealth(50);
		setHealth(50);
		setBaseMeleeDamage(2);
		
		setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
		//setCollisionBox(midX + 18, endY - 10, midX + 28, endY);
		tex = EntityTextures.player;
		timeUntilNextAttack = 175l;
	}
	
	//----------------
	// Player Getters
	//----------------
	
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		super.draw(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
	}
	
	public void drawEntity(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		boolean flip = facing == Rotation.RIGHT || facing == Rotation.DOWN;
		
		drawTexture(tex, x, y, w, h, flip, brightness);
		//healthBar.setDimensions(x, y - 7, w, 7);
		//healthBar.drawObject(Mouse.getMx(), Mouse.getMy());
		
		if ((recentlyAttacked || healthChanged) && !invincible && (health < maxHealth)) {
//			EDimension draw = new EDimension(x + 20, y - 7, x + w - 20, y);
//			
//			var cur = health;
//			var percent = (double) cur / (double) maxHealth;
//			var pw = (draw.width * percent);
//			
//			drawRect(draw.add(1), EColors.black);
//			var end = (this == QoT.thePlayer) ? 4 : 1;
//			drawRect(draw.startX, draw.startY, draw.startX + pw, draw.endY - end, EColors.mc_darkred);
			
			//healthBar.keepDrawing();
		}
		
		drawStringC(headText, x + w/2, y - h/4);
		//drawStringC(recentlyAttacked + ":" + recentlyHit + ":" + mouseOver, x + w/2, y - h/2, 0.5, 0.5);
		
		//draw sword *crapily*
		if (attacking) {
			if (System.currentTimeMillis() - attackStart <= 100) {
				double drawX = x + w / 2 - ItemTextures.iron_sword.getWidth();
				double drawY = y + h / 2 - ItemTextures.iron_sword.getHeight() / 2;
				drawTexture(ItemTextures.iron_sword, drawX, drawY, 64, 64);
			}
			
			if ((System.currentTimeMillis() - attackStart) >= timeUntilNextAttack) {
				attacking = false;
			}
		}
		
		//draw cooldown bar
		if (System.currentTimeMillis() - attackStart < timeUntilNextAttack) {
			EDimension draw = new EDimension(x, y - 3, x + w, y - 1);
			
			var cur = System.currentTimeMillis() - attackStart;
			var percent = (double) cur / (double) timeUntilNextAttack / 2;
			var pw = (draw.width * percent);
			
			//drawRect(draw.startX + pw, draw.startY, draw.endX - pw, draw.endY, EColors.mc_gold.opacity(180));
		}
	}
	
	public void onMousePress(int mXIn, int mYIn, int button) {
		if (!attacking && button == 0) {
			attacking = true;
			recentlyAttacked = true;
			attackDrawStart = System.currentTimeMillis();
			attackStart = System.currentTimeMillis();
			
			EArrayList<Entity> inRange = new EArrayList();
			for (var e : QoT.theWorld.getEntitiesInWorld()) {
				if (e == this) continue;
				if (e.isInvincible()) continue;
				if (QoT.theWorld.getDistance(e, this) < 50) inRange.add((Entity) e);
			}
			
			for (var e : inRange) {
				var damage = getBaseMeleeDamage();
				e.drainHealth(damage);
				//addObject(new DamageSplash(e.startX + e.midX, e.startY + e.midY, damage));
				if (e.isDead()) {
					getStats().addKilled(1);
					QoT.theWorld.getEntitiesInWorld().remove(e);
					addXP(e.getExperienceRewardedOnKill());
				}
			}
		}
	}
	
	@Override
	public void onLivingUpdate() {

	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
