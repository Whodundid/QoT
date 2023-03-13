package envision.game.objects;

import envision.game.objects.entities.Entity;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.EDimension;
import eutil.math.vectors.Vec2f;
import eutil.misc.Rotation;

public class CollisionHelper {
	
	private Entity theEntity;
	
	//========
	// Fields
	//========
	
	public CollisionHelper(Entity entity) {
		theEntity = entity;
	}
	
	//=========
	// Methods
	//=========
	
	public void tryMove(double moveX, double moveY, float timeSinceLastUpdate) {
		var e = theEntity;
		var world = e.world;
		
		boolean isMovingX = moveX != 0.0d;
		boolean isMovingY = moveY != 0.0d;
		boolean left = false, right = false, up = false, down = false;
		
		if (moveX < 0) left = true;
		else if (moveX > 0) right = true;
		if (moveY < 0) up = true;
		else if (moveY > 0) down = true;
		
		if (left) e.facing = Rotation.LEFT;
		else if (right) e.facing = Rotation.RIGHT;
		else if (up) e.facing = Rotation.UP;
		else if (down) e.facing = Rotation.DOWN;
		
		double len = Math.sqrt(moveX * moveX + moveY * moveY);
		double normX = moveX / len;
		double normY = moveY / len;
		
		if (len == 0 || len == Float.NaN || normX == Float.NaN || normY == Float.NaN) { return; }
		
		normX *= e.speed;
		normY *= e.speed;
		
		if (e.startX == Float.NaN || e.startY == Float.NaN || normX == Float.NaN || timeSinceLastUpdate == Float.NaN) {
			System.out.println(e.startX);
			System.out.println(e.startY);
			System.out.println(normX);
			System.out.println(timeSinceLastUpdate);
		}
		
		e.startX = e.startX + normX * timeSinceLastUpdate;
		e.startY = e.startY + normY * timeSinceLastUpdate;
		
		if (!e.isNoClipping()) {
			final var xAxis = new Vec2f(1, 0);
			final var yAxis = new Vec2f(0, 1);
			final var dims = e.getCollisionDims();
			
			boolean blockY = false;
			double blockYCoords = 0.0;
			
			boolean blockX = false;
			double blockXCoords = 0.0;
			
			if (isMovingX) {
				for (var t : getCollidingTilesForXAxis(left)) {
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
				for (var t : getCollidingTilesForYAxis(up)) {
//					System.out.println("Y: " + t + " [" + t.worldX + ", " + t.worldY + "]");
					
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
				if (left) e.startX = blockXCoords - e.collisionBox.startX;
				else e.startX = blockXCoords - e.collisionBox.endX;
			}
			
			if (blockY) {
				if (up) e.startY = blockYCoords - e.collisionBox.startY;
				else e.startY = blockYCoords - e.collisionBox.endY;
			}
			
			e.startX = ENumUtil.clamp(e.startX, -e.collisionBox.startX, world.getPixelWidth() - e.collisionBox.endX);
			e.startY = ENumUtil.clamp(e.startY, -e.collisionBox.startY, world.getPixelHeight() - e.collisionBox.endY);
		}
		
		e.midX = e.startX + (e.width / 2);
		e.midY = e.startY + (e.height / 2);
		
		double valX = e.startX / world.getTileWidth();
		double valY = e.startY / world.getTileHeight();
		
		e.worldX = (int) valX;
		e.worldY = (int) valY;
	}
	
	public Vec2f getInterval(Vec2f axis) {
		return getInterval(theEntity.getCollisionDims(), axis);
	}
	
	public static Vec2f getInterval(EDimension rect, Vec2f axis) {
		Vec2f result = new Vec2f();
		
		Vec2f min = new Vec2f(rect.startX, rect.endY);
		Vec2f max = new Vec2f(rect.endX, rect.startY);
		
		Vec2f[] vertices = {
			new Vec2f(min.x, min.y), new Vec2f(min.x, max.y),
			new Vec2f(max.x, min.y), new Vec2f(max.x, max.y)
		};
		
		result.x = axis.dot(vertices[0]);
		result.y = result.x;
		for (int i = 1; i < 4; i++) {
			float proj = axis.dot(vertices[i]);
			
			if (proj < result.x) result.x = proj;
			if (proj > result.y) result.y = proj;
		}
		
		return result;
	}
	
	public static boolean overlapOnAxis(EDimension b1, EDimension b2, Vec2f axis) {
		Vec2f intervalA = getInterval(b1, axis);
		Vec2f intervalB = getInterval(b2, axis);
		
		boolean collide = intervalB.x < intervalA.y && intervalA.x < intervalB.y;
		
		return collide;
	}
	
	public EList<WorldTile> getCollidingTilesForYAxis(boolean up) {
		EList<WorldTile> tiles = EList.newList();
		
		// if moving up, look at the row above the entity, otherwise, look at the row below it
		int searchYOffset = (up) ? -1 : 1;
		
		var cb = theEntity.getCollisionDims(); // collision box
		
		int wcsx = (int) ((cb.startX + 2) / theEntity.world.getTileWidth()); // world collision start x
		int wcex = (int) ((cb.endX - 2) / theEntity.world.getTileWidth()); // world collision end x
		
		int wcy = (int) (cb.midY / theEntity.world.getTileHeight());
		
		// clamp to keep within the dimensions of the actual world
		wcsx = ENumUtil.clamp(wcsx, 0, theEntity.world.getWidth() - 1);
		wcex = ENumUtil.clamp(wcex, 0, theEntity.world.getWidth() - 1);
		
		wcy += searchYOffset;
		if (wcy < 0 || wcy >= theEntity.world.getHeight()) return tiles;
		
		// iterate across X world coordinate range using the relevant Y offset
		for (int i = wcsx; i <= wcex; i++) {
			WorldTile t = theEntity.world.getTileAt(i, wcy);
			
			// if the tile is null, it should be a void tile which blocks movement by default
			if (t == null) t = new VoidTile(i, wcy);
			
			// ignore tiles that don't have collision restrictions
			if (!t.blocksMovement()) continue;
			
			tiles.add(t);
		}
		
		return tiles;
	}
	
	public EList<WorldTile> getCollidingTilesForXAxis(boolean left) {
		EList<WorldTile> tiles = EList.newList();
		
		// if moving left, look at the row to the left of this entity, otherwise, look at the row to the right of it
		int searchXOffset = (left) ? -1 : 1;
		
		var cb = theEntity.getCollisionDims(); // collision box
		
		int wcsy = (int) ((cb.startY + 2) / theEntity.world.getTileHeight()); // world collision start y
		int wcey = (int) ((cb.endY - 2) / theEntity.world.getTileHeight()); // world collision end y
		
		int wcx = (int) (cb.midX / theEntity.world.getTileWidth());
		
		// clamp to keep within the dimensions of the actual world
		wcsy = ENumUtil.clamp(wcsy, 0, theEntity.world.getHeight() - 1);
		wcey = ENumUtil.clamp(wcey, 0, theEntity.world.getHeight() - 1);
		
		wcx += searchXOffset;
		if (wcx < 0 || wcx >= theEntity.world.getWidth()) return tiles;
		
		// iterate across X world coordinate range using the relevant Y offset
		for (int i = wcsy; i <= wcey; i++) {
			WorldTile t = theEntity.world.getTileAt(wcx, i);
			
			// if the tile is null, it should be a void tile which blocks movement by default
			if (t == null) t = new VoidTile(wcx, i);
			
			// ignore tiles that don't have collision restrictions
			if (!t.blocksMovement()) continue;
			
			tiles.add(t);
		}
		
		return tiles;
	}
	
}
