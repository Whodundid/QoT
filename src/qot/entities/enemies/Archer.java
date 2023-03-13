package qot.entities.enemies;

import envision.game.objects.entities.Entity;
import eutil.math.dimensions.EDimension;
import eutil.math.vectors.Vec2d;
import eutil.math.vectors.Vec2f;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.EntityList;

public class Archer extends Entity {

	/** I am.. SPEED */
	public double speed;
	public Vec2d velocity;
	public long oldTime, curTime;
	public boolean start;
	float dt;
	
	boolean left = true;
	long timeSinceLeft;
	
	public Archer() { this("Archer", 0, 0); }
	public Archer(String nameIn, int x, int y) {
		super(nameIn);
		
		setBaseMeleeDamage(3);
		setMaxHealth(25);
		setHealth(25);
		
		init(x, y, 32, 64);
		tex = EntityTextures.player;
		
		setCollisionBox(startX + 8, endY - 15, endX - 8, endY);
		setExperienceRewardedOnKill(75);
		
		speed = 50;
	}
	
	public void move(double moveX, double moveY, float timeSinceLastUdate) {
		double len = Math.sqrt(moveX * moveX + moveY * moveY);
		double normX = moveX / len;
		double normY = moveY / len;
		
		normX *= speed;
		normY *= speed;
		
		startX = startX + normX * timeSinceLastUdate;
		startY = startY + normY * timeSinceLastUdate;
		
		this.setHeadText("" + (int) startX + " " + left);
		
		double valX = startX / world.getTileWidth();
		double valY = startY / world.getTileHeight();
		
		worldX = (int) valX;
		worldY = (int) valY;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		if (!start) {
			oldTime = System.currentTimeMillis();
			curTime = oldTime;
			timeSinceLeft = (long) curTime;
			
			start = true;
		}

		oldTime = curTime;
		curTime = System.currentTimeMillis();
		
		dt = curTime - oldTime;
		dt /= 1000.0f;
		
		if (dt > 0.15f) dt = 0.15f;
		
		// movement logic
		if (left) {
			if ((System.currentTimeMillis() - timeSinceLeft) <= 2000L) {
				move(1.0f, 1f, dt);
			}
			else {
				left = false;
				timeSinceLeft = System.currentTimeMillis();
			}
		}
		else {
			if ((System.currentTimeMillis() - timeSinceLeft) <= 2000L) {
				move(-1.0f, -1f, dt);
			}
			else {
				left = true;
				timeSinceLeft = System.currentTimeMillis();
			}
		}
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.Archer.ID;
	}
	
	
	
}
