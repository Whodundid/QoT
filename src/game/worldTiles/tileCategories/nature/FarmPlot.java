package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import engine.renderEngine.textureSystem.GameTexture;
import eutil.colors.EColors;
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
		double wh = h * wallHeight; //wh == 'wallHeight'
		
		if (isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			
			//draw main texture slightly above main location
			drawTexture(tex, x, y - wh, w, h, false, tileBrightness);
			
			//check if the tile directly above is a wall
			//if so - don't draw wall side
			WorldTile tb = null; // tb == 'tileBelow'
			if ((worldY + 1) < world.getHeight()) tb = world.getWorldData()[worldX][worldY + 1];
			if ((tb == null ||
				!tb.hasTexture() ||
				 tb.getWallHeight() < wh) ||
				!tb.isWall())
			{
				double yPos;
				
				if (wh > 0) {
					yPos = y + h - wh;
					wallBrightness = EColors.changeBrightness(brightness, 200);
				}
				else {
					yPos = y - wh;
					wallBrightness = brightness;
				}
				
				//draw wall side slightly below
				GameTexture side = (sideTex != null) ? sideTex : tex;
				drawTexture(side, x, yPos, w, wh, false, wallBrightness);
			}
		}
		else {
			drawTexture(tex, x, y, w, h, false, brightness);
		}
		
		//draw bottom of map edge or if right above a tile with no texture/void
		WorldTile tileBelow = null;
		if ((worldY + 1) < world.getHeight()) tileBelow = world.getWorldData()[worldX][worldY + 1];
		if ((tileBelow == null || !tileBelow.hasTexture())) {
			drawTexture(tex, x, y + h, w, h / 2, false, EColors.changeBrightness(brightness, 125));
		}
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
