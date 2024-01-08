package envision.game.world.worldTiles;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.resourceLoaders.Sprite;
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
	public void draw(IGameWorld world, WorldCamera camera,
		 			 int midDrawX, int midDrawY,
		 			 double midX, double midY,
		 			 int distX, int distY)
	{
		//ignore if there is no texture
		if (!theTile.hasSprite()) return;
		
		if (DebugSettings.fixingCamera) {
		    newRenderingMethod(world, camera);
		    return;
		}
		
        //pixel width of each tile
        final double w = world.getTileWidth() * camera.getZoom();
        //pixel height of each tile
        final double h = world.getTileHeight() * camera.getZoom();

        //the left most x pixel for map drawing
        double x = (int) (midX - (distX * w) - (w * 0.5));
        //the top most y pixel for map drawing
        double y = (int) (midY - (distY * h) - (h * 0.5));
        
        //transform the world coordinates of the tile to screen x/y coordinates
        double drawX = (theTile.worldX * w) + x;
        double drawY = (theTile.worldY * h) + y;
        
        //translate to the middle drawn world tile
        drawX += (distX - midDrawX) * w;
        drawY += (distY - midDrawY) * h;
        
        //apply the player's (CAMERA'S) offset to the drawn tile
        drawX -= camera.getOffsetX();
        drawY -= camera.getOffsetY();
        
        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        
        if (BatchManager.isEnabled()) drawTile(world, drawX, drawY, w, h, color, false);
        else drawTile(world, drawX, drawY, w, h, calcBrightness(theTile.worldX, theTile.worldY), false);
	}
	
	/** This is actually newer, but still need to work out kinks. */
    public void newRenderingMethod(IGameWorld world, WorldCamera camera)
    {
        double[] draw = camera.calculateDrawDimensions(theTile);
        
        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        
        if (BatchManager.isEnabled()) drawTile(world, draw[0], draw[1], draw[2], draw[3], color, false);
        else drawTile(world, draw[0], draw[1], draw[2], draw[3], calcBrightness(theTile.worldX, theTile.worldY), false);
    }
	
	//-----------------------------------------------------------------------------------------------------------
	
	public void drawTile(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		double wh = h * theTile.wallHeight; //wh == 'wallHeight'
		
		WorldTile tb = null; // tb == 'tileBelow'
		WorldTile ta = null; // ta == 'tileAbove'
		
		Rotation rot = (theTile.rotation != null) ? theTile.rotation : Rotation.UP;
		
		if ((theTile.worldY - 1) >= 0) ta = world.getTileAt(theTile.worldX, theTile.worldY - 1);
		if ((theTile.worldY + 1) < world.getHeight()) tb = world.getTileAt(theTile.worldX, theTile.worldY + 1);
		
		if (theTile.isWall && DebugSettings.drawFlatWalls) {
			RenderingManager.drawSprite(theTile.sprite, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (tb == null || !tb.hasSprite())) {
				RenderingManager.drawSprite(theTile.sprite, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
		else if (theTile.isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			
			//check if the tile directly above is a wall
			//if so - don't draw wall side
			if (wh >= 0) {
				//draw main texture slightly above main location
				RenderingManager.drawSprite(theTile.sprite, x, y - wh, w, h, theTile.drawFlipped, rot, tileBrightness);
				
				Sprite side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
				
				double yPos = y + h - wh;
				wallBrightness = EColors.changeBrightness(brightness, 145);
				
				//draw wall side slightly below
				RenderingManager.drawSprite(side, x, yPos, w, wh, theTile.drawFlipped, rot, wallBrightness);
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if ((tb == null || !tb.hasSprite())) {
					RenderingManager.drawSprite(theTile.sprite, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
			else {
				wh = -wh;
				double yPos = y + wh;
				
				//draw main texture slightly below main location
				RenderingManager.drawSprite(theTile.sprite, x, yPos, w, h, theTile.drawFlipped, rot, tileBrightness);
				
				//I don't want to draw if ta is null
				//but
				//I also don't want to draw if ta is a wall and has the same wall height as this one
				
				if (ta != null && (!ta.isWall || ((h * ta.wallHeight) != -wh))) {
					Sprite side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
					
					wallBrightness = EColors.changeBrightness(brightness, 145);
					side = (ta.sideTex != null) ? ta.sideTex : ta.sprite;
					
					double sideWallY = yPos - wh;
					
					//THIS IS NOT QUITE RIGHT -- the yPos needs to take into account whether or
					//not the tile above is a wall and if so what height the wall is at and then
					//size the wh accordingly to fit the area in between the ta's end wh and this
					//tiles yPos
					
					//if (ta.isWall) {
					//	if (ta.wallHeight < 0)
					//}
					
					//draw wall side slightly above
					RenderingManager.drawSprite(side, x, sideWallY, w, wh, theTile.drawFlipped, rot, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if (tb == null || !tb.hasSprite()) {
					RenderingManager.drawSprite(theTile.sprite, x, yPos + h, w, (h / 2) - wh, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
		}
		else {
			RenderingManager.drawSprite(theTile.sprite, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (tb == null || !tb.hasSprite())) {
				var side = (theTile.sideTex != null) ? theTile.sideTex : theTile.sprite;
				RenderingManager.drawSprite(side, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
		
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
			String taText = (ta != null) ? "[" + ta.worldX + "," + ta.worldY + "] " + ta.getName(): "null";
			String tbText = (tb != null) ? "[" + tb.worldX + "," + tb.worldY + "] " + tb.getName(): "null";
			
			RenderingManager.drawString(tText, x, y, 0.7, 0.7, EColors.yellow);
			RenderingManager.drawString(taText, x, y + FontRenderer.FONT_HEIGHT, 0.7, 0.7, EColors.green);
			RenderingManager.drawString(tbText, x, y + FontRenderer.FONT_HEIGHT * 2, 0.7, 0.7, EColors.red);
		}
	}
	
}
