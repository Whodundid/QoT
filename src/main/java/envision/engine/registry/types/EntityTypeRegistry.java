package envision.engine.registry.types;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;

public class EntityTypeRegistry {
    
    private final EList<Entity> entityTypes = EList.newList();
    private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
    
    public EList<Entity> getRegisteredEntityTypes() { return entityTypes; }
    
    public Entity createEntityFromTypeID(int id) {
        return null;
    }
    
}
