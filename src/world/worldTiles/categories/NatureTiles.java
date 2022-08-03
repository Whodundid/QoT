package world.worldTiles.categories;

import world.worldTiles.TileCategory;
import world.worldTiles.WorldTile;
import world.worldTiles.categories.farm.FarmPlot;
import world.worldTiles.categories.nature.Bush0;
import world.worldTiles.categories.nature.CrackedDirt;
import world.worldTiles.categories.nature.DarkGrass;
import world.worldTiles.categories.nature.Dirt;
import world.worldTiles.categories.nature.Grass;
import world.worldTiles.categories.nature.Mud;
import world.worldTiles.categories.nature.RedSand;
import world.worldTiles.categories.nature.RockyStone;
import world.worldTiles.categories.nature.Sand;
import world.worldTiles.categories.nature.Stone;
import world.worldTiles.categories.nature.Water;
import world.worldTiles.categories.nature.Wood;

public class NatureTiles implements TileCategory {
	
	private NatureTiles() {}
	
	public static final WorldTile dirt = new Dirt();
	public static final WorldTile grass = new Grass();
	public static final WorldTile crackedDirt = new CrackedDirt();
	public static final WorldTile darkGrass = new DarkGrass();
	public static final WorldTile mud = new Mud();
	public static final WorldTile redSand = new RedSand();
	public static final WorldTile rockyStone = new RockyStone();
	public static final WorldTile sand = new Sand();
	public static final WorldTile stone = new Stone();
	public static final WorldTile water = new Water();
	public static final WorldTile wood = new Wood();
	public static final WorldTile bush = new Bush0();
	public static final WorldTile farmPlot = new FarmPlot();
	
}
