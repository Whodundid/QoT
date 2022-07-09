package game.worldTiles;

import static eutil.reflection.ReflectionHelper.*;
import static eutil.reflection.EModifier.*;

import eutil.EUtil;
import eutil.datatypes.EArrayList;
import game.worldTiles.tileCategories.DungeonTiles;
import game.worldTiles.tileCategories.NatureTiles;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

/** Keeps track of all world tiles in the game. */
public class GlobalTileList {
	
	private static EArrayList<WorldTile> tiles = new EArrayList<WorldTile>();
	
	//--------------
	// Constructors
	//--------------
	
	private GlobalTileList() {}
	
	//--------
	// Static
	//--------
	
	static {
		registerCategory(NatureTiles.class);
		registerCategory(DungeonTiles.class);
	}
	
	//-------------------------------------------
	
	public static void register(WorldTile tileIn) { tiles.add(tileIn); }
	
	public static void registerCategory(TileCategory catIn) { registerCategory(catIn.getClass()); }
	public static void registerCategory(Class c) {
		if (!TileCategory.class.isAssignableFrom(c)) return;
		Arrays.stream(c.getDeclaredFields())
			  .filter(GlobalTileList::isTile)
			  .map(GlobalTileList::toTile)
			  .sorted(Comparator.comparing(t -> t.getID()))
			  .forEach(tiles::add);
	}
	
	/** Returns true iff the field is all of the following:
	 * 	public, static, accessible, and is assignable from the WorldTile class. */
	private static boolean isTile(Field f) { return publicStatic(f) && assignableFrom(f, WorldTile.class); }
	/** Casts the field's object back as a WorldTile. */
	private static WorldTile toTile(Field f) { return (WorldTile) forceGet(f); }
	
	//------------------------------------------
	
	public static WorldTile getTileFromName(String tileName) { return getTileFromName(tileName, 0); }
	public static WorldTile getTileFromName(String tileName, int texID) {
		WorldTile tile = EUtil.getFirst(tiles, t -> t.getName().equals(tileName));
		return (tile != null) ? getTileInstance(tile, texID) : null;
	}
	
	public static WorldTile getTileFromID(int id) { return getTileFromID(id, 0); }
	public static WorldTile getTileFromID(int id, int texID) {
		WorldTile tile = EUtil.getFirst(tiles, t -> t.getID() == id);
		return (tile != null) ? getTileInstance(tile, texID) : null;
	}
	
	public static WorldTile getTileInstance(WorldTile tile, int texID) {
		if (tile != null) {
			try {
				Class<? extends WorldTile> c = tile.getClass();
				if (hasConstructor(tile, Integer.class)) return c.getConstructor(Integer.class).newInstance(texID);
				else return c.getConstructor().newInstance();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static EArrayList<WorldTile> getTiles() { return new EArrayList(tiles); }
	
}
