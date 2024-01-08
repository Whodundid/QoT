package qot.entities.projectiles;

import envision.Envision;
import envision.engine.resourceLoaders.Sprite;
import envision.game.GameObject;
import envision.game.entities.BasicRenderedEntity;
import eutil.EUtil;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.world_tiles.GlobalTileList;
import qot.world_tiles.TileIDs;
import qot.world_tiles.categories.NatureTiles;

public class Fireball extends BasicRenderedEntity {
	
	private boolean spawned = false;
	private long timeToLive = 3000;
	private long timeSpawned;
	private GameObject spawningObject;
	
	public Fireball() { this("Fireball", null, 0, 0); }
	public Fireball(GameObject spawningObject) { this("Fireball", spawningObject, 0, 0); }
	public Fireball(String nameIn, GameObject spawningObject, int x, int y) {
		super(nameIn);
		
		setBaseMeleeDamage(3 + ERandomUtil.getRoll(0, 3));
		setInvincible(true);
		setNoClipAllowed(true);
		
		init(x, y, 32, 32);
		setSprite(new Sprite(EntityTextures.fireBall_projectile));
		setCollisionBox(startX - 8, startY - 8, endX + 8, endY + 8);
		
		this.spawningObject = spawningObject;
		setSpeed(270);
		canBeCarried = false;
        canBeMoved = false;
	}
	
	/**
	 * When fireball spawns, it follows the mouse cursor, grabs the coordinates, and then casts the fireball in an animation
	 * towards the cursor. If it touches Enemy Entities, it will do damage to them accordingly. If they are below the minimum health
	 * they will be destroyed 
	 */
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
				// ignore the target that spawned this projectile
				if (e == spawningObject) continue;
				e.drainHealth(baseMeleeDamage);
				if (e.isDead()) world.removeObjectFromWorld(e);
			}
			killProjectile();
			
			if (worldX >= 0 && worldX < world.getWidth() && worldY >= 0 && worldY < world.getHeight()) {
				damageGround(worldX, worldY, 2);
				damageGround(worldX, worldY - 1);
				damageGround(worldX - 1, worldY);
				damageGround(worldX, worldY + 1);
				damageGround(worldX + 1, worldY);
			}
			
		}
	}
	
	private void damageGround(int x, int y) { damageGround(x, y, 1); }
	private void damageGround(int x, int y, int amount) {
		if (!(x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight())) return;
		
		var t = world.getTileAt(x, y);
		if (t == null || t.isWall()) return;
		
		var toSet = NatureTiles.dryPlainsGrass;
		final int id = t.getID();
		
		final int dam1 = TileIDs.DRY_PLAINS_GRASS.tileID;
		final int dam2 = TileIDs.DRY_CRACKED_DIRT.tileID;
		final int dam3 = TileIDs.CRACKED_DIRT.tileID;
		final int dam4 = TileIDs.ROCKY_DIRT.tileID;
		final int dam5 = TileIDs.DARK_DUNG_FLOOR.tileID;
		
		int curID = EUtil.findMatch(id, dam1, dam2, dam3, dam4, dam5);
		
		// transform into workable range
		curID = switch (TileIDs.getIdFrom(curID)) {
		case DRY_PLAINS_GRASS -> 0;
		case DRY_CRACKED_DIRT -> 1;
		case CRACKED_DIRT -> 2;
		case ROCKY_DIRT -> 3;
		case DARK_DUNG_FLOOR -> 4;
		default -> -1;
		};
		
		curID += amount;
		
		// transform back into normal tile ids
		int idToSet = switch (curID) {
		case 0 -> TileIDs.DRY_PLAINS_GRASS.tileID;
		case 1 -> TileIDs.DRY_CRACKED_DIRT.tileID;
		case 2 -> TileIDs.CRACKED_DIRT.tileID;
		case 3 -> TileIDs.ROCKY_DIRT.tileID;
		default -> TileIDs.DARK_DUNG_FLOOR.tileID;
		};
		
		toSet = GlobalTileList.getTileFromID(idToSet);
		
		if (toSet != null) world.setTileAt(toSet.copy(), x, y);
	}
	
	private void killProjectile() {
		kill();
		world.removeEntity(this);
	}
	
	@Override public int getInternalSaveID() {
		return 0;
	}
	
}
