package qot.entities.enemies.dragon;

import envision.Envision;
import envision.game.entities.BasicRenderedEntity;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.world_tiles.categories.NatureTiles;

public class Fireball extends BasicRenderedEntity {
	
	private boolean spawned = false;
	private long timeToLive = 3000;
	private long timeSpawned;
	
	public Fireball() { this("Fireball", 0, 0); }
	public Fireball(String nameIn, int x, int y) {
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
		
		var p = Envision.thePlayer;
		if (p == null) return;
		
		// always move towards the player if alive
		var dir = world.getDirectionTo(this, p);
		move(dir);
		
		var cb = getCollisionDims();
		var pcb = p.getCollisionDims();
		
		if (cb.partiallyContains(pcb)) {
			var entitiesInRange = world.getAllEntitiesWithinDistance(this, 80);
			for (var e : entitiesInRange) {
				if (e instanceof Thyrah) continue;
				e.drainHealth(baseMeleeDamage);
			}
			killProjectile();
			
			if (worldX >= 0 && worldX < world.getWidth() && worldY >= 0 && worldY < world.getHeight()) {
				var tile = world.getTileAt(worldX, worldY);
				if (tile != null && !tile.isWall()) {
					world.setTileAt(NatureTiles.crackedDirt.copy(), worldX, worldY);
				}
			}
			
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
