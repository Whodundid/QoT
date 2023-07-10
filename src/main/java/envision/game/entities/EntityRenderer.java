package envision.game.entities;

import java.text.DecimalFormat;

import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.game.component.types.RenderingComponent;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.misc.Rotation;
import eutil.strings.EStringBuilder;

public class EntityRenderer extends RenderingComponent {
	
	protected Entity theEntity;
	
	//==============
	// Constructors
	//==============
	
	public EntityRenderer(Entity entityIn) {
		super(entityIn);
		theEntity = entityIn;
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
		var tex = theObject.getTexture();
		if (tex == null) return;
		
//		final double zoom = camera.getZoom();
//		
//		final int worldX = theObject.worldX;
//        final int worldY = theObject.worldY;
//		
//        final int screenWidth = Envision.getWidth();
//        final int screenHeight = Envision.getHeight();
//        
//        final double camWorldX = camera.getCameraCenterX();
//        final double camWorldY = camera.getCameraCenterY();
//        
//        //scaled pixel width of the entity
//        final double drawW = theObject.width * zoom;
//        //scaled pixel height of the entity
//        final double drawH = theObject.height * zoom;
//        
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
//        double drawX = ((camWorldX - worldX) * drawW) + screenWidth / 2;
//        double drawY = ((worldY - camWorldY) * drawH) + screenHeight / 2;
        
        //drawX -= camera.getScaledTileWidth() * 0.5;
        //drawY -= camera.getScaledTileHeight() * 0.5;
		
		double zoom = camera.getZoom();
		
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
		
		var worldX = theObject.worldX;
		var worldY = theObject.worldY;
		
		//transform the world coordinates of the entity to screen x/y coordinates
		double drawX = (worldX * w) + x;
		double drawY = (worldY * h) + y;
		
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
		
		var width = theObject.width;
		var height = theObject.height;
		
		//calculate the entity's draw width and height based off of actual dims and zoom
		double drawW = width * zoom;
		double drawH = height * zoom;
		
		var collisionBox = theObject.collisionBox;
		
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		boolean mouseOver = (mX >= drawX && mX <= drawX + drawW && mY >= drawY && mY <= drawY + drawH);
		
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
			
			drawEntity(world, drawX, drawY, drawW, drawH, calcBrightness(lightx, lighty), mouseOver);
		}
		else {
			drawEntity(world, drawX, drawY, drawW, drawH, 0xffffffff, mouseOver);
		}
	}
	
	public void drawEntity(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		lastDrawX = x;
		lastDrawY = y;
		lastDrawW = w;
		lastDrawH = h;
		lastDrawBrightness = brightness;
		lastDrawMouseOver = mouseOver;
		
		drawEntityTexture();
		theEntity.healthBar.setDimensions(x, y - 7, w, 7);
		theEntity.healthBar.drawObject(Mouse.getMx(), Mouse.getMy());
		
		var recentlyAttacked = theEntity.recentlyAttacked;
		var healthChanged = theEntity.healthChanged;
		var invincible = theEntity.invincible;
		var health = theEntity.health;
		var maxHealth = theEntity.maxHealth;
		
		if ((recentlyAttacked || healthChanged) && !invincible && (health < maxHealth)) {
			theEntity.healthBar.keepDrawing();
		}
		
		//headText = startX + " : " + startY;
		RenderingManager.drawStringC(theEntity.headText, x + w/2, y - h/4);
	}
	
	public void drawEntityTexture() {
		boolean flip = theObject.facing == Rotation.RIGHT || theObject.facing == Rotation.DOWN;
		
		RenderingManager.drawTexture(theObject.tex, lastDrawX, lastDrawY, lastDrawW, lastDrawH, flip, lastDrawBrightness);
	}
	
}
