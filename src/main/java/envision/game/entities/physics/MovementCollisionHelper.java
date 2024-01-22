package envision.game.entities.physics;

import envision.game.effects.OutOfStaminaEffect;
import envision.game.entities.Entity;
import envision.game.entities.Projectile;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.math.vectors.Vec2f;
import eutil.misc.Rotation;

public class MovementCollisionHelper {
	
	private Entity theEntity;
	private boolean isCollidedInX = false;
	private boolean isCollidedInY = false;
	
	//========
	// Fields
	//========
	
	public MovementCollisionHelper(Entity entity) {
		theEntity = entity;
	}
	
	//=========
	// Methods
	//=========
	
    public void tryMovePixel(double moveX, double moveY) {
        var e = theEntity;
        var world = e.world;
        
        isCollidedInX = false;
        isCollidedInY = false;
        
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
        
        double normX = moveX;
        double normY = moveY;
        
        if (normX == Float.NaN || normY == Float.NaN) {
            return;
        }
        
        if (e.startX == Float.NaN || e.startY == Float.NaN || normX == Float.NaN) {
            System.out.println("NaN: " + e.startX + " : " + e.startY + " : " + normX);
        }
        
        e.startX = e.startX + normX;
        e.startY = e.startY + normY;
        
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
                    
                    var tDims = new Dimension_d(tsx, tsy, tex, tey);
                    boolean col = MovementCollisionHelper.overlapOnAxis(dims, tDims, xAxis);
                    
                    if (col) {
                        blockX = true;
                        isCollidedInX = true;
                        blockXCoords = (left) ? tex : tsx;
                        break;
                    }
                }
            }
            
            if (isMovingY) {
                for (var t : getCollidingTilesForYAxis(up)) {
                    // System.out.println("Y: " + t + " [" + t.worldX + ", " + t.worldY + "]");
                    
                    double tsx = t.worldX * world.getTileWidth();
                    double tsy = t.worldY * world.getTileWidth();
                    double tex = tsx + world.getTileWidth();
                    double tey = tsy + world.getTileHeight();
                    
                    var tDims = new Dimension_d(tsx, tsy, tex, tey);
                    boolean col = MovementCollisionHelper.overlapOnAxis(dims, tDims, yAxis);
                    
                    if (col) {
                        blockY = true;
                        isCollidedInY = true;
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
        
        e.endX = e.startX + e.width;
        e.endY = e.startY + e.height;
        
        e.worldX = (int) valX;
        e.worldY = (int) valY;
    }
	
	public void tryMove(double moveX, double moveY, float dt) {
		var e = theEntity;
		var world = e.world;
		
		isCollidedInX = false;
        isCollidedInY = false;
		
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
		
		if (len == 0 || len == Float.NaN || normX == Float.NaN || normY == Float.NaN) {
		    return;
		}
		
		var speed = e.getSpeed();
		
		if (e.activeEffectsTracker.hasEffect(OutOfStaminaEffect.EFFECT_NAME)) {
		    speed = (32.0 * 0.5) / 1000.0;
		}
		else if (e.activeEffectsTracker.hasEffectType("SPEED_MODIFIER")) {
		    double effectTotal = e.activeEffectsTracker.getEffectTypeTotal("SPEED_MODIFIER");
		    speed += effectTotal * 0.0001;
		}
		
		normX *= speed;
		normY *= speed;
		
		if (e.startX == Float.NaN || e.startY == Float.NaN || normX == Float.NaN || dt == Float.NaN) {
			System.out.println(e.startX);
			System.out.println(e.startY);
			System.out.println(normX);
			System.out.println(dt);
		}
		
		e.startX = e.startX + normX * dt;
		e.startY = e.startY + normY * dt;
		
		if (!e.isNoClipping()) {
			final var xAxis = new Vec2f(1, 0);
			final var yAxis = new Vec2f(0, 1);
			final var dims = e.getCollisionDims();
			
			boolean blockY = false;
			double blockYCoords = 0.0;
			
			boolean blockX = false;
			double blockXCoords = 0.0;
			
			boolean isProjectile = e instanceof Projectile;
			
			if (isMovingX) {
				for (var t : getCollidingTilesForXAxis(left)) {
				    if (isProjectile && t.wallHeight <= 0.2) continue;
				    
					double tsx = t.worldX * world.getTileWidth();
					double tsy = t.worldY * world.getTileWidth();
					double tex = tsx + world.getTileWidth();
					double tey = tsy + world.getTileHeight();
					
					var tDims = new Dimension_d(tsx, tsy, tex, tey);
					boolean col = MovementCollisionHelper.overlapOnAxis(dims, tDims, xAxis);
					
					if (col) {
						blockX = true;
						isCollidedInX = true;
						blockXCoords = (left) ? tex : tsx;
						break;
					}
				}
			}
			
			if (isMovingY) {
				for (var t : getCollidingTilesForYAxis(up)) {
				    if (isProjectile && t.wallHeight <= 0.2) continue;
				    
					double tsx = t.worldX * world.getTileWidth();
					double tsy = t.worldY * world.getTileWidth();
					double tex = tsx + world.getTileWidth();
					double tey = tsy + world.getTileHeight();
					
					var tDims = new Dimension_d(tsx, tsy, tex, tey);
					boolean col = MovementCollisionHelper.overlapOnAxis(dims, tDims, yAxis);
					
					if (col) {
						blockY = true;
						isCollidedInY = true;
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
			
			final double minX = -e.collisionBox.startX;
			final double minY = -e.collisionBox.startY;
			final double maxX = world.getPixelWidth() - e.collisionBox.endX;
			final double maxY = world.getPixelHeight() - e.collisionBox.endY;
			
			if (e.startX <= minX) isCollidedInX = true;
			if (e.startY <= minY) isCollidedInY = true;
			if (e.startX >= maxX) isCollidedInX = true;
			if (e.startY >= maxY) isCollidedInY = true;
			
			e.startX = ENumUtil.clamp(e.startX, minX, maxX);
			e.startY = ENumUtil.clamp(e.startY, minY, maxY);
		}
		
		e.midX = e.startX + (e.width / 2.0);
		e.midY = e.startY + (e.height / 2.0);
		
		double valX = e.startX / world.getTileWidth();
		double valY = e.startY / world.getTileHeight();
		
        e.endX = e.startX + e.width;
        e.endY = e.startY + e.height;
		
		e.worldX = (int) valX;
		e.worldY = (int) valY;
	}
	
	public Vec2f getInterval(Vec2f axis) {
		return getInterval(theEntity.getCollisionDims(), axis);
	}
	
	public static Vec2f getInterval(Dimension_d rect, Vec2f axis) {
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
	
	public static boolean overlapOnAxis(Dimension_d b1, Dimension_d b2, Vec2f axis) {
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
		
		int wcy = (int) (cb.midY / theEntity.world.getTileHeight()); // world collision y
		
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
			
			// if the entity can move across low walls, allow the movement
            if (theEntity.canMoveAcrossLowMovementBlockingWalls) {
                if (t.wallHeight < 0.0) continue;
            }
			
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
			
			// if the entity can move across low walls, allow the movement
			if (theEntity.canMoveAcrossLowMovementBlockingWalls) {
			    if (t.wallHeight < 0.0) continue;
			}
			
			tiles.add(t);
		}
		
		return tiles;
	}
	
	//=========
    // Getters
    //=========
	
	public boolean isCollidedInX() { return isCollidedInX; }
	public boolean isCollidedInY() { return isCollidedInY; }
	public boolean isCollided() { return isCollidedInX || isCollidedInY; }
	
}
