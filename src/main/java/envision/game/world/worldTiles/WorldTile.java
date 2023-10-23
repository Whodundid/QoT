package envision.game.world.worldTiles;

import envision.Envision;
import envision.engine.resourceLoaders.Sprite;
import envision.game.component.ComponentBasedObject;
import envision.game.entities.Entity;
import eutil.colors.EColors;
import eutil.misc.Rotation;
import qot.world_tiles.GlobalTileList;
import qot.world_tiles.TileIDs;

public abstract class WorldTile extends ComponentBasedObject implements Comparable<WorldTile> {
	
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
	protected Sprite sideTex;
	
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
	
//	protected EList<Entity> entitiesOnTile = new EArrayList<>(10);
//	protected EList<Entity> entitiesAdding = new EArrayList<>(10);
//	protected EList<Entity> entitiesRemoving = new EArrayList<>(10);
	
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
	
	protected WorldTile() {}
	protected WorldTile(TileIDs idIn) {
		id = idIn;
		name = id.name;
		
		addComponent(new WorldTileRenderer(this));
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
		return (worldY + 1) * Envision.theWorld.getTileHeight();
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
	
	//---------
	// Getters
	//---------
	
	public boolean hasSprite() { return sprite != null; }
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
	
	public WorldTile setSprite(Sprite texIn) { sprite = texIn; return this; }
	public WorldTile setSideSprite(Sprite texIn) { sideTex = texIn; return this; }
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
			Sprite tex = in.getSprite();
//			if (tex != null && tex.hasParent()) {
//				r.setTexture(tex.getParent().getRandVariant());
//			}
			return r;
		}
		catch (Exception e) { e.printStackTrace(); }
		return in;
	}
	
	public abstract WorldTile copy();
	protected WorldTile copyFields(WorldTile from, WorldTile to) {
		to.id = from.id;
		to.name = from.name;
		to.sprite = from.sprite;
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
//		to.entitiesOnTile = from.entitiesOnTile;
//		to.entitiesAdding = from.entitiesAdding;
//		to.entitiesRemoving = from.entitiesRemoving;
		to.rotation = from.rotation;
		to.drawFlipped = from.drawFlipped;
		
		return to;
	}
	
	public static WorldTile copy(WorldTile tile) {
		return tile.copy();
	}
	
}
