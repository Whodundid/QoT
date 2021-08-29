package assets.worldTiles;

import assets.worldTiles.tiles.CrackedDirt;
import assets.worldTiles.tiles.DarkGrass;
import assets.worldTiles.tiles.Dirt;
import assets.worldTiles.tiles.Grass;
import assets.worldTiles.tiles.Mud;
import assets.worldTiles.tiles.RedSand;
import assets.worldTiles.tiles.RockyStone;
import assets.worldTiles.tiles.Sand;
import assets.worldTiles.tiles.Stone;
import assets.worldTiles.tiles.Water;
import assets.worldTiles.tiles.Wood;
import eutil.EUtil;
import eutil.reflection.ReflectionUtil;
import eutil.storage.EArrayList;
import java.util.Collections;

public class WorldTiles {
	
	private WorldTiles() {}
	
	private static EArrayList<WorldTile> tiles = new EArrayList<WorldTile>();
	
	public static final WorldTile crackedDirt = new CrackedDirt();
	public static final WorldTile darkGrass = new DarkGrass();
	public static final WorldTile dirt = new Dirt();
	public static final WorldTile grass = new Grass();
	public static final WorldTile mud = new Mud();
	public static final WorldTile redSand = new RedSand();
	public static final WorldTile rockyStone = new RockyStone();
	public static final WorldTile sand = new Sand();
	public static final WorldTile stone = new Stone();
	public static final WorldTile water = new Water();
	public static final WorldTile wood = new Wood();
	
	static {
		tiles.add(crackedDirt);
		tiles.add(darkGrass);
		tiles.add(dirt);
		tiles.add(grass);
		tiles.add(mud);
		tiles.add(redSand);
		tiles.add(rockyStone);
		tiles.add(sand);
		tiles.add(stone);
		tiles.add(water);
		tiles.add(wood);
		
		Collections.sort(tiles);
	}
	
	public static WorldTile getTileFromName(String tileName) { return getTileFromName(tileName, 0); }
	public static WorldTile getTileFromName(String tileName, int texID) {
		WorldTile tile = EUtil.getFirst(tiles, t -> t.getName().equals(tileName));
		if (tile != null) {
			try {
				if (texID == 0) {
					return tile.getClass().getConstructor().newInstance();
				}
				else if (ReflectionUtil.doesConstructorExist(tile, Integer.class)) {
					return tile.getClass().getConstructor(Integer.class).newInstance(texID);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static WorldTile getTileFromID(int id) { return getTileFromID(id, 0); }
	public static WorldTile getTileFromID(int id, int texID) {
		WorldTile tile = EUtil.getFirst(tiles, t -> t.getID() == id);
		if (tile != null) {
			try {
				if (ReflectionUtil.doesConstructorExist(tile, Integer.class)) {
					return tile.getClass().getConstructor(Integer.class).newInstance(texID);
				}
				else {
					return tile.getClass().getConstructor().newInstance();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static EArrayList<WorldTile> getTiles() { return new EArrayList(tiles); }
	
}
