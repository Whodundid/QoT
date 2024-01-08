package qot.world_tiles;

import static eutil.reflection.EModifier.*;
import static eutil.reflection.EReflectionUtil.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.world_tiles.categories.DungeonTiles;
import qot.world_tiles.categories.FarmTiles;
import qot.world_tiles.categories.HouseTiles;
import qot.world_tiles.categories.NatureTiles;
import qot.world_tiles.categories.StoneTiles;

/** Keeps track of all world tiles in the game. */
public class GlobalTileList {
	
    //========
    // Fields
    //========
    
	private static EList<WorldTile> tiles = EList.newList();
	
	private static Map<String, WorldTile> tileNameMap = new HashMap<>();
	private static Map<Integer, WorldTile> tileIDMap = new HashMap<>();
	
	//==============
    // Constructors
    //==============
	
	private GlobalTileList() {}
	
	//========
	// Static
	//========
	
	static {
//	    ids = EList.newList();
		registerCategory(NatureTiles.class);
		registerCategory(DungeonTiles.class);
		registerCategory(FarmTiles.class);
		registerCategory(HouseTiles.class);
		registerCategory(StoneTiles.class);
	}
	
	//-------------------------------------------
	
	public static void register(WorldTile tileIn) { tiles.add(tileIn); }
	
//	public static final EList<Integer> ids;
	
	public static void registerCategory(TileCategory catIn) { registerCategory(catIn.getClass()); }
	public static void registerCategory(Class c) {
		if (!TileCategory.class.isAssignableFrom(c)) return;
		Arrays.stream(c.getDeclaredFields())
			  .filter(GlobalTileList::isTile)
			  .map(GlobalTileList::toTile)
			  .sorted(Comparator.comparing(t -> t.getID()))
			  .forEach(t -> {
			      tiles.add(t);
//			      ids.add(t.getID());
			      //System.out.println(t.getID() + " : " + t.getName());
			      tileNameMap.put(t.getName(), t);
			      tileIDMap.put(t.getID(), t);
			  });
		
//		ids.sort((a, b) -> Integer.compare(a, b));
//		System.out.println(ids);
	}
	
	/** Returns true iff the field is all of the following:
	 * 	public, static, accessible, and is assignable from the WorldTile class. */
	private static boolean isTile(Field f) { return publicStatic(f) && assignableFrom(f, WorldTile.class); }
	/** Casts the field's object back as a WorldTile. */
	private static WorldTile toTile(Field f) { return (WorldTile) forceGet(f); }
	
	//------------------------------------------
	
	public static WorldTile getTileFromName(String tileName) { return getTileFromName(tileName, 0); }
	public static WorldTile getTileFromName(String tileName, int texID) {
	    WorldTile tile = tileNameMap.get(tileName);
		return (tile != null) ? createNewTileInstance(tile, texID) : null;
	}
	
	public static WorldTile getTileFromID(int id) { return getTileFromID(id, 0); }
	public static WorldTile getTileFromID(int id, int texID) {
	    WorldTile tile = tileIDMap.get(id);
		return (tile != null) ? createNewTileInstance(tile, texID) : null;
	}
	
	public static WorldTile createNewTileInstance(WorldTile tile, int texID) {
	    if (tile == null) {
	        return null;
	    }
	    
	    //if (tile.hasVariation()) {
	        try {
	            Class<? extends WorldTile> c = tile.getClass();
	            if (hasConstructor(tile, Integer.class)) return c.getConstructor(Integer.class).newInstance(texID);
	            else return c.getConstructor().newInstance();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    //}
	    
	    //return tile;
	}
	
	public static EArrayList<WorldTile> getTiles() { return new EArrayList(tiles); }
	
}
