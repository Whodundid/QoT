package gameSystems.mapSystem.worldTiles;

import gameSystems.mapSystem.worldTiles.tiles.*;
import util.EUtil;
import util.storageUtil.EArrayList;

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
	}
	
	public static WorldTile getTileFromName(String tileName) {
		return EUtil.getFirst(tiles, t -> t.getName().equals(tileName));
	}
	
	public static WorldTile getTileFromID(int id) {
		return EUtil.getFirst(tiles, t -> t.getID() == id);
	}
	
	public static EArrayList<WorldTile> getTiles() { return new EArrayList(tiles); }
	
}
