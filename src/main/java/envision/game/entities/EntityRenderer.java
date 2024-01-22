package envision.game.entities;

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
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.misc.Rotation;
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
	
	protected int flashColor;
	protected long flashStart;
	protected long flashDurration;
	protected boolean drawFlash = false;
	
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
    @Override
	public void draw(IGameWorld world, WorldCamera camera) {
	    final double zoom = camera.getZoom();
        final double[] draw = camera.calculateDrawDimensions(theEntity);
        boolean mouseOver = camera.isMouseOverObject(theEntity);

        int worldBrightness = world.getAmbientLightLevel();
        int color = EColors.white.brightness(worldBrightness);
        // check if camera should make the entity transparent if the camera target is behind it
        color = checkIfCameraTargetIsBehind(camera, color);
        
        var collisionBox = theObject.collisionBox;        
        
        
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
            
            drawEntity(world, camera, draw, calcBrightness(lightx, lighty), mouseOver);
        }
        else {
            drawEntity(world, camera, draw, color, mouseOver);
        }
        
        if (DebugSettings.drawEntityHitboxes) {
            RenderingManager.drawHRectDimsArray(draw, 1, EColors.blue);
        }
        
        if (DebugSettings.drawEntityCollisionBoxes) {
            double colSX = draw[0] + (collisionBox.startX * zoom);
            double colSY = draw[1] + (collisionBox.startY * zoom);
            double colEX = colSX + (collisionBox.width * zoom);
            double colEY = colSY + (collisionBox.height * zoom);
            RenderingManager.drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
        }
        
        if (DebugSettings.drawFocusedEntityAxis && camera.getFocusedObject() == theEntity) {
            final double halfScreenW = camera.getDrawableAreaHalfWidth();
            final double halfScreenH = camera.getDrawableAreaHalfHeight();
            double midX = draw[0] + draw[2] * 0.5;
            double midY = draw[1] + draw[3] * 0.5;
            double diffX = midX - halfScreenW;
            double diffY = midY - halfScreenH;
            double xPos = halfScreenW + diffX;
            double yPos = halfScreenH + diffY;
            
            RenderingManager.drawRect(xPos, 0, xPos + 1, camera.getDrawableAreaHeight(), EColors.green);
            RenderingManager.drawRect(0, yPos, camera.getDrawableAreaWidth(), yPos + 1, EColors.green);
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
	
	public void drawEntity(IGameWorld world, WorldCamera camera, double[] dims, int brightness, boolean mouseOver) {
	    double x = dims[0];
	    double y = dims[1];
	    double w = dims[2];
	    double h = dims[3];
	    
	    lastDrawX = x;
		lastDrawY = y;
		lastDrawW = w;
		lastDrawH = h;
		lastDrawBrightness = brightness;
		lastDrawMouseOver = mouseOver;
		
		drawFlash(brightness);
		
		if (theObject.getSprite() != null) drawEntityTexture();
		theEntity.draw(camera, dims, mouseOver);
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
		
		// hide upper layers if under roof
		checkIfCameraIsUnderRoof(world, camera);
		
		// draw chat box (if there is one)
		drawChatbox(x, y, w, h);
        
		// draw head text
		RenderingManager.drawStringC(theEntity.headText, x + w * 0.5, y - h * 0.25);
	}
	
	public void checkIfCameraIsUnderRoof(IGameWorld world, WorldCamera camera) {
	    if (theEntity != camera.getFocusedObject()) return;
	    
	    int camLayer = theEntity.getCameraLayer();
	    if (camLayer >= world.getNumberOfLayers() - 1) return;
	    
	    var colDims = theEntity.getCollisionDims();
        int worldMidX = (int) (colDims.midX / world.getTileWidth());
        int worldMidY = (int) (colDims.midY / world.getTileHeight());
        WorldTile above = null;
        if (worldMidX >= 0 && worldMidX < world.getWidth() && worldMidY >= 0 && worldMidY < world.getHeight()) {
            while ((above == null || above == VoidTile.instance) && camLayer < world.getNumberOfLayers() - 1) {
                above = world.getTileAt(camLayer + 1, worldMidX, worldMidY);
                camLayer++;
            }
        }
        
        int upper = world.getNumberOfLayers() - 1;
        
        if (above != null && above != VoidTile.instance) {
            upper = above.getCameraLayer() - 1;
        }
        
        camera.setUpperCameraLayer(upper);
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
        
        //int ratio = 255 - ((255 * (world.getDayLength() / 2 - world.getTime()) / (world.getDayLength() / 2)));
        int ratio = 220;
        ratio = ENumUtil.clamp(ratio, 100, 255);
        int textRatio = ENumUtil.clamp(ratio, 220, 255);
        
        final var camEntity = Envision.levelManager.getCamera().getFocusedObject();
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
	
	public void drawFlash(int brightness) {
	    if (!drawFlash) return;
	    
	    if (System.currentTimeMillis() - flashStart >= flashDurration) {
	        drawFlash = false;
	    }
	    
	    int color = flashColor;
	    color = EColors.changeBrightness(color, theEntity.world.getAmbientLightLevel());
	    lastDrawBrightness = color;
	}
	
	public void drawEntityTexture() {
	    boolean flip = false;
	    
	    if (flipTextureWhenMoving) {
	        flip = theObject.facing == Rotation.RIGHT || theObject.facing == Rotation.DOWN;
	    }
	    
	    Rotation rot = (theObject.forcedRotation != null) ? theObject.forcedRotation : Rotation.UP;
	    flip = (theObject.forceFlip) ? true : flip;
	    
	    RenderingManager.drawSprite(theObject.sprite, lastDrawX, lastDrawY, lastDrawW, lastDrawH, flip, rot, lastDrawBrightness);
	}
	
	public EntityRenderer setFlipTextureWhenMoving(boolean val) { flipTextureWhenMoving = val; return this; }
	
	
	public void flashColor(EColors color, long durration) {
	    flashColor(color.intVal, durration);
	}
	public void flashColor(int color, long durration) {
	    flashColor = color;
	    flashDurration = durration;
	    flashStart = System.currentTimeMillis();
	    drawFlash = true;
	}
	
}
