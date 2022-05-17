package world.resources.tile_categories;

import world.resources.TileCategory;
import world.resources.WorldTile;
import world.resources.tile_categories.nature.Bush0;
import world.resources.tile_categories.nature.CrackedDirt;
import world.resources.tile_categories.nature.DarkGrass;
import world.resources.tile_categories.nature.Dirt;
import world.resources.tile_categories.nature.Farm0;
import world.resources.tile_categories.nature.Farm1;
import world.resources.tile_categories.nature.Farm2;
import world.resources.tile_categories.nature.Farm3;
import world.resources.tile_categories.nature.Grass;
import world.resources.tile_categories.nature.Mud;
import world.resources.tile_categories.nature.RedSand;
import world.resources.tile_categories.nature.RockyStone;
import world.resources.tile_categories.nature.Sand;
import world.resources.tile_categories.nature.Stone;
import world.resources.tile_categories.nature.Water;
import world.resources.tile_categories.nature.Wood;

public class NatureTiles extends TileCategory {
	
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
	public static final WorldTile farm0 = new Farm0();
	public static final WorldTile farm1 = new Farm1();
	public static final WorldTile farm2 = new Farm2();
	public static final WorldTile farm3 = new Farm3();
	
}
