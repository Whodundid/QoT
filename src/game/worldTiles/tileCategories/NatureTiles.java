package game.worldTiles.tileCategories;

import game.worldTiles.TileCategory;
import game.worldTiles.WorldTile;
import game.worldTiles.tileCategories.nature.Bush0;
import game.worldTiles.tileCategories.nature.CrackedDirt;
import game.worldTiles.tileCategories.nature.DarkGrass;
import game.worldTiles.tileCategories.nature.Dirt;
import game.worldTiles.tileCategories.nature.FarmPlot;
import game.worldTiles.tileCategories.nature.Grass;
import game.worldTiles.tileCategories.nature.Mud;
import game.worldTiles.tileCategories.nature.RedSand;
import game.worldTiles.tileCategories.nature.RockyStone;
import game.worldTiles.tileCategories.nature.Sand;
import game.worldTiles.tileCategories.nature.Stone;
import game.worldTiles.tileCategories.nature.Water;
import game.worldTiles.tileCategories.nature.Wood;

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
