package qot.world_tiles.categories.farm;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.farmland.FarmTextures;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;
import qot.world_tiles.TileIDs;

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
		setSprite(new Sprite(FarmTextures.farm_0));
		setSideSprite(new Sprite(WoodFloorTextures.wood_siding));
	}
	
	//-----------
	// Overrides
	//-----------
	
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
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new FarmPlot());
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
		
		setSprite(new Sprite(tex));
	}
	
	//---------
	// Methods
	//---------
	
	public void plantCrops() {
		growState = 0;
		update();
	}
	
}
