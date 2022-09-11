package envision.gameEngine.world.worldTiles.categories;

import envision.gameEngine.world.worldTiles.TileCategory;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldTiles.categories.nature.CoarseSand;
import envision.gameEngine.world.worldTiles.categories.nature.CrackedDirt;
import envision.gameEngine.world.worldTiles.categories.nature.DarkGrass;
import envision.gameEngine.world.worldTiles.categories.nature.Dirt;
import envision.gameEngine.world.worldTiles.categories.nature.DryGrass;
import envision.gameEngine.world.worldTiles.categories.nature.Grass;
import envision.gameEngine.world.worldTiles.categories.nature.LeafyGrass;
import envision.gameEngine.world.worldTiles.categories.nature.LightGrass;
import envision.gameEngine.world.worldTiles.categories.nature.Mud;
import envision.gameEngine.world.worldTiles.categories.nature.RedSand;
import envision.gameEngine.world.worldTiles.categories.nature.RockyStone;
import envision.gameEngine.world.worldTiles.categories.nature.Sand;
import envision.gameEngine.world.worldTiles.categories.nature.Stone;
import envision.gameEngine.world.worldTiles.categories.nature.Water;
import envision.gameEngine.world.worldTiles.categories.nature.Wood;
import envision.gameEngine.world.worldTiles.categories.nature.WoodSlats;

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
	
}
