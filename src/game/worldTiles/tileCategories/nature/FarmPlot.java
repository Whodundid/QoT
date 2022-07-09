package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import engine.renderEngine.textureSystem.GameTexture;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;
import world.GameWorld;

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
		setTexture(WorldTextures.farm0);
		setSideTexture(WorldTextures.wood_siding);
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
		if (growState >= 4) return; //don't grow past fully grown
		
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
		case 0 -> WorldTextures.farm0;
		case 1 -> WorldTextures.farm1;
		case 2 -> WorldTextures.farm2;
		case 3 -> WorldTextures.farm3;
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
