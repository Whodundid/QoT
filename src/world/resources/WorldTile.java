package world.resources;

import engine.renderEngine.textureSystem.GameTexture;

public class WorldTile implements Comparable<WorldTile> {
	
	protected TileIDs id;
	protected String name;
	protected GameTexture tex;
	protected int numVariants = 1;
	protected boolean blocksMovement = false;
	protected boolean wildCardTexture = false;
	protected TileMaterial material = TileMaterial.VOID;
	protected int worldX, worldY;
	
	protected WorldTile(TileIDs idIn) {
		id = idIn;
		name = id.name;
		tex = id.texture;
	}
	
	@Override public String toString() { return id + ""; }
	
	@Override
	public int compareTo(WorldTile in) {
		return Integer.compare(id.id, in.id.id);
	}
	
	//---------
	// Getters
	//---------
	
	public boolean hasTexture() { return tex != null; }
	public boolean blocksMovement() { return blocksMovement; }
	public boolean isWildCard() { return wildCardTexture; }
	
	public int getID() { return id.id; }
	public String getName() { return name; }
	public TileMaterial getMaterial() { return material; }
	public int getMapColor() { return id.mapColor; }
	public int getNumVariants() { return numVariants; }
	
	public WorldTile setWorldPos(int x, int y) { worldX = x; worldY = y; return this; }
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	
	public GameTexture getTexture() { return tex; }
	
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
	public WorldTile setBlocksMovement(boolean val) { blocksMovement = val; return this; }
	public WorldTile setWildCard(boolean val) { wildCardTexture = val; return this; }
	
	public WorldTile setAdditional(String in) {
		if (in != null) {
			String[] values = in.split(" ");
			
			try {
				if (values.length >= 1) { blocksMovement = Boolean.valueOf(values[0]); }
				if (values.length >= 2) { material = TileMaterial.getMaterial(values[1]); }
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
	public static WorldTile getTileFromID(int id, int texNum) { return GlobalTileList.getTileFromID(id, texNum); }
	
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
		if (in == null) { return null; }
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
