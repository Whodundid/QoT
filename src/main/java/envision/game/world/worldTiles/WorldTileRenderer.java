package envision.game.world.worldTiles;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.registry.types.Sprite;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.game.component.types.RenderingComponent;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.misc.Rotation;

public class WorldTileRenderer extends RenderingComponent {
	
	public WorldTile theTile;
	
	//==============
	// Constructors
	//==============
	
	public WorldTileRenderer(WorldTile tileIn) {
		super(tileIn);
		theTile = tileIn;
	}
	
	//=========
	// Methods
	//=========

	/**
	 * Called from the WorldRenderer whenever the tile is about to be rendered.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param brightness
	 */
	@Override
	public void draw(IGameWorld world, WorldCamera camera) {
		//ignore if there is no texture
		if (!theTile.hasSprite()) return;
		
		double[] draw = camera.calculateDrawDimensions(theTile);
        
        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        
        //boolean mouseOver = theTile.getCameraLayer() == camera.getCurrentLayer() && camera.isMouseOverObject(theTile);
        
        if (BatchManager.isEnabled()) drawTile(world, camera, draw, color, false);
        else drawTile(world, camera, draw, calcBrightness(theTile.worldX, theTile.worldY), false);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	public void drawTile(IGameWorld world, WorldCamera camera, double[] dims, int brightness, boolean mouseOver) {
		
	    double x = dims[0];
	    double y = dims[1];
	    double w = dims[2];
	    double h = dims[3];
	    double wh = h * theTile.wallHeight; //wh == 'wallHeight'
		
		WorldTile tn = null; // tn == 'tileNorth'
		WorldTile ts = null; // ts == 'tileSouth'
		WorldTile ta = null; // ta == 'tileAbove'
		WorldTile tb = null; // tb == 'tileBelow'
		WorldTile taa = null; // taa == 'tileAboveAll'
		int camLayer = theTile.getCameraLayer();
		
		Rotation rot = (theTile.rotationDir != null) ? theTile.rotationDir : Rotation.UP;
		
        if ((theTile.worldY - 1) >= 0) tn = world.getTileAt(camLayer, theTile.worldX, theTile.worldY - 1);
        if ((theTile.worldY + 1) < world.getHeight()) ts = world.getTileAt(camLayer, theTile.worldX, theTile.worldY + 1);
        
        int l = camLayer;
        while ((taa == null || taa == VoidTile.instance) && l < world.getNumberOfLayers() - 1) {
            taa = world.getTileAt(l + 1, theTile.worldX, theTile.worldY);
            l++;
        }
        
        if ((camLayer < world.getNumberOfLayers() - 1)) {
            ta = world.getTileAt(camLayer + 1, theTile.worldX, theTile.worldY);
        }
        if ((camLayer > 0)) {
            tb = world.getTileAt(camLayer - 1, theTile.worldX, theTile.worldY);
        }
		
		if (theTile.isWall && DebugSettings.drawFlatWalls) {
			RenderingManager.drawSprite(theTile.sprite, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (ts == null || !ts.hasSprite())) {
				RenderingManager.drawSprite(theTile.sprite, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
		else if (theTile.isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			if (taa != null && taa != VoidTile.instance) {
			    tileBrightness = EColors.changeBrightness(tileBrightness, 160);
			}
			
			// check if the tile directly north is a wall
			// if so - don't draw wall side
			if (wh >= 0) {
				//draw main texture slightly above main location
			    if (ta == null || ta == VoidTile.instance || camLayer <= camera.getUpperCameraLayer()) {
//			        if (ta != null && ta != VoidTile.instance) RenderingManager.drawSprite(theTile.sprite, x, y - h, w, h, theTile.drawFlipped, rot, tileBrightness);
//			        else
			            RenderingManager.drawSprite(theTile.sprite, x, y - wh, w, h, theTile.drawFlipped, rot, tileBrightness);
			    }
			    
				Sprite side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
				
				double yPos = y + h - wh;
				wallBrightness = EColors.changeBrightness(brightness, 165);
				
				//draw wall side slightly below
				if ((ts == null || ts == VoidTile.instance || !ts.isWall) || (ts.wallHeight != theTile.wallHeight)) {
//				    if (ta != null && ta != VoidTile.instance) {
//				        yPos = y + h;
//				        RenderingManager.drawSprite(side, x, y, w, h, theTile.drawFlipped, rot, wallBrightness);
//				    }
//				    else
				        RenderingManager.drawSprite(side, x, yPos, w, wh, theTile.drawFlipped, rot, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if ((ts == null || !ts.hasSprite()) && theTile.getCameraLayer() == 0) {
					RenderingManager.drawSprite(theTile.sprite, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
			else {
				wh = -wh;
				double yPos = y + wh;
				
				//draw main texture slightly below main location
				RenderingManager.drawSprite(theTile.sprite, x, yPos, w, h, theTile.drawFlipped, rot, tileBrightness);
				
				//I don't want to draw if tn is null
				//but
				//I also don't want to draw if tn is a wall and has the same wall height as this one
				
				if (tn != null && (!tn.isWall || ((h * tn.wallHeight) != -wh))) {
					Sprite side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
					
					wallBrightness = EColors.changeBrightness(brightness, 145);
					side = (tn.sideTex != null) ? tn.sideTex : tn.sprite;
					
					double sideWallY = yPos - wh;
					
					//THIS IS NOT QUITE RIGHT -- the yPos needs to take into account whether or
					//not the tile above is a wall and if so what height the wall is at and then
					//size the wh accordingly to fit the area in between the ta's end wh and this
					//tiles yPos
					
					//if (ta.isWall) {
					//	if (ta.wallHeight < 0)
					//}
					
					//draw wall side slightly above
					if (side != null) {
					    RenderingManager.drawSprite(side, x, sideWallY, w, wh, theTile.drawFlipped, rot, wallBrightness);					    
					}
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if (ts == null || !ts.hasSprite()) {
					RenderingManager.drawSprite(theTile.sprite, x, yPos + h, w, (h / 2) - wh, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
		}
		else {
		    if (taa != null && taa != VoidTile.instance) {
                brightness = EColors.changeBrightness(brightness, 160);
            }
		    
			RenderingManager.drawSprite(theTile.sprite, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (ts == null || !ts.hasSprite()) && theTile.getCameraLayer() == 0) {
				var side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
				RenderingManager.drawSprite(side, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
        
//        int tileBrightness = brightness;
//        if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
//        
//        double yPos = (theTile.isWall) ? y + wh : y;
//        
//        RenderingManager.drawSprite(theTile.sprite, x, yPos, w, h, theTile.drawFlipped, rot, tileBrightness);
//        
//        Sprite side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
//        RenderingManager.drawSprite(side, x, yPos + h, w, h * 0.5, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
//		
		if (mouseOver) {
			if (theTile.isWall) {
				RenderingManager.drawHRect(x, y - wh, x + w, y - wh + h, 1, EColors.chalk);
				RenderingManager.drawHRect(x, y + h - wh - 1, x + w, y + h, 1, EColors.chalk);
			}
			else {
				RenderingManager.drawHRect(x, y, x + w, y + h, 1, EColors.chalk);
			}
		}
		
		if (Envision.isDebugMode() && DebugSettings.drawTileInfo) {
			String tText = "[" + theTile.worldX + "," + theTile.worldY + "] " + this;
			String taText = (tn != null) ? "[" + tn.worldX + "," + tn.worldY + "] " + tn.getName(): "null";
			String tbText = (ts != null) ? "[" + ts.worldX + "," + ts.worldY + "] " + ts.getName(): "null";
			
			RenderingManager.drawString(tText, x, y, 0.7, 0.7, EColors.yellow);
			RenderingManager.drawString(taText, x, y + FontRenderer.FONT_HEIGHT, 0.7, 0.7, EColors.green);
			RenderingManager.drawString(tbText, x, y + FontRenderer.FONT_HEIGHT * 2, 0.7, 0.7, EColors.red);
		}
	}
	
}
