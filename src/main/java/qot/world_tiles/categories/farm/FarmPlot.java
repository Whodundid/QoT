package qot.world_tiles.categories.farm;

import envision.Envision;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.game.entities.Entity;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.farmland.FarmTextures;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;
import qot.world_tiles.TileIDs;

public class FarmPlot extends WorldTile {

	private int growState;
	private int curGrowAmount = 0;
	private int nextGrowStage = 5000;
	private boolean fullyGrown = false;
	
	//==============
    // Constructors
    //==============
	
	public FarmPlot() {
		super(TileIDs.FARM_PLOT);
		blocksMovement = true;
		setWall(true);
		wallHeight = 0.20;
		setSprite(new Sprite(FarmTextures.farm_0));
		setSideSprite(new Sprite(WoodFloorTextures.wood_siding));
		
		growState = ERandomUtil.getRoll(0, 3);
		setMiniMapColor(0xff8E5C39);
	}
	
	//===========
    // Overrides
    //===========
	
	@Override
	public void onWorldTick() {
		if (growState == 0) return; //ignore if nothing is planted
		if (growState >= 3) return; //don't grow past fully grown
		
		curGrowAmount++;
		if (curGrowAmount >= nextGrowStage) {
		    growState++;
			update();
			curGrowAmount = 0;
		}
	}
	
	@Override
	public void onMousePress(int mXIn, int mYIn, int button) {
        if (button == 1) {
            RightClickMenu rcm = new RightClickMenu(getName());
            
            if (fullyGrown) rcm.addOption("Harvest", () -> harvest(Envision.thePlayer));
            else            rcm.addOption("Plant Crops", this::plantCrops);
            
            rcm.displayOnCurrent();
        }
	}
	
	@Override public boolean hasVariation() { return true; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new FarmPlot());
	}
	
	//=========================
    // Internal Helper Methods
    //=========================
	
	private void update() {
		GameTexture tex = switch (growState) {
		case 0 -> FarmTextures.farm_0;
		case 1 -> FarmTextures.farm_1;
		case 2 -> FarmTextures.farm_2;
		case 3 -> FarmTextures.farm_3;
		default -> null;
		};
		
		if (growState >= 3) {
		    fullyGrown = true;
		}
		
		setSprite(new Sprite(tex));
	}
	
	//=========
	// Methods
	//=========
	
	public void plantCrops() {
		growState = 1;
		curGrowAmount = 0;
		update();
	}
	
	public void harvest(Entity harvestingEntity) {
	    if (!fullyGrown) return;
	    growState = 0;
	    curGrowAmount = 0;
	    update();
	}
	
}
