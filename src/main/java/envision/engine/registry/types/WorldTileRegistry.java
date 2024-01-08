package envision.engine.registry.types;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class WorldTileRegistry {
    
    private final EList<WorldTile> worldTiles = EList.newList();
    private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
    
    public EList<WorldTile> getRegisteredTiles() { return worldTiles; }
    
    public WorldTile getTileFromID(int id) {
        int index = idMap.getOrDefault(id, -1);
        return (index > 0) ? worldTiles.get(index) : null;
    }
    
    public WorldTile getTileFromID(int id, int childId) {
        var tile = getTileFromID(id);
        return tile;
//        return (tile != null) ? tile.getChildVariant(childId) : null;
    }
    
}
