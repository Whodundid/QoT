package envision.game.entities;

import java.text.DecimalFormat;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.game.GameObject;
import envision.game.component.types.RenderingComponent;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.misc.Rotation;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class EntityRenderer extends RenderingComponent {
	
	protected Entity theEntity;
	
	// Transparency
	
	protected boolean makeTransparentIfInFront = false;
	/** True if this entity is current blocking the focused camera entity and will be drawn transparent. */
	protected boolean isTransparent;
	protected boolean fadeBack;
	/** the tick that transparency fading started on. */
	protected long transparencyStartTick;
	/** 60 ticks for 1 second. */
	protected long transparencyTransitionTickDuration = 10;
	
	protected boolean flipTextureWhenMoving = true;
	
	//==============
	// Constructors
	//==============
	
    public EntityRenderer(Entity entityIn) {
        super(entityIn);
        theEntity = entityIn;
    }
    
    public EntityRenderer(Entity entityIn, boolean drawTransparentIfBlockingCamera) {
        super(entityIn);
        theEntity = entityIn;
        makeTransparentIfInFront = drawTransparentIfBlockingCamera;
    }
    
	//=========
	// Methods
	//=========
	
	/**
	 * Calculates screen drawing positions up front so that they can be
	 * extracted out from the actual entity drawing logic.
	 */
	public void draw(IGameWorld world, WorldCamera camera,
					 int midDrawX, int midDrawY,
					 double midX, double midY,
					 int distX, int distY)
	{
		var sprite = theObject.getSprite();
		//if (sprite == null && EStringUtil.isNotPopulated(theEntity.getHeadText())) return;
		
		final double zoom = camera.getZoom();
		lastZoom = zoom;
		
		final int worldX = theObject.worldX;
        final int worldY = theObject.worldY;
		
        final int screenWidth = Envision.getWidth();
        final int screenHeight = Envision.getHeight();
        
        final double camWorldX = camera.getCameraCenterX();
        final double camWorldY = camera.getCameraCenterY();
        
        //scaled pixel width of the entity
        double drawW = theObject.width * zoom;
        //scaled pixel height of the entity
        double drawH = theObject.height * zoom;
        
        final var df = new DecimalFormat("#.#####");
        var sb = new EStringBuilder();
        sb.a("xy[", df.format(theObject.startX), ", ", df.format(theObject.startY), "] | ");
        sb.a("wh[", df.format(theObject.width), ", ", df.format(theObject.height), "] | ");
//        sb.a("cwxy[", df.format(worldX), ", ", df.format(worldY), "] | ");
//        sb.a("cwxy[", df.format(camWorldX), ", ", df.format(camWorldY), "] | ");
//        sb.a("dwh[", df.format(drawW), ", ", df.format(drawH), "] | ");
//        sb.a(df.format((worldX - camWorldX) * drawW + screenWidth / 2));
        //System.out.println(sb);
//      
        double drawX, drawY;
        
        if (DebugSettings.fixingCamera) {
//            drawX = ((camWorldX - worldX) * drawW) + screenWidth / 2.0;
//            drawY = ((worldY - camWorldY) * drawH) + screenHeight / 2.0;
//            
//            drawX -= camera.getScaledTileWidth() * 0.5;
//            drawY -= camera.getScaledTileHeight() * 0.5;
            
            old(world, camera);
            return;
        }
        else {
            //pixel width of each tile
            double w = (int) (world.getTileWidth() * zoom);
            //pixel height of each tile
            double h = (int) (world.getTileHeight() * zoom);
            
            //the left most x coordinate for map drawing
            double x = (int) (midX - (distX * w) - (w / 2));
            //the top most y coordinate for map drawing
            double y = (int) (midY - (distY * h) - (h / 2));
            
            var startX = theObject.startX;
            var startY = theObject.startY;
            
            double entityOffsetX = (startX % world.getTileWidth()) * zoom;
            double entityOffsetY = (startY % world.getTileWidth()) * zoom;
            
            //transform the world coordinates of the entity to screen x/y coordinates
            drawX = (worldX * w) + x;
            drawY = (worldY * h) + y;
            
            //translate to the middle drawn world tile
            drawX += (distX - midDrawX) * w;
            drawY += (distY - midDrawY) * h;
            
            //System.out.println(entityOffsetX + " : " + camera.getOffsetX());
            
            drawX -= camera.getOffsetX();
            drawY -= camera.getOffsetY();
            //double tileX = drawX;
            //double tileY = drawY;
            
            drawX += entityOffsetX;
            drawY += entityOffsetY;
        }
        
        
        
		

		
		var width = theObject.width;
		var height = theObject.height;
		
		//calculate the entity's draw width and height based off of actual dims and zoom
		drawW = width * zoom;
		drawH = height * zoom;
		
		var collisionBox = theObject.collisionBox;
		
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		boolean mouseOver = (mX >= drawX && mX <= drawX + drawW && mY >= drawY && mY <= drawY + drawH);
		
        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        
        // check if camera should make the entity transparent if the camera target is behind it
        color = checkIfCameraTargetIsBehind(camera, color);
		
		if (!BatchManager.isEnabled()) {
			double cmx = collisionBox.midX; //collision mid x
			double cmy = collisionBox.midY; //collision mid y
			double tw = world.getTileWidth(); //tile width
			double th = world.getTileHeight(); //tile height
			
			//offset world coordinates to middle of collision box
			int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			//light render position
			int lightx = worldX + mwcx;
			int lighty = worldY + mwcy;
			
			drawEntity(world, camera, drawX, drawY, drawW, drawH, calcBrightness(lightx, lighty), mouseOver);
		}
		else {
			drawEntity(world, camera, drawX, drawY, drawW, drawH, color, mouseOver);
		}
		
		if (DebugSettings.drawEntityHitboxes) {
		    RenderingManager.drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
		}
		
        if (DebugSettings.drawEntityCollisionBoxes) {
            double colSX = drawX + (collisionBox.startX * zoom);
            double colSY = drawY + (collisionBox.startY * zoom);
            double colEX = colSX + (collisionBox.width * zoom);
            double colEY = colSY + (collisionBox.height * zoom);
            RenderingManager.drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
        }
	}
	
	protected void old(IGameWorld world, WorldCamera camera) {
	    final int screenW = Envision.getWidth();
	    final int screenH = Envision.getHeight();
        final int halfScreenW = screenW >> 1;
        final int halfScreenH = screenH >> 1;
        
        final double camWorldX = camera.getCameraCenterX();
        final double camWorldY = camera.getCameraCenterY();
        
        final double zoom = camera.getZoom();
        
        //pixel width of each tile
        final double w = camera.getScaledTileWidth();
        //pixel height of each tile
        final double h = camera.getScaledTileHeight();
        
        double entityOffsetX = (theEntity.startX % world.getTileWidth()) * zoom;
        double entityOffsetY = (theEntity.startY % world.getTileWidth()) * zoom;
        
        //              world coordinates                   pixel coords        screen cords
        double drawX = (theEntity.worldX - camWorldX - 0.5) * w + entityOffsetX + halfScreenW;
        double drawY = (theEntity.worldY - camWorldY - 0.5) * h + entityOffsetY + halfScreenH;
        
        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        
        double width = theObject.width;
        double height = theObject.height;
        
        //calculate the entity's draw width and height based off of actual dims and zoom
        double drawW = width * zoom;
        double drawH = height * zoom;
        
        var collisionBox = theObject.collisionBox;
        
        int mX = Mouse.getMx();
        int mY = Mouse.getMy();
        boolean mouseOver = (mX >= drawX && mX <= drawX + drawW && mY >= drawY && mY <= drawY + drawH);
        
        // check if camera should make the entity transparent if the camera target is behind it
        color = checkIfCameraTargetIsBehind(camera, color);
        
        if (!BatchManager.isEnabled()) {
            double cmx = collisionBox.midX; //collision mid x
            double cmy = collisionBox.midY; //collision mid y
            double tw = world.getTileWidth(); //tile width
            double th = world.getTileHeight(); //tile height
            
            //offset world coordinates to middle of collision box
            int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
            int mwcy = (int) Math.floor(cmy / th); //mid world coords y
            
            //light render position
            int lightx = theEntity.worldX + mwcx;
            int lighty = theEntity.worldY + mwcy;
            
            drawEntity(world, camera, drawX, drawY, drawW, drawH, calcBrightness(lightx, lighty), mouseOver);
        }
        else {
            drawEntity(world, camera, drawX, drawY, drawW, drawH, color, mouseOver);
        }
        
        if (DebugSettings.drawEntityHitboxes) {
            RenderingManager.drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
        }
        
        if (DebugSettings.drawEntityCollisionBoxes) {
            double colSX = drawX + (collisionBox.startX * zoom);
            double colSY = drawY + (collisionBox.startY * zoom);
            double colEX = colSX + (collisionBox.width * zoom);
            double colEY = colSY + (collisionBox.height * zoom);
            RenderingManager.drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
        }
        
        if (DebugSettings.drawFocusedEntityAxis && camera.getFocusedObject() == theEntity) {
            double midX = drawX + drawW * 0.5;
            double midY = drawY + drawH * 0.5;
            double diffX = midX - halfScreenW;
            double diffY = midY - halfScreenH;
            double xPos = halfScreenW + diffX;
            double yPos = halfScreenH + diffY;
            
            RenderingManager.drawRect(xPos, 0, xPos + 1, screenH, EColors.green);
            RenderingManager.drawRect(0, yPos, screenW, yPos + 1, EColors.green);
        }
	}
	
	protected int checkIfCameraTargetIsBehind(WorldCamera camera, int color) {
	    if (!makeTransparentIfInFront) return color;
	    
        GameObject target = camera.getFocusedObject();
        if (target == null) return color;
        if (target == theEntity) return color;
        
        var targetDims = target.getCollisionDims();
        var dims = theEntity.getDimensions();
        boolean contains = dims.partiallyContains(targetDims);
        
        double tsp = target.getSortPoint();
        double esp = theEntity.getSortPoint();
        boolean blocking = Double.compare(tsp, esp) < 0;
        
        final int low = 100; // the lowest opacity point (out of 255)
        
        boolean care = contains && blocking;
        long ticks = Envision.getRunningTicks();
        int dur = (int) transparencyTransitionTickDuration;
        int start = (int) transparencyStartTick;
        int deltaO = 255 - low; // opacity low/high delta
        
        if (care) {
            if (fadeBack) fadeBack = false;
            
            if (!isTransparent) {
                isTransparent = true;
                transparencyStartTick = ticks;
            }
            
            int ratio = 100;
            
            if (ticks - start < dur) {
                ratio = (int) (low + deltaO - ((ticks - start) * deltaO) / dur);
            }
            
            return EColors.changeOpacity(color, ratio);
        }
        else if (isTransparent) {
            if (!fadeBack) {
                fadeBack = true;
                transparencyStartTick = ticks;
                start = (int) transparencyStartTick;
            }
            
            int ratio = 255;
            
            if (ticks - start < dur) {
                ratio = (int) (low + ((ticks - start) * deltaO) / dur);
            }
            else {
                isTransparent = false;
                fadeBack = false;
            }
            
            return EColors.changeOpacity(color, ratio);
        }
        
        return color;
	}
	
	public void drawEntity(IGameWorld world, WorldCamera camera, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		lastDrawX = x;
		lastDrawY = y;
		lastDrawW = w;
		lastDrawH = h;
		lastDrawBrightness = brightness;
		lastDrawMouseOver = mouseOver;
		
		if (theObject.getSprite() != null) drawEntityTexture();
		theEntity.draw(camera, x, y, w, h, mouseOver);
		theEntity.healthBar.setDimensions(x, y - 7, w, 7);
		theEntity.healthBar.drawObject(0, Mouse.getMx(), Mouse.getMy());
		
		var recentlyAttacked = theEntity.recentlyAttacked;
		var healthChanged = theEntity.healthChanged;
		var invincible = theEntity.invincible;
		var health = theEntity.health;
		var maxHealth = theEntity.maxHealth;
		
		if ((recentlyAttacked || healthChanged) && !invincible && (health < maxHealth)) {
			theEntity.healthBar.keepDrawing();
		}
		
		// draw chat box (if there is one)
		drawChatbox(x, y, w, h);
        
		// draw head text
		RenderingManager.drawStringC(theEntity.headText, x + w * 0.5, y - h * 0.25);
	}
	
	public void drawChatbox(double x, double y, double w, double h) {
	    if (EStringUtil.isNotPopulated(theEntity.activeChat)) return;
	    
	    final String chat = theEntity.activeChat;
	    final var world = theEntity.world;
	    double chatWidth = FontRenderer.strWidth(chat);
	    
	    double midX = (x + w * 0.5);
	    double dx = midX - (chatWidth * 0.6);
        double dy = y - h * 0.10;
        double dw = (chatWidth * 1.2);
        double dh = 40;
        
        int ratio = 255 - ((255 * (world.getDayLength() / 2 - world.getTime()) / (world.getDayLength() / 2)));
        ratio = ENumUtil.clamp(ratio, 100, 255);
        int textRatio = ENumUtil.clamp(ratio, 220, 255);
        
        final var camEntity = Envision.theWorld.getCamera().getFocusedObject();
        if (camEntity != null) {
            final double tw = world.getTileWidth();
            double distToPlayer = world.getDistance(theEntity, camEntity);
            if (distToPlayer > (12 * tw)) return;
            
            int distRatio = (int) (255 - ((255 * distToPlayer) / (4 * tw)) + (10 * tw));
            distRatio = ENumUtil.clamp(distRatio, 0, 255);
            ratio = ENumUtil.clamp(ratio, 0, distRatio);
            textRatio = ENumUtil.clamp(ratio, 0, distRatio);
        }
        
        var blackBr = EColors.white.brightnessOpacity(ratio - 30, ratio);
        var backBr = EColors.dsteel.brightnessOpacity(ratio + 30, ratio);
        var textBr = EColors.white.opacity(textRatio);
	    
	    RenderingManager.drawHRect(dx, dy, dx + dw, dy + dh, 2, blackBr);
        RenderingManager.drawRect(dx + 1, dy + 1, dx + dw - 1, dy + dh - 1, backBr);
        RenderingManager.drawStringC(chat, midX, dy + dh / 2 - FontRenderer.HALF_FH + 2, textBr);
	}
	
	public void drawEntityTexture() {
	    boolean flip = false;
	    
	    if (flipTextureWhenMoving) {
	        flip = theObject.facing == Rotation.RIGHT || theObject.facing == Rotation.DOWN;
	    }
	    
		RenderingManager.drawSprite(theObject.sprite, lastDrawX, lastDrawY, lastDrawW, lastDrawH, flip, lastDrawBrightness);
	}
	
	public EntityRenderer setFlipTextureWhenMoving(boolean val) { flipTextureWhenMoving = val; return this; }
	
}
