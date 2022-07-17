package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import engine.renderEngine.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.random.RandomUtil;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;
import world.GameWorld;
import eutil.misc.Rotation;

public class Stone extends WorldTile {
	
	Rotation rotation;
	
	public Stone() {
		super(TileIDs.STONE);
		setBlocksMovement(true);
		setTexture(WorldTextures.stone);
		setWall(true);
		wallHeight = RandomUtil.getRoll(0.25, 0.75);
		
		rotation = Rotation.random();
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		double wh = h * wallHeight; //wh == 'wallHeight'
		
		if (isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			
			//draw main texture slightly above main location
			drawTexture(tex, x, y - wh, w, h, false, rotation, tileBrightness);
			
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
					wallBrightness = EColors.changeBrightness(brightness, 125);
				}
				else {
					yPos = y - wh;
					wallBrightness = brightness;
				}
				
				//draw wall side slightly below
				GameTexture side = (sideTex != null) ? sideTex : tex;
				drawTexture(side, x, yPos, w, wh, false, rotation, wallBrightness);
			}
		}
		else {
			drawTexture(tex, x, y, w, h, false, rotation, brightness);
		}
		
		//draw bottom of map edge or if right above a tile with no texture/void
		WorldTile tileBelow = null;
		if ((worldY + 1) < world.getHeight()) tileBelow = world.getWorldData()[worldX][worldY + 1];
		if ((tileBelow == null || !tileBelow.hasTexture())) {
			drawTexture(tex, x, y + h, w, h / 2, false, EColors.changeBrightness(brightness, 125));
		}
	}
	
}
