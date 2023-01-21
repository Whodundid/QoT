package envisionEngine.gameEngine.world.worldTiles.categories.farm;

import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import envisionEngine.renderEngine.textureSystem.GameTexture;
import qot.assets.textures.world.farmland.FarmTextures;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;

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
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		super.draw(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
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
