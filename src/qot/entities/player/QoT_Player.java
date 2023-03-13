package qot.entities.player;

import envision.Envision;
import envision.game.objects.CollisionHelper;
import envision.game.objects.entities.Entity;
import envision.game.objects.entities.Player;
import envision.game.objects.entities.PlayerStats;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.EDimension;
import eutil.math.vectors.Vec2f;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.assets.textures.item.ItemTextures;
import qot.entities.enemies.Thyrah;
import qot.items.Items;

public class QoT_Player extends Player {
	
	private PlayerStats stats;
	
	public long oldTime, curTime;
	public boolean start;
	float dt;
	
	public QoT_Player() { this("Player", 0, 0); }
	public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
	public QoT_Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
		//init(posX, posY, 48, 48);
		init(posX, posY, 40, 40);
		
		setMaxHealth(10);
		setHealth(10);
		setBaseMeleeDamage(1);
		
		baseInventorySize = 10;
		inventory.setSize(baseInventorySize);
		
		inventory.setItem(0, Items.lesserHealing);
		inventory.setItem(1, Items.lesserMana);
		
		setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
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
	
	@Override
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
//			var end = (this == Envision.thePlayer) ? 4 : 1;
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
			
			drawRect(draw.startX + pw, draw.startY, draw.endX - pw, draw.endY, EColors.mc_gold.opacity(180));
		}
	}
	
	public void onMousePress(int mXIn, int mYIn, int button) {
		if (!attacking && button == 0) {
			attacking = true;
			recentlyAttacked = true;
			attackDrawStart = System.currentTimeMillis();
			attackStart = System.currentTimeMillis();
			
			EList<Entity> inRange = new EArrayList<>();
			for (var e : Envision.theWorld.getEntitiesInWorld()) {
				if (e == this) continue;
				if (e.isInvincible()) continue;
				if (((GameWorld) Envision.theWorld).getDistance(e, this) < 50) inRange.add((Entity) e);
			}
			
			for (var e : inRange) {
				var damage = getBaseMeleeDamage();
				e.drainHealth(damage);
				//addObject(new DamageSplash(e.startX + e.midX, e.startY + e.midY, damage));
				if (e.isDead()) {
					if (e instanceof Thyrah) getInventory().addItem(Items.random());
					else if (ERandomUtil.roll(5, 0, 10)) getInventory().addItem(Items.random());
					
					getStats().addKilled(1);
					Envision.theWorld.getEntitiesInWorld().remove(e);
					addXP(e.getExperienceRewardedOnKill());
				}
			}
		}
	}
	
//	@Override
//	public void move(double x, double y) {
//		super.move(x, y);
//		//move(x, y, dt);
//	}
	
	public void move(double moveX, double moveY, float timeSinceLastUpdate) {		
		boolean isMovingX = moveX != 0.0d;
		boolean isMovingY = moveY != 0.0d;
		
		boolean left = false, right = false, up = false, down = false;
		if (moveX < 0) 			left = true;
		else if (moveX > 0) 	right = true;
		if (moveY < 0) 			up = true;
		else if (moveY > 0) 	down = true;
		
		if (left) 		facing = Rotation.LEFT;
		else if (right) facing = Rotation.RIGHT;
		else if (up)	facing = Rotation.UP;
		else if (down) 	facing = Rotation.DOWN;
		
		double len = Math.sqrt(moveX * moveX + moveY * moveY);
		double normX = moveX / len;
		double normY = moveY / len;
		
		normX *= speed;
		normY *= speed;
		
		startX = startX + normX * timeSinceLastUpdate;
		startY = startY + normY * timeSinceLastUpdate;
		
		final var xAxis = new Vec2f(1, 0);
		final var yAxis = new Vec2f(0, 1);
		final var dims = this.getCollisionDims();
		
		boolean blockY = false;
		double blockYCoords = 0.0;
		
		boolean blockX = false;
		double blockXCoords = 0.0;
		
		if (isMovingX) {
			for (var t : collisionHelper.getCollidingTilesForXAxis(left)) {
				double tsx = t.worldX * world.getTileWidth();
				double tsy = t.worldY * world.getTileWidth();
				double tex = tsx + world.getTileWidth();
				double tey = tsy + world.getTileHeight();
				
				var tDims = new EDimension(tsx, tsy, tex, tey);
				boolean col = CollisionHelper.overlapOnAxis(dims, tDims, xAxis);
				
				if (col) {
					blockX = true;
					blockXCoords = (left) ? tex : tsx;
					break;
				}
			}
		}
		
		if (isMovingY) {
			for (var t : collisionHelper.getCollidingTilesForYAxis(up)) {
				double tsx = t.worldX * world.getTileWidth();
				double tsy = t.worldY * world.getTileWidth();
				double tex = tsx + world.getTileWidth();
				double tey = tsy + world.getTileHeight();
				
				var tDims = new EDimension(tsx, tsy, tex, tey);
				boolean col = CollisionHelper.overlapOnAxis(dims, tDims, yAxis);
				
				if (col) {
					blockY = true;
					blockYCoords = (up) ? tey : tsy;
					break;
				}
			}
		}
		
		if (blockX) {
			if (left) startX = blockXCoords - collisionBox.startX;
			else 	  startX = blockXCoords - collisionBox.endX;
		}
		else if (blockY) {
			if (up) startY = blockYCoords - collisionBox.startY;
			else 	startY = blockYCoords - collisionBox.endY;
		}
		
		if (!allowNoClip) {
			startX = ENumUtil.clamp(startX, -collisionBox.startX, world.getPixelWidth() - collisionBox.endX);
			startY = ENumUtil.clamp(startY, -collisionBox.startY, world.getPixelHeight() - collisionBox.endY);
		}
		
		midX = startX + (width / 2);
		midY = startY + (height / 2);
		
		double valX = startX / world.getTileWidth();
		double valY = startY / world.getTileHeight();
		
		worldX = (int) valX;
		worldY = (int) valY;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
//		System.out.println(dt);
		
//		if (!start) {
//			oldTime = System.currentTimeMillis();
//			curTime = oldTime;
//			
//			start = true;
//		}
//
//		oldTime = curTime;
//		curTime = System.currentTimeMillis();
//		
//		dt = curTime - oldTime;
//		dt /= 1000.0f;
//		
//		if (dt > 0.015f) dt = 0.015f;
		
		//setHeadText("[", (int) (startX + collisionBox.startX), ", ", (int) (startY + collisionBox.startY), "]");
	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
