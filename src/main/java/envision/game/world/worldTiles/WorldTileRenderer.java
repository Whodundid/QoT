package envision.game.world.worldTiles;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.game.component.types.RenderingComponent;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.misc.Rotation;

public class WorldTileRenderer extends RenderingComponent {
	
	protected WorldTile theTile;
	
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
		if (!theTile.hasTexture()) return;
		
//		final int halfScreenW = Envision.getWidth() >> 1;
//		final int halfScreenH = Envision.getHeight() >> 1;
//		
//		final double camWorldX = camera.getCameraCenterX();
//		final double camWorldY = camera.getCameraCenterY();
//		
//		//pixel width of each tile
//		final double w = camera.getScaledTileWidth();
//		//pixel height of each tile
//		final double h = camera.getScaledTileHeight();
//		
//		double drawX = (theTile.worldX - camWorldX - 0.5) * w + halfScreenW;
//		double drawY = (theTile.worldY - camWorldY - 0.5) * h + halfScreenH;
		
//		//the left most x pixel for map drawing
//		double x = (int) (midX - (distX * w) - (w / 2));
//		//the top most y pixel for map drawing
//		double y = (int) (midY - (distY * h) - (h / 2));
//		
//		//transform the world coordinates of the tile to screen x/y coordinates
//		double drawX = (theTile.worldX * w) + x;
//		double drawY = (theTile.worldY * h) + y;
//		
//		//translate to the middle drawn world tile
//		drawX += (distX - midDrawX) * w;
//		drawY += (distY - midDrawY) * h;
//		
//		//apply the player's (CAMERA'S) offset to the drawn tile
//		drawX -= camera.getOffsetX();
//		drawY -= camera.getOffsetY();
		
		//calculate the entity's draw width and height based off of actual dims and zoom
		//double drawW = theTile.width * zoom;
		//double drawH = theTile.height * zoom;
		
		//if (BatchManager.isEnabled()) drawTile(world, drawX, drawY, w, h, 0xffffffff, false);
		//else drawTile(world, drawX, drawY, w, h, calcBrightness(theTile.worldX, theTile.worldY), false);
		
		old(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
	}
	
    private void old(IGameWorld world, WorldCamera camera,
        int midDrawX, int midDrawY,
        double midX, double midY,
        int distX, int distY)
    {
        //pixel width of each tile
        final double w = camera.getScaledTileWidth();
        //pixel height of each tile
        final double h = camera.getScaledTileHeight();

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
        
        if (BatchManager.isEnabled()) drawTile(world, drawX, drawY, w, h, 0xffffffff, false);
        else drawTile(world, drawX, drawY, w, h, calcBrightness(theTile.worldX, theTile.worldY), false);
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
			RenderingManager.drawTexture(theTile.tex, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (tb == null || !tb.hasTexture())) {
				RenderingManager.drawTexture(theTile.tex, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
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
				RenderingManager.drawTexture(theTile.tex, x, y - wh, w, h, theTile.drawFlipped, rot, tileBrightness);
				
				GameTexture side = (theTile.sideTex != null) ? theTile.sideTex : theTile.tex;
				
				double yPos = y + h - wh;
				wallBrightness = EColors.changeBrightness(brightness, 145);
				
				//draw wall side slightly below
				RenderingManager.drawTexture(side, x, yPos, w, wh, theTile.drawFlipped, rot, wallBrightness);
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if ((tb == null || !tb.hasTexture())) {
					RenderingManager.drawTexture(theTile.tex, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
			else {
				wh = -wh;
				double yPos = y + wh;
				
				//draw main texture slightly below main location
				RenderingManager.drawTexture(theTile.tex, x, yPos, w, h, theTile.drawFlipped, rot, tileBrightness);
				
				//I don't want to draw if ta is null
				//but
				//I also don't want to draw if ta is a wall and has the same wall height as this one
				
				if (ta != null && (!ta.isWall || ((h * ta.wallHeight) != -wh))) {
					GameTexture side = (theTile.sideTex != null) ? theTile.sideTex : theTile.tex;
					
					wallBrightness = EColors.changeBrightness(brightness, 145);
					side = (ta.sideTex != null) ? ta.sideTex : ta.tex;
					
					double sideWallY = yPos - wh;
					
					//THIS IS NOT QUITE RIGHT -- the yPos needs to take into account whether or
					//not the tile above is a wall and if so what height the wall is at and then
					//size the wh accordingly to fit the area in between the ta's end wh and this
					//tiles yPos
					
					//if (ta.isWall) {
					//	if (ta.wallHeight < 0)
					//}
					
					//draw wall side slightly above
					RenderingManager.drawTexture(side, x, sideWallY, w, wh, theTile.drawFlipped, rot, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if (tb == null || !tb.hasTexture()) {
					RenderingManager.drawTexture(theTile.tex, x, yPos + h, w, (h / 2) - wh, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
		}
		else {
			RenderingManager.drawTexture(theTile.tex, x, y, w, h, theTile.drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if (!DebugSettings.drawFlatWalls && (tb == null || !tb.hasTexture())) {
				var side = (theTile.sideTex != null) ? theTile.sideTex : theTile.tex;
				RenderingManager.drawTexture(side, x, y + h, w, h / 2, theTile.drawFlipped, rot, EColors.changeBrightness(brightness, 145));
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
