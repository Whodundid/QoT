package qot.world_tiles.categories.test_tiles;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.colors.EColors;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class TestGrass extends WorldTile {
	
	int level;
	EColors color;
	
	public TestGrass() { this(-1); }
	public TestGrass(int id) {
		super(TileIDs.GRASS);
		
		numVariants = 4;
		level = ERandomUtil.getRoll(232, 255);
		int chooseColor = ERandomUtil.getRoll(0, 1);
		switch (chooseColor) {
		case 0: color = EColors.lgreen; break;
		case 1: color = EColors.green; break;
		//case 2: color = EColors.dgreen; break;
		}
		
		if (id < 0) {
		    setSprite(new Sprite(GrassTextures.grass.getRandVariant()));
		}
		else {
		    setSprite(new Sprite(GrassTextures.grass.getChild(id)));
		}
		
		blocksMovement = false;
	}
	
//	@Override
//	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
//		double wh = h * wallHeight; //wh == 'wallHeight'
//		
//		if (isWall) {
//			//determine tile brightness
//			int tileBrightness = brightness;
//			int wallBrightness = brightness;
//			
//			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
//			
//			//draw main texture slightly above main location
//			drawTexture(tex, x, y - wh, w, h, false, tileBrightness);
//			
//			//check if the tile directly above is a wall
//			//if so - don't draw wall side
//			WorldTile tb = null; // tb == 'tileBelow'
//			if ((worldY + 1) < world.getHeight()) tb = world.getTileAt(worldX, worldY + 1);
//			if ((tb == null ||
//				!tb.hasTexture() ||
//				 tb.getWallHeight() < wh) ||
//				!tb.isWall())
//			{
//				double yPos;
//				
//				if (wh > 0) {
//					yPos = y + h - wh;
//					wallBrightness = EColors.changeBrightness(brightness, 125);
//				}
//				else {
//					yPos = y - wh;
//					wallBrightness = brightness;
//				}
//				
//				//draw wall side slightly below
//				GameTexture side = (sideTex != null) ? sideTex : tex;
//				drawTexture(side, x, yPos, w, wh, false, wallBrightness);
//			}
//		}
//		else {
//			drawTexture(tex, x, y, w, h, false, color.brightness(level));
//		}
//		
//		//draw bottom of map edge or if right above a tile with no texture/void
//		WorldTile tileBelow = null;
//		if ((worldY + 1) < world.getHeight()) tileBelow = world.getTileAt(worldX, worldY + 1);
//		if ((tileBelow == null || !tileBelow.hasTexture())) {
//			drawTexture(tex, x, y + h, w, h / 2, false, EColors.changeBrightness(brightness, 125));
//		}
//		
//		if (mouseOver) {
//			if (isWall) {
//				drawHRect(x, y - wh, x + w, y - wh + h, 1, EColors.chalk);
//				drawHRect(x, y + h - wh - 1, x + w, y + h, 1, EColors.chalk);
//			}
//			else {
//				drawHRect(x, y, x + w, y + h, 1, EColors.chalk);
//			}
//		}
//	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new TestGrass());
	}
	
}
