package envision.game.entities;

import envision.Envision;
import eutil.datatypes.util.EList;
import eutil.misc.Direction;
import eutil.random.ERandomUtil;

//@Deprecated(since = "any time --> remove this in favor of having everything be component/json based")
public abstract class Enemy extends BasicRenderedEntity {
    
    //========
    // Fields
    //========
    
    protected long randShort = 400l;
    protected long randLong = 2500l;
    protected long waitDelay = 0l;
    protected long moveTime = 0l;
    protected long waitTime = 0l;
    protected long lastMove = 0l;
    protected Direction lastDir = Direction.N;
    
    protected EList<Entity> targets = EList.newList();
    protected Entity currentTarget;
    protected int agroRange = 200;
    
    protected long timeSinceMoved = 0l;
    protected long timeLastAttacked = 0;
    protected Entity entityLastAttackedBy = null;
    
    //==============
    // Constructors
    //==============
    
    public Enemy(String nameIn) {
        super(nameIn);
        
        canBeMoved = false;
        canMoveEntities = true;
        this.canRegenHealth = true;
        this.canRegenMana = false;
        this.canRegenStamina = true;
        
        if (Envision.thePlayer != null) {
            favorTracker.decreaseFavorWithEntity(Envision.thePlayer, 75);
        }
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onLivingUpdate(float dt) {
        long cur = System.currentTimeMillis();
        
        if (entityLastAttackedBy != null && cur - timeLastAttacked <= 4000) {
            currentTarget = entityLastAttackedBy;
        }
        else if (currentTarget == null) determineTargets();
        else {
            double dist = world.getDistance(this, currentTarget);
            boolean los = this.hasDirectLineOfSightToObject(currentTarget);
            //System.out.println("CUR TARGET: " + currentTarget + " : " + dist + " : " + los);
            if (!los || dist > agroRange) {
                currentTarget = null;
                determineTargets();
            }
        }
        
        if (currentTarget == null) {
            runPassiveAI(dt);
        }
        else {
            runAggressiveAI(dt);
        }
    }
    
    @Override
    public void attackedBy(Entity ent, int amount) {
        super.attackedBy(ent, amount);
        
        timeLastAttacked = System.currentTimeMillis();
        entityLastAttackedBy = ent;
        
        if (ent != currentTarget) {
            currentTarget = ent;
        }
    }
    
    protected void runPassiveAI(float dt) {}
    protected void runAggressiveAI(float dt) {}
    
    protected void wander() {
        if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
            waitTime = ERandomUtil.getRoll(randShort, randLong);
            moveTime = ERandomUtil.getRoll(randShort, randLong);
            waitDelay = ERandomUtil.getRoll(randShort, randLong);
            lastMove = System.currentTimeMillis();
            lastDir = ERandomUtil.randomDir(true);
        }
        
        if (System.currentTimeMillis() - lastMove >= moveTime) {
            move(lastDir);
        }
        
//        boolean shouldMove = ERandomUtil.roll(10, 0, 10);
//        
//        if (shouldMove) {
//            Direction dir = ERandomUtil.randomDir();
//            move(dir);
//        }
    }
    
    protected void determineTargets() {
        targets.clear();
        EList<Entity> foundTargets = world.getAllEntitiesWithinDistance(this, agroRange);
        var it = foundTargets.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            var favor = getFavorDecider().isPositiveFavor(e);
            if (favor) it.remove();
        }
        this.targets = foundTargets;
        
        var inRange = getEntitiesWithinAgroRange(targets);
        inRange = inRange.filter(e -> this.hasDirectLineOfSightToObject(e));
        Entity lowestFavorTarget = getEntityWithLowestFavor(inRange);
        
        currentTarget = lowestFavorTarget;
        //System.out.println(currentTarget + " : " + targets);
    }
    
    protected EList<Entity> getEntitiesWithinAgroRange(EList<Entity> entities) {
        return entities.filter(e -> world.getDistance(this, e) < agroRange);
    }
    
    protected Entity getClosestEntity(EList<Entity> entites) {
        Entity closest = null;
        double closestDist = Double.MAX_VALUE;
        
        for (Entity e : entites) {
            double dist = world.getDistance(this, e);
            boolean hasLineOfSight = this.hasDirectLineOfSightToObject(e);
            if ((closest == null || dist < closestDist) && dist <= agroRange && hasLineOfSight) {
                closest = e;
                closestDist = dist;
            }
        }
        
        return closest;
    }
    
    protected Entity getEntityWithLowestFavor(EList<Entity> entities) {
        Entity ent = null;
        int lowestFavor = Integer.MAX_VALUE;
        
        for (Entity e : entities) {
            int favor = favorTracker.getFavorWithEntity(e);
            if (ent == null || favor < lowestFavor) {
                ent = e;
                lowestFavor = favor;
            }
        }
        
        return ent;
    }
    
}
