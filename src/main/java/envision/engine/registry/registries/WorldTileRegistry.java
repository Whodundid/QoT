package envision.engine.registry.registries;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class WorldTileRegistry {
    
    private static volatile int RESOURCE_ID;
    private static synchronized int nextID() { return RESOURCE_ID++; }
    
    //========
    // Fields
    //========
    
    /** List of all currently registered tiles (NOTE: There can be null values!). */
    private static final EList<WorldTile> registeredTiles = EList.newList();
    /** A list to keep track of open indicies in the registered tiles list. */
    private static final EList<Integer> openIndexList = EList.newList();
    /** Maps the registered ID to the world tile's index in the registered tiles list. */
    private static final ConcurrentMap<Integer, Integer> idToIndexMap = new ConcurrentHashMap<>();
    /** Maps the resource name to the world tile's index in the registered tiles list. */
    private static final ConcurrentMap<String, Integer> nameToIndexMap = new ConcurrentHashMap<>();
    /** Maps the resource name to the registered resource ID. */
    private static final ConcurrentMap<String, Integer> nameToResourceIdMap = new ConcurrentHashMap<>();
    
    //=========
    // Getters
    //=========
    
    public static EList<WorldTile> getRegisteredTiles() {
        return registeredTiles.copy().purgeNulls().toUnmodifiableList();
    }
    
    public static WorldTile getTileFromID(int id) {
        Integer index = idToIndexMap.get(id);
        if (index == null) return null;
        return registeredTiles.get(index);
    }
    
    public static WorldTile getTileByResourceName(String name) {
        Integer index = nameToIndexMap.get(name);
        if (index == null) return null;
        return registeredTiles.get(index);
    }
    
    public static Integer getTileResourceID(String name) {
        return nameToResourceIdMap.get(name);
    }
    
    public static Integer getTileResourceID(WorldTile tile) {
        return nameToResourceIdMap.get(tile.tileName);
    }
    
    //==================
    // Internal Methods
    //==================
    
    public static synchronized void registerAllTiles(EList<WorldTile> tiles) {
        unregisterAllTiles();
        
        for (WorldTile t : tiles) {
            registerTile(t);
        }
    }
    
    public static synchronized void registerTile(WorldTile tile) {
        final int id = nextID();
        final int listIndex;
        final String name = tile.tileName;
        
        // use an index that's open first before adding more to the list
        if (openIndexList.isNotEmpty()) {
            listIndex = openIndexList.removeFirst();
            registeredTiles.set(listIndex, tile);
        }
        else {
            listIndex = registeredTiles.size();
            registeredTiles.add(tile);
        }
        
        idToIndexMap.put(id, listIndex);
        nameToIndexMap.put(name, listIndex);
        nameToResourceIdMap.put(name, id);
    }
    
    public static synchronized void unregisterAllTiles() {
        registeredTiles.clear();
        openIndexList.clear();
        idToIndexMap.clear();
        nameToIndexMap.clear();
        nameToResourceIdMap.clear();
    }
    
    public static synchronized void unregisterTile(WorldTile tile) {
        // if the incoming tile is null, then there's nothing to unregister
        if (tile == null) return;
        
        String name = tile.getName();
        Integer id = nameToResourceIdMap.get(name);
        // if the 'id' is null then there isn't a registered tile under that resource name
        if (id == null) return;
        
        Integer index = idToIndexMap.get(id);
        if (index == null) {
            throw new IllegalStateException("A mapped resource: {" + id + ":" + name +
                                            "} does not have an internal index!");
        }
        
        registeredTiles.set(index, null);
        idToIndexMap.remove(id);
        nameToIndexMap.remove(name);
        nameToResourceIdMap.remove(name);
        openIndexList.add(index);
    }
    
}
