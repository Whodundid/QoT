package gameSystems.mapSystem.worldTiles;

import eutil.EUtil;
import gameSystems.mapSystem.worldTiles.tiles.CrackedDirt;
import gameSystems.mapSystem.worldTiles.tiles.DarkGrass;
import gameSystems.mapSystem.worldTiles.tiles.Dirt;
import gameSystems.mapSystem.worldTiles.tiles.Grass;
import gameSystems.mapSystem.worldTiles.tiles.Mud;
import gameSystems.mapSystem.worldTiles.tiles.RedSand;
import gameSystems.mapSystem.worldTiles.tiles.RockyStone;
import gameSystems.mapSystem.worldTiles.tiles.Sand;
import gameSystems.mapSystem.worldTiles.tiles.Stone;
import gameSystems.mapSystem.worldTiles.tiles.Water;
import gameSystems.mapSystem.worldTiles.tiles.Wood;
import miscUtil.ReflectionHelper;
import storageUtil.EArrayList;

public class WorldTiles {
	
	private WorldTiles() {}
	
	private static final EArrayList<WorldTile> tiles = new EArrayList();
	
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
	}
	
	public static WorldTile getTileFromName(String tileName) { return getTileFromName(tileName, 0); }
	public static WorldTile getTileFromName(String tileName, int texID) {
		WorldTile tile = EUtil.getFirst(tiles, t -> t.getName().equals(tileName));
		if (tile != null) {
			try {
				if (texID == 0) {
					return tile.getClass().getConstructor().newInstance();
				}
				else if (ReflectionHelper.doesConstructorExist(tile, Integer.class)) {
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
				if (ReflectionHelper.doesConstructorExist(tile, Integer.class)) {
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
