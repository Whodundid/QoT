package envisionEngine.gameEngine.world.worldTiles;

import envisionEngine.debug.DebugSettings;
import envisionEngine.gameEngine.GameObject;
import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import envisionEngine.renderEngine.fontRenderer.FontRenderer;
import envisionEngine.renderEngine.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.misc.Rotation;
import qot.QoT;

public abstract class WorldTile extends GameObject implements Comparable<WorldTile> {
	
	//--------
	// Fields
	//--------
	
	/**
	 * The id of this tile which is primarily used for saving/loading world data.
	 */
	protected TileIDs id;
	
	/**
	 * The human-readable name of this tile.
	 */
	protected String name;
	
	/**
	 * The primary texture of this tile.
	 */
	//protected GameTexture tex;
	
	/**
	 * If this tile is a wall, this is the texture that is drawn either above or
	 * below the primary texture in order to give additional depth to the terrain.
	 */
	protected GameTexture sideTex;
	
	/**
	 * For tiles that can have multiple variations, I.E. Grass, this number keeps
	 * track of the total number of variations there are.
	 */
	protected int numVariants = 1;
	
	/**
	 * True if this tile should prevent entities from being able to enter the
	 * boundaries of this tile.
	 */
	protected boolean blocksMovement = false;
	
	/**
	 * Used to denote whether or not this tile's texture can be randomly swapped for
	 * any of its provided variants. For instance, Grass can randomly decide which
	 * texture it will display based on its provided number of variants.
	 */
	protected boolean wildCardTexture = false;
	
	/**
	 * True if this tile has additional vertical depth.
	 * <p>
	 * Note: Wall tiles do not inherently block movement as that modifier must be
	 * specified separately.
	 */
	protected boolean isWall = false;
	
	/**
	 * Represents the physical height of this tile raised above the standard terrain
	 * level of zero (0.0).
	 * 
	 * <li> Note: This value is ignored unless 'isWall' is true
	 * <li> Note: Standard height ranges from [0.0,1.0]
	 */
	protected double wallHeight = 0.5;
	
	/**
	 * A tile's material is used to determine a variety of things ranging from:
	 * 
	 * <ul>
	 * 		<li> Walking sound
	 * 		<li> Active modifiers such as:
	 * 		<ul>
	 * 			<li> Entity movement speed
	 * 			<li> General area effects
	 * 			<li> Passive status effects (I.E. being wet)
	 * 		</ul>
	 * </ul>
	 */
	protected TileMaterial material = TileMaterial.VOID;
	
	/**
	 * The physical x and y coordinate position within the world.
	 * <p>
	 * Note: this does NOT represent screen coordinates.
	 */
	//protected int worldX, worldY;
	
	protected boolean hasSideBrightness = false;
	protected int sideBrightness = 255;
	
	protected EArrayList<Entity> entitiesOnTile = new EArrayList(10);
	protected EArrayList<Entity> entitiesAdding = new EArrayList(10);
	protected EArrayList<Entity> entitiesRemoving = new EArrayList(10);
	
	/** This tile's rotation. */
	protected Rotation rotation;
	/** Whether or not the tile will draw flipped. */
	protected boolean drawFlipped = false;
	
	/** 0 by default -- really dark. */
	protected int lightLevel = 0;
	
	/** If true, light cannot move through. */
	protected boolean blocksLight = true;
	
	/** If true, light level will primarily be based off of the time of day. */
	protected boolean isOutside = true;
	
	protected int miniMapColor = 0xff000000;
	
	//--------------
	// Constructors
	//--------------
	
	protected WorldTile(TileIDs idIn) {
		id = idIn;
		name = id.name;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return id + "";
	}
	
	@Override
	public int compareTo(WorldTile in) {
		return Integer.compare(id.tileID, in.id.tileID);
	}
	
	@Override
	public int getInternalSaveID() {
		return id.tileID;
	}
	
