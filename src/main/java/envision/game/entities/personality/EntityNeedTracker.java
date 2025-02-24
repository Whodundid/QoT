package envision.game.entities.personality;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import envision.engine.loader.built.game.Entity;

public class EntityNeedTracker {
    
    //========
    // Fields
    //========
    
    public Entity theEntity;
    public Map<EntityNeed, Float> needTrackerMap = new HashMap<>();    
    //==============
    // Constructors
    //==============
    
    public EntityNeedTracker(Entity entityIn) {
        theEntity = entityIn;
    }    
    //=========
    // Methods
    //=========
    
    public void addNeed(EntityNeed need, float initialValue) {
        needTrackerMap.put(need, initialValue);
    }
    
    public void removeNeed(EntityNeed need) {
        needTrackerMap.remove(need);
    }
    
    public Set<EntityNeed> getNeeds() {
        return needTrackerMap.keySet();
    }
    
    public float getNeedValue(EntityNeed need) {
        return needTrackerMap.getOrDefault(need, Float.NaN);
    }
    
    public void setNeedValue(EntityNeed need, float value) {
        if (!needTrackerMap.containsKey(need)) return;
        needTrackerMap.put(need, value);
    }
    
    public void addToNeedValue(EntityNeed need, float amountToAdd) {
        if (!needTrackerMap.containsKey(need)) return;
        needTrackerMap.put(need, needTrackerMap.get(need) + amountToAdd);
    }
    
}
