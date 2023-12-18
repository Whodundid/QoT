package envision.game.entities;

public class EntityFavorDecider {
    
    //========
    // Fields
    //========
    
    protected Entity theEntity;
    protected EntityFavorTracker tracker;
    
    //==============
    // Constructors
    //==============
    
    public EntityFavorDecider(Entity theEntityIn) {
        theEntity = theEntityIn;
        if (theEntity != null) tracker = theEntity.getFavorTracker();
    }
    
    //=========
    // Methods
    //=========
    
    public boolean isNeutral(Entity ent) {
        if (ent == null) return false;
        return isNeutral(ent.objectID);
    }
    public boolean isNeutral(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor <= 50 && favor >= -50;
    }
    
    public boolean isGoodFavor(Entity ent) {
        if (ent == null) return false;
        return isGoodFavor(ent.objectID);
    }
    public boolean isGoodFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor <= 150 && favor >= 50;
    }
    
    public boolean isReallyGoodFavor(Entity ent) {
        if (ent == null) return false;
        return isReallyBadFavor(ent.objectID);
    }
    public boolean isReallyGoodFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor >= 150;
    }
    
    public boolean isBadFavor(Entity ent) {
        if (ent == null) return false;
        return isBadFavor(ent.objectID);
    }
    public boolean isBadFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor <= -50 && favor >= -150;
    }
    
    public boolean isReallyBadFavor(Entity ent) {
        if (ent == null) return false;
        return isReallyBadFavor(ent.objectID);
    }
    public boolean isReallyBadFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor <= -150;
    }
    
    public boolean isPositiveFavor(Entity ent) {
        if (ent == null) return false;
        return isPositiveFavor(ent.objectID);
    }
    public boolean isPositiveFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor >= 0;
    }
    
    public boolean isNegativeFavor(Entity ent) {
        if (ent == null) return false;
        return isNegativeFavor(ent.objectID);
    }
    public boolean isNegativeFavor(String entityID) {
        if (tracker == null) return false;
        int favor = tracker.getFavorWithEntity(entityID);
        return favor < 0;
    }
    
}
