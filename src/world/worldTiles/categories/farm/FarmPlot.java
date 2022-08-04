package world.worldTiles.categories.farm;

import assets.textures.world.farmland.FarmTextures;
import assets.textures.world.floors.wood.WoodFloorTextures;
import engine.renderEngine.textureSystem.GameTexture;
import world.GameWorld;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class FarmPlot extends WorldTile {

	private int growState;
	private int curGrowAmount = 0;
	private int nextGrowStage = 600;
	
	//--------------
	// Constructors
	//--------------
	
	public FarmPlot() {
		super(TileIDs.FARM_PLOT);
		blocksMovement = true;
		setWall(true);
		wallHeight = 0.20;
		setTexture(FarmTextures.farm_0);
		setSideTexture(WoodFloorTextures.wood_siding);
	}
	
	//-----------
	// Overrides
	//-----------

	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		super.renderTile(world, x, y, w, h, brightness);
	}
	
	@Override
	public void onWorldTick() {
		if (growState == 0) return; //ignore if nothing is planted
		if (growState >= 3) return; //don't grow past fully grown
		
		curGrowAmount++;
		if (curGrowAmount >= nextGrowStage) {
			update();
			curGrowAmount = 0;
		}
	}
	
	
	
	//------------------
	// Internal Methods
	//------------------
	
	private void update() {
		growState++;
		
		GameTexture tex = switch (growState) {
		case 0 -> FarmTextures.farm_0;
		case 1 -> FarmTextures.farm_1;
		case 2 -> FarmTextures.farm_2;
		case 3 -> FarmTextures.farm_3;
		default -> null;
		};
		
		setTexture(tex);
	}
	
	//---------
	// Methods
	//---------
	
	public void plantCrops() {
		growState = 0;
		update();
	}
	
}
