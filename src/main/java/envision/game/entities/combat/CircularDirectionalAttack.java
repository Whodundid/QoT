package envision.game.entities.combat;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import envision.game.entities.Entity;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class CircularDirectionalAttack {
    
    /**
     * Draws the 'cone' for which the given entity could hit based on a
     * starting and ending world position.
     * 
     * @param entity          The entity to draw the attack cone for
     * 
     * @param startX          The starting X position of the attack (usually
     *                        entity's midX (world coordiantes))
     *                        
     * @param startY          The starting Y position of the attack (usually
     *                        entity's midY (world coordinates))
     *                        
     * @param targetX         The X location where to attack (in world
     *                        coordinates)
     * 
     * @param targetY         The y location where to attack (in world
     *                        coordiantes)
     * 
     * @param sectorDegrees   The angle of attack that the entity can reach (in
     *                        degrees)
     */
    public static void drawAttackAreaSector(Entity entity,
                                            double startX, double startY,
                                            double targetX, double targetY,
                                            double sectorDegrees)
    {
        double rads = sectorDegrees * Math.PI / 180.0;
        rads -= Math.PI;
        
        // determine offset from origin
        double x = startX - targetX;
        double y = startY - targetY;
        
        double mag = ENumUtil.distance(startX, startY, targetX, targetY);
        // TODO: implement some kind of 'range' mechanic that is accessible through entity -> component ?
        double maxRange = entity.getMaxRange();
        // clamp range to entity's max attack range
        //if (mag > maxRange) {
            x /= mag;
            y /= mag;
            x *= maxRange;
            y *= maxRange;
            mag = maxRange;
        //}
        
        double upperCos = Math.cos(rads);
        double upperSin = Math.sin(rads);
        double lowerCos = Math.cos(-rads);
        double lowerSin = Math.sin(-rads);
        
        // determine resultant world pixel coordinates based on 2D rotation math
        double upperPX = x * upperCos - y * upperSin + startX;
        double upperPY = x * upperSin + y * upperCos + startY;
        double lowerPX = x * lowerCos - y * lowerSin + startX;
        double lowerPY = x * lowerSin + y * lowerCos + startY;
        
        
        final var camera = Envision.levelManager.getCamera();
        double[] lower = camera.convertWorldPxToScreenPx(upperPX, upperPY);
        double[] upper = camera.convertWorldPxToScreenPx(lowerPX, lowerPY);
        
        double upperX = upper[0];
        double upperY = upper[1];
        double lowerX = lower[0];
        double lowerY = lower[1];
        
        double[] entityPos = camera.convertWorldPxToScreenPx(entity.midX, entity.midY);
        double midX = entityPos[0];
        double midY = entityPos[1];
        
        int lightLevel = entity.world.getAmbientLightLevel();
        // could probably be more formalized..
        RenderingManager.drawLine(midX, midY, lowerX, lowerY, 2, EColors.lgray.brightness(lightLevel));
        //RenderingManager.drawStringCS((int) lowerPX/* + " : " + (int) lowerPY */, lowerX, lowerY, EColors.lgray);
        
        RenderingManager.drawLine(midX, midY, upperX, upperY, 2, EColors.lgray.brightness(lightLevel));
        //RenderingManager.drawStringCS((int) upperPX/* + " : " + (int) upperPY */, upperX, upperY, EColors.lgray);
    }
    
    /**
     * Attack entities that are in range (and direction) of the attacking entity.
     * 
     * @param world           The world that the entity is in
     * 
     * @param attackingEntity The entity performing the attack
     * 
     * @param startX          The starting X position of the attack
     *                        (usually entity's midX (world coordiantes))
     *                        
     * @param startY          The starting Y position of the attack
     *                        (usually entity's midY (world coordinates))
     *                        
     * @param targetX         The X location where to attack
     *                        (in world coordinates)
     *                        
     * @param targetY         The y location where to attack
     *                        (in world coordiantes)
     *                        
     * @param sectorDegrees   The angle of attack that the entity can reach
     *                        (in degrees)
     */
    public static void attackAt(IGameWorld world, Entity attackingEntity,
                                double startX, double startY,
                                double targetX, double targetY,
                                double sectorDegrees)
    {
        double angleDegress = sectorDegrees;
        
        sectorDegrees -= 180.0;
        sectorDegrees %= 360.0;
        
        // determine offset from origin
        double x = startX - targetX;
        double y = startY - targetY;
        double destX = x;
        double destY = y;
        
        double mag = ENumUtil.distance(startX, startY, targetX, targetY);
        // TODO: implement some kind of 'range' mechanic that is accessible through entity -> component ?
        double maxRange = attackingEntity.getMaxRange();
        // clamp range to entity's max attack range
        //if (mag > maxRange) {
            x /= mag;
            y /= mag;
            destX *= maxRange;
            destY *= maxRange;
            mag = maxRange;
        //}
        
        double angle = Math.abs(180.0 - Math.atan2(destY, destX) * (180.0 / Math.PI)) % 360.0;
        double radiusSquared = mag * mag;
        
        EList<Entity> entities = findEntitiesWithinSector(world, attackingEntity,
                                                          startX, startY,
                                                          angle, angleDegress,
                                                          radiusSquared);
        
        // attack entities in range
        final int damage = EntityAttack.calculateMeleeAttackDamage(attackingEntity);
        for (var e : entities) {
            e.attackedBy(attackingEntity, damage);
//            if (!e.isDead()) {
//                double kbMag = -KnockbackCalculator.calculateKnockbackForce(attackingEntity, e, AttackType.MELEE);
//                double kbX = x * kbMag;
//                double kbY = y * kbMag;
//                //System.out.println(kbMag + " : " + kbX + " : " + kbY);
//                e.getPhysicsHandler().applyImpulse(kbX, kbY);
//            }
        }
    }
    
    /**
     * Iterates through entities in the world and determines if they are within
     * the attack cone of the attacking entity.
     * 
     * @param world
     * @param entityIn
     * @param centerX
     * @param centerY
     * @param angleToTarget
     * @param sectorDegrees
     * @param radiusSquared
     * 
     * @return All entities within the attacking entity's attack cone
     */
    static EList<Entity> findEntitiesWithinSector(IGameWorld world, Entity entityIn,
                                                  double centerX, double centerY,
                                                  double angleToTarget, double sectorDegrees,
                                                  double radiusSquared)
    {
        EList<Entity> entities = EList.newList();
        EList<Entity> inWorld = world.getEntitiesInWorld();
        
        // distance that is close enough to the entity that it can always hit regardless of angle
        double minDist = entityIn.width * 0.5;
        double minDistSquared = minDist * minDist;
        
        for (Entity e : inWorld) {
            if (e == entityIn) continue;
            if (e.isInvincible()) continue;
            
            final var cDims = e.getCollisionDims();
            double pointX = cDims.midX - centerX;
            double pointY = cDims.midY - centerY;
            
            // if the distance to the entity is smaller than the min distance, always add it
            double distSquared = pointX * pointX + pointY * pointY;
            if (distSquared <= minDistSquared) {
                entities.add(e);
            }
            // otherwise, check if its in the right angle
            else {
                boolean inSector = isPointWithinSector(pointX, pointY, angleToTarget, sectorDegrees, radiusSquared);
                if (inSector) entities.add(e);
            }
        }
        
        return entities;
    }
    
    /**
     * Determines if the given point is within the attack cone based on radius and angle.
     * 
     * @param pointX
     * @param pointY
     * @param angleToTarget
     * @param sectorDegrees
     * @param radiusSquared
     * 
     * @return True if within attack cone
     */
    static boolean isPointWithinSector(double pointX, double pointY,
                                       double angleToTarget, double sectorDegrees,
                                       double radiusSquared)
    {
        // determine angle from point to origin (attacking entity position)
        double rel = Math.atan2(pointX, pointY) * (180.0 / Math.PI) - 90.0;
        
        // 'ronalchn' - StackOverflow - 09/02/2012
        // https://stackoverflow.com/questions/12234574/calculating-if-an-angle-is-between-two-angles
        double angleDiff = (angleToTarget - rel + 180.0 + 360.0) % 360.0 - 180.0;
        
        boolean withinRadius = pointX * pointX + pointY * pointY <= radiusSquared;
        boolean gteLower = angleDiff >= -sectorDegrees; // greater than or equal to
        boolean lteUpper = angleDiff <= sectorDegrees; // less than or equal to
        
        return withinRadius && gteLower && lteUpper;
    }
    
}
