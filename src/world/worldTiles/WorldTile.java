package world.worldTiles;

import engine.renderEngine.GLObject;
import engine.renderEngine.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.entities.Entity;
import world.GameWorld;

public abstract class WorldTile extends GLObject implements Comparable<WorldTile> {
	
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
	protected GameTexture tex;
	
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
	protected int worldX, worldY;
	
	protected boolean hasSideBrightness = false;
	protected int sideBrightness = 255;
	
	protected EArrayList<Entity> entitiesOnTile = new EArrayList(10);
	protected EArrayList<Entity> entitiesAdding = new EArrayList(10);
	protected EArrayList<Entity> entitiesRemoving = new EArrayList(10);
	
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
		return Integer.compare(id.id, in.id.id);
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
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		double wh = h * wallHeight; //wh == 'wallHeight'
		
		WorldTile tb = null; // tb == 'tileBelow'
		WorldTile ta = null; // ta == 'tileAbove'
		
		if ((worldY + 1) < world.getHeight()) tb = world.getWorldData()[worldX][worldY + 1];
		if ((worldY - 1) >= 0) ta = world.getWorldData()[worldX][worldY - 1];
		
		if (isWall) {
			//determine tile brightness
			int tileBrightness = brightness;
			int wallBrightness = brightness;
			
			if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
			

			
			//check if the tile directly above is a wall
			//if so - don't draw wall side
			if (wh >= 0) {
				//draw main texture slightly above main location
				drawTexture(tex, x, y - wh, w, h, false, tileBrightness);
				
				GameTexture side = (sideTex != null) ? sideTex : tex;
				
				double yPos = y + h - wh;
				wallBrightness = EColors.changeBrightness(brightness, 145);
				
				//draw wall side slightly below
				drawTexture(side, x, yPos, w, wh, false, wallBrightness);
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if ((tb == null || !tb.hasTexture())) {
					drawTexture(tex, x, y + h, w, h / 2, false, EColors.changeBrightness(brightness, 145));
				}
			}
			else {
				wh = -wh;
				double yPos = y + wh;
				
				//draw main texture slightly below main location
				drawTexture(tex, x, yPos, w, h, false, tileBrightness);
				
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
					drawTexture(side, x, sideWallY, w, wh, false, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				if (tb == null || !tb.hasTexture()) {
					drawTexture(tex, x, yPos + h, w, (h / 2) - wh, false, EColors.changeBrightness(brightness, 145));
				}
			}
		}
		else {
			drawTexture(tex, x, y, w, h, false, brightness);
			
			//draw bottom of map edge or if right above a tile with no texture/void
			if ((tb == null || !tb.hasTexture())) {
				drawTexture(tex, x, y + h, w, h / 2, false, EColors.changeBrightness(brightness, 145));
			}
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
	
	public int getID() { return id.id; }
	public String getName() { return name; }
	public TileMaterial getMaterial() { return material; }
	public int getMapColor() { return id.mapColor; }
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
	
	//---------
	// Setters
	//---------
	
	public WorldTile setTexture(GameTexture texIn) { tex = texIn; return this; }
	public WorldTile setSideTexture(GameTexture texIn) { sideTex = texIn; return this; }
	public WorldTile setBlocksMovement(boolean val) { blocksMovement = val; return this; }
	public WorldTile setWall(boolean val) { isWall = val; return this; }
	public WorldTile setWildCard(boolean val) { wildCardTexture = val; return this; }
	
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
	
}