	@Override
	public double getSortPoint() {
		return (worldY + 1) * QoT.theWorld.getTileHeight();
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Called every time the world updates.
	 */
	public void onWorldTick() {}
	
	/**
	 * Called whenever a specific entity click on this tile.
	 * 
	 * @param entity The entity performing the action
	 * @param button The mouse button clicking
	 */
	public void onTileClicked(Entity entity, int button) {}
	
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
	@SuppressWarnings("unused")
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		//ignore if there is no texture
		if (!hasTexture()) return;
		
		double zoom = camera.getZoom();
		
		//pixel width of each tile
		double w = (int) (world.getTileWidth() * zoom);
		//pixel height of each tile
		double h = (int) (world.getTileHeight() * zoom);
		
		//the left most x coordinate for map drawing
		double x = (int) (midX - (distX * w) - (w / 2));
		//the top most y coordinate for map drawing
		double y = (int) (midY - (distY * h) - (h / 2));
		
		double tileOffsetX = (startX % world.getTileWidth()) * zoom;
		double tileOffsetY = (startY % world.getTileWidth()) * zoom;
		
		//transform the world coordinates of the tile to screen x/y coordinates
		double drawX = (worldX * w) + x;
		double drawY = (worldY * h) + y;
		
		//translate to the middle drawn world tile
		drawX += (distX - midDrawX) * w;
		drawY += (distY - midDrawY) * h;
		
		drawX += tileOffsetX;
		drawY += tileOffsetY;
		drawX -= camera.getOffsetX();
		drawY -= camera.getOffsetY();
		
		//calculate the entity's draw width and height based off of actual dims and zoom
		double drawW = width * zoom;
		double drawH = height * zoom;
		
		//apply the player's (CAMERA'S) offset to the drawn tile
		//dX -= offsetX;
		//dY -= offsetY;
		
		drawTile(world, drawX, drawY, w, h, calcBrightness(worldX, worldY), false);
	}
	
