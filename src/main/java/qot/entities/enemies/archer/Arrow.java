package qot.entities.enemies.archer;

import envision.Envision;
import envision.game.entities.BasicRenderedEntity;
import envision.game.entities.Entity;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;

public class Arrow extends BasicRenderedEntity {
	
	private boolean spawned = false;
	private long timeToLive = 3000;
	private long timeSpawned;
	
	public Arrow() { this("Arrow", 0, 0); }
	public Arrow(String nameIn, int x, int y) {
		super(nameIn);
		
		setBaseMeleeDamage(3 + ERandomUtil.getRoll(0, 3));
		setInvincible(true);
		setNoClipAllowed(true);
		
		init(x, y, 32, 32);
		setTexture(EntityTextures.fireBall_projectile);
		setCollisionBox(startX - 8, startY - 8, endX + 8, endY + 8);
		
		speed = 270;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		var cur = System.currentTimeMillis();
		
		if (!spawned) {
			spawned = true;
			timeSpawned = cur;
		}
		
		if (cur - timeSpawned >= timeToLive) {
			killProjectile();
			return;
		}
		
		Entity target = null;
		var p = Envision.thePlayer;
		target = p;
		if (target == null) return;
		
		// always move towards the player if alive
		var dir = world.getDirectionTo(this, target);
		move(dir);
		
		var cb = getCollisionDims();
		var pcb = p.getCollisionDims();
		
		if (cb.partiallyContains(pcb)) {
			target.drainHealth(getBaseMeleeDamage());
			killProjectile();
		}
	}
	
	private void killProjectile() {
		kill();
		world.removeEntity(this);
	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
