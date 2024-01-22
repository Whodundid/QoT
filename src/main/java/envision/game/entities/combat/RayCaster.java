package envision.game.entities.combat;

import org.joml.RoundingMode;
import org.joml.Vector3f;
import org.joml.Vector3i;

import envision.game.GameObject;
import envision.game.world.IGameWorld;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;

public class RayCaster {
    
    /**
     * 
     * @param world
     * @param a
     * @param b
     * @param maxDist
     * @return
     */
    public static RayCastResult checkRaycastHit(IGameWorld world,
                                                 GameObject a,
                                                 GameObject b,
                                                 double maxDist)
    {
        return checkRaycastHit(world, a, b, maxDist, true);
    }
    
    /**
     * 
     * @param world
     * @param a
     * @param b
     * @param maxDist
     * @return
     */
    public static RayCastResult checkRaycastHit(IGameWorld world,
                                                 GameObject a,
                                                 GameObject b,
                                                 double maxDist,
                                                 boolean ignoreNegativeHeightWalls)
    {
        var ad = a.getCollisionDims();
        var bd = b.getCollisionDims();
        Vector3f av = new Vector3f((float) ad.midX, (float) ad.midY, 0.0f);
        Vector3f bv = new Vector3f((float) bd.midX, (float) b.midY, 0.0f);
        return checkRaycastHit(world, av, bv, maxDist, ignoreNegativeHeightWalls);
    }
    
    /**
     * Ignores tiles with negative height by default. AKA: water.
     * 
     * @param world
     * @param start
     * @param end
     * @param maxDist
     * @return
     */
    public static RayCastResult checkRaycastHit(IGameWorld world,
                                                Vector3f start,
                                                Vector3f end,
                                                double maxDist)
    {
        return checkRaycastHit(world, start, end, maxDist, true);
    }
    
    /**
     * 
     * @param world
     * @param start
     * @param end
     * @param maxDist
     * @param ignoreNegativeHeightWalls
     * @return
     */
    public static RayCastResult checkRaycastHit(IGameWorld world,
                                                Vector3f start,
                                                Vector3f end,
                                                double maxDist,
                                                boolean ignoreNegativeHeightWalls)
    {
        // make sure the raycast is actually possible upfront
        if (world == null || start == null || end == null) return RayCastResult.miss();
        if (start.x == Float.NaN || start.y == Float.NaN || start.z == Float.NaN) return RayCastResult.miss();
        if (end.x == Double.NaN || end.y == Double.NaN || end.z == Double.NaN) return RayCastResult.miss();
        
        final float tw = world.getTileWidth();
        final float th = world.getTileHeight();
        
        Vector3f rayStart = new Vector3f(start.x / tw, start.y / th, 0.0f);
        Vector3f targetPos = new Vector3f(end.x / tw, end.y / th, 0.0f);
        
        Vector3f rayDir = new Vector3f(targetPos).sub(rayStart).normalize();
        
        Vector3f rayUnitStepSize = new Vector3f((float) Math.sqrt(1 + (rayDir.y / rayDir.x) * (rayDir.y / rayDir.x)),
                                                (float) Math.sqrt(1 + (rayDir.x / rayDir.y) * (rayDir.x / rayDir.y)),
                                                0.0f);
        
        Vector3i mapCheck = new Vector3i(rayStart, RoundingMode.FLOOR);
        Vector3f rayLength1D = new Vector3f();
        Vector3f step = new Vector3f();
        
        // determine initial x starting values
        if (rayDir.x < 0) {
            step.x = -1;
            rayLength1D.x = (rayStart.x - mapCheck.x) * rayUnitStepSize.x; 
        }
        else {
            step.x = 1;
            rayLength1D.x = ((mapCheck.x + 1) - rayStart.x) * rayUnitStepSize.x;
        }
        
        // determine initial y starting values
        if (rayDir.y < 0) {
            step.y = -1;
            rayLength1D.y = (rayStart.y - mapCheck.y) * rayUnitStepSize.y; 
        }
        else {
            step.y = 1;
            rayLength1D.y = ((mapCheck.y + 1) - rayStart.y) * rayUnitStepSize.y;
        }
        
        boolean tileFound = false;
        WorldTile theTile = null;
        float fMaxDist = (float) maxDist;
        float distance = 0.0f;
        
        while (!tileFound && distance < fMaxDist) {
            if (rayLength1D.x < rayLength1D.y) {
                mapCheck.x += step.x;
                distance = rayLength1D.x;
                rayLength1D.x += rayUnitStepSize.x;
            }
            else {
                mapCheck.y += step.y;
                distance = rayLength1D.y;
                rayLength1D.y += rayUnitStepSize.y;
            }
            
            final int tileX = mapCheck.x;
            final int tileY = mapCheck.y;
            if (tileX >= 0 && tileX < world.getWidth() && tileY >= 0 && tileY < world.getHeight()) {
                WorldTile testTile = world.getTileAt(tileX, tileY);
                
                if (testTile != null && testTile.blocksMovement) {
                    boolean ignore = (ignoreNegativeHeightWalls && (testTile.wallHeight < 0 || testTile == VoidTile.instance));
                    if (!ignore) {
                        tileFound = true;
                        theTile = testTile;
                    }
                }
            }
        }
        
        Vector3f intersection = null;
        if (tileFound) {
            intersection = new Vector3f(rayStart).add(rayDir.mul(distance));
            return RayCastResult.hit(theTile, distance, intersection);
        }
        
        return RayCastResult.miss();
    }
    
    public static class RayCastResult {
        
        //========
        // Fields
        //========
        
        public final WorldTile tile;
        public final float distance;
        public final Vector3f intersection;
        public final boolean collided;
        
        //==============
        // Constructors
        //==============
        
        RayCastResult() {
            tile = null;
            distance = 0.0f;
            intersection = null;
            collided = false;
        }
        
        RayCastResult(WorldTile tileIn, float distanceIn, Vector3f intersectionIn) {
            tile = tileIn;
            distance = distanceIn;
            intersection = intersectionIn;
            collided = true;
        }
        
        //===========
        // Overrides
        //===========
        
        @Override
        public String toString() {
            String hit = (collided) ? "HIT" : "MISS";
            if (collided) {
                hit += " : " + tile + "=" +tile.worldX+","+tile.worldY + " : " + distance + " : " + intersection;
            }
            return hit;
        }
        
        //================
        // Static Methods
        //================
        
        public static RayCastResult miss() {
            return new RayCastResult();
        }
        
        public static RayCastResult hit(WorldTile tile, float distance, Vector3f intersection) {
            return new RayCastResult(tile, distance, intersection);
        }
        
    }
    
}