	public void drawTile(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		double wh = h * wallHeight; //wh == 'wallHeight'
		
		WorldTile tb = null; // tb == 'tileBelow'
		WorldTile ta = null; // ta == 'tileAbove'
		
		Rotation rot = (rotation != null) ? rotation : Rotation.UP;
		
		if ((worldY - 1) >= 0) ta = world.getTileAt(worldX, worldY - 1);
		if ((worldY + 1) < world.getHeight()) tb = world.getTileAt(worldX, worldY + 1);
		
		if (isWall && DebugSettings.drawFlatWalls) {
			drawTexture(tex, x, y, w, h, drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if ((tb == null || !tb.hasTexture())) {
				drawTexture(tex, x, y + h, w, h / 2, drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
		else if (isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			
			//check if the tile directly above is a wall
			//if so - don't draw wall side
			if (wh >= 0) {
				//draw main texture slightly above main location
				drawTexture(tex, x, y - wh, w, h, drawFlipped, rot, tileBrightness);
				
				GameTexture side = (sideTex != null) ? sideTex : tex;
				
				double yPos = y + h - wh;
				wallBrightness = EColors.changeBrightness(brightness, 145);
				
				//draw wall side slightly below
				drawTexture(side, x, yPos, w, wh, drawFlipped, rot, wallBrightness);
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if ((tb == null || !tb.hasTexture())) {
					drawTexture(tex, x, y + h, w, h / 2, drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
			else {
				wh = -wh;
				double yPos = y + wh;
				
				//draw main texture slightly below main location
				drawTexture(tex, x, yPos, w, h, drawFlipped, rot, tileBrightness);
				
				//I don't want to draw if ta is null
				//but
				//I also don't want to draw if ta is a wall and has the same wall height as this one
				
				if (ta != null && (!ta.isWall || ((h * ta.wallHeight) != -wh))) {
					GameTexture side = (sideTex != null) ? sideTex : tex;
					
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
					drawTexture(side, x, sideWallY, w, wh, drawFlipped, rot, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if (tb == null || !tb.hasTexture()) {
					drawTexture(tex, x, yPos + h, w, (h / 2) - wh, drawFlipped, rot, EColors.changeBrightness(brightness, 145));
				}
			}
		}
		else {
			drawTexture(tex, x, y, w, h, drawFlipped, rot, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if ((tb == null || !tb.hasTexture())) {
				var side = (sideTex != null) ? sideTex : tex;
				drawTexture(side, x, y + h, w, h / 2, drawFlipped, rot, EColors.changeBrightness(brightness, 145));
			}
		}
		
		if (mouseOver) {
			if (isWall) {
				drawHRect(x, y - wh, x + w, y - wh + h, 1, EColors.chalk);
				drawHRect(x, y + h - wh - 1, x + w, y + h, 1, EColors.chalk);
			}
			else {
				drawHRect(x, y, x + w, y + h, 1, EColors.chalk);
			}
		}
		
		if (DebugSettings.drawTileGrid) {
			drawRect(x, y, x + w, y + 1, EColors.vdgray);
			drawRect(x, y, x + 1, y + h, EColors.vdgray);
		}
		
		if (QoT.isDebugMode() && DebugSettings.drawTileInfo) {
			String tText = "[" + worldX + "," + worldY + "] " + this;
			String taText = (ta != null) ? "[" + ta.worldX + "," + ta.worldY + "] " + ta.getName(): "null";
			String tbText = (tb != null) ? "[" + tb.worldX + "," + tb.worldY + "] " + tb.getName(): "null";
			
			drawString(tText, x, y, 0.7, 0.7, EColors.yellow);
			drawString(taText, x, y + FontRenderer.FONT_HEIGHT, 0.7, 0.7, EColors.green);
			drawString(tbText, x, y + FontRenderer.FONT_HEIGHT * 2, 0.7, 0.7, EColors.red);
		}
	}
	
	//---------
	// Getters
	//---------
	
	public boolean hasTexture() { return tex != null; }
	public boolean blocksMovement() { return blocksMovement; }
	public boolean isWildCard() { return wildCardTexture; }
	public boolean isWall() { return isWall; }
	public double getWallHeight() { return wallHeight; }
	
	public int getID() { return id.tileID; }
	public String getName() { return name; }
	public TileMaterial getMaterial() { return material; }
	public int getMapColor() { return miniMapColor; }
	public int getNumVariants() { return numVariants; }
	
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	
	public GameTexture getTexture() { return tex; }
	public GameTexture getSideTexture() { return sideTex; }
	
	public String getAdditionalValues() {
		String r = "";
		
		r += (blocksMovement) ? "true " : "false ";
		r += (material != null) ? material.name : "";
		
		return r;
	}
	
	public boolean isOutside() { return isOutside; }
	public int getLightLevel() { return lightLevel; }
	
	//---------
	// Setters
	//---------
	
	public WorldTile setTexture(GameTexture texIn) { tex = texIn; return this; }
	public WorldTile setSideTexture(GameTexture texIn) { sideTex = texIn; return this; }
	public WorldTile setBlocksMovement(boolean val) { blocksMovement = val; return this; }
	public WorldTile setWall(boolean val) { isWall = val; return this; }
	public WorldTile setWildCard(boolean val) { wildCardTexture = val; return this; }
	
	public void setMiniMapColor(EColors colorIn) { setMiniMapColor(colorIn.intVal); }
	public void setMiniMapColor(int colorIn) { miniMapColor = colorIn; }
	
	public WorldTile setWorldPos(int x, int y) {
		worldX = x;
		worldY = y;
		return this;
	}
	
	public WorldTile setAdditional(String in) {
		if (in != null) {
			String[] values = in.split(" ");
			
			try {
				if (values.length >= 1) blocksMovement = Boolean.valueOf(values[0]);
				if (values.length >= 2) material = TileMaterial.getMaterial(values[1]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public void setOutside(boolean val) { isOutside = val; }
	public void setLightLevel(int level) { lightLevel = level; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static WorldTile getTileFromID(int id) { return getTileFromID(id, 0); }
	public static WorldTile getTileFromID(int id, int texNum) {
		return GlobalTileList.getTileFromID(id, texNum);
	}
	
	public static int getIDFromTile(WorldTile in) {
		return (in != null) ? in.getID() : -1;
	}
	
	public static WorldTile getTileFromName(String nameIn) { return getTileFromArgs(nameIn, null); }
	public static WorldTile getTileFromArgs(String nameIn, String additional) {
		if (nameIn != null) {
			WorldTile t = GlobalTileList.getTileFromName(nameIn);
			
			if (additional != null) {
				t.setAdditional(additional);
			}
			
			return t;
		}
		return null;
	}
	
	public static WorldTile randVariant(WorldTile in) {
		if (in == null) return null;
		try {
			WorldTile r = in.getClass().getConstructor().newInstance();
			GameTexture tex = in.getTexture();
			if (tex != null && tex.hasParent()) {
				r.setTexture(tex.getParent().getRandVariant());
			}
			return r;
		}
		catch (Exception e) { e.printStackTrace(); }
		return in;
	}
	
	public abstract WorldTile copy();
	protected WorldTile copyFields(WorldTile from, WorldTile to) {
		to.id = from.id;
		to.name = from.name;
		to.tex = from.tex;
		to.sideTex = from.sideTex;
		to.numVariants = from.numVariants;
		to.blocksMovement = from.blocksMovement;
		to.wildCardTexture = from.wildCardTexture;
		to.isWall = from.isWall;
		to.wallHeight = from.wallHeight;
		to.material = from.material;
		to.worldX = from.worldX;
		to.worldY = from.worldY;
		to.hasSideBrightness = from.hasSideBrightness;
		to.sideBrightness = from.sideBrightness;
		to.entitiesOnTile = from.entitiesOnTile;
		to.entitiesAdding = from.entitiesAdding;
		to.entitiesRemoving = from.entitiesRemoving;
		to.rotation = from.rotation;
		to.drawFlipped = from.drawFlipped;
		
		return to;
	}
	
	public static WorldTile copy(WorldTile tile) {
		return tile.copy();
	}
	
}
