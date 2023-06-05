package qot.world_tiles.categories;

import envision.game.world.worldTiles.TileCategory;
import envision.game.world.worldTiles.WorldTile;
import qot.world_tiles.categories.nature.*;

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
	public static final WorldTile coarseSand = new CoarseSand();
	public static final WorldTile stone = new Stone();
	public static final WorldTile water = new Water();
	public static final WorldTile wood = new Wood();
	public static final WorldTile woodSlats = new WoodSlats();
	public static final WorldTile dryGrass = new DryGrass();
	public static final WorldTile leafyGrass = new LeafyGrass();
	public static final WorldTile lightGrass = new LightGrass();
	public static final WorldTile coarseDirt = new CoarseDirt();
	public static final WorldTile dryCrackedDirt = new DryCrackedDirt();
	public static final WorldTile dryPlainsGrass = new DryPlainsGrass();
	public static final WorldTile icySnow = new IcySnow();
	public static final WorldTile rockyDirt = new RockyDirt();
	public static final WorldTile roughRocky = new RoughRocky();
	public static final WorldTile sandyDirt = new SandyDirt();
	public static final WorldTile smoothDirt = new SmoothDirt();
	public static final WorldTile wetSand = new WetSand();
	
}
