package envision.game.entities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EntityFavorTracker {
    
    //========
    // Fields
    //========
    
    /** The entity for who this favor tracker belongs to. */
    private final Entity theEntity;
    private final ConcurrentMap<String, Integer> favorMap = new ConcurrentHashMap<>();
    
    private volatile long timeSinceLastFavorChange;
    private long favorUpdateInterval = 10000;
    
    //==============
    // Constructors
    //==============
    
    public EntityFavorTracker(Entity entityIn) {
        theEntity = entityIn;
    }
    
    //=========
    // Methods
    //=========
    
    public synchronized void updateFavorOverTime(long dt) {
        timeSinceLastFavorChange += dt;
        
        if (timeSinceLastFavorChange >= favorUpdateInterval) {
            timeSinceLastFavorChange = 0;
        }
        // prevent overflows
        else if (timeSinceLastFavorChange == Long.MAX_VALUE) {
            timeSinceLastFavorChange = 0;
            return;
        }
        else return;
        
        synchronized (favorMap) {
            for (var entry : favorMap.entrySet()) {
                String entityID = entry.getKey();
                Integer favor = entry.getValue();
                
                if (favor == null) continue;
                if (favor < 0) favorMap.put(entityID, favor + 1);
            }
        }
    }
    
    public int getFavorWithEntity(Entity entityIn) {
        if (entityIn == null) return 0;
        return getFavorWithEntity(entityIn.getObjectID());
    }
    
    public int getFavorWithEntity(String entityID) {
        Integer value = favorMap.get(entityID);
        if (value == null) {
            value = 0;
            favorMap.put(entityID, value);
        }
        return value;
    }
    
    public int increaseFavorWithEntity(Entity entityIn, int amountToAdd) {
        if (entityIn == null) return 0;
        return increaseFavorWithEntity(entityIn.getObjectID(), amountToAdd);
    }
    
    public int increaseFavorWithEntity(String entityID, int amountToAdd) {
        Integer value = favorMap.get(entityID);
        if (value == null) value = 0;
        value += amountToAdd;
        favorMap.put(entityID, value);
        return value;
    }
    
    public int decreaseFavorWithEntity(Entity entityIn, int amountToRemove) {
        if (entityIn == null) return 0;
        return decreaseFavorWithEntity(entityIn.getObjectID(), amountToRemove);
    }
    
    public int decreaseFavorWithEntity(String entityID, int amountToRemove) {
        Integer value = favorMap.get(entityID);
        if (value == null) value = 0;
        value -= amountToRemove;
        favorMap.put(entityID, value);
        return value;
    }
    
    /**
     * Removes all favor with all entities.
     */
    public void clear() {
        favorMap.clear();
    }
    
    //=========
    // Getters
    //=========
    
    public Entity getEntity() { return theEntity; }
    public long getFavorUpdateInterval() { return favorUpdateInterval; }
    
    //=========
    // Setters
    //=========
    
    public void setFavorUpdateInterval(long amount) { favorUpdateInterval = amount; }
    
}
