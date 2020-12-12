package gameSystems.mapSystem.worldTiles;

import gameSystems.textureSystem.GameTexture;

public class WorldTile {
	
	protected int id;
	protected String name;
	protected GameTexture tex;
	protected int numVariants = 1;
	protected boolean blocksMovement = false;
	protected boolean wildCardTexture = false;
	protected TileMaterial material = TileMaterial.VOID;
	
	protected WorldTile(int id) { this(id, "unnamed tile", null); }
	protected WorldTile(int id, String nameIn) { this(id, nameIn, null); }
	protected WorldTile(int id, GameTexture texIn) { this(id, "unnamed tile", texIn); }
	protected WorldTile(int id, String nameIn, GameTexture texIn) {
		this.id = id;
		name = nameIn;
		tex = texIn;
	}
	
	//---------
	// Getters
	//---------
	
	public boolean hasTexture() { return tex != null; }
	public boolean blocksMovement() { return blocksMovement; }
	public boolean isWildCard() { return wildCardTexture; }
	
	public int getID() { return id; }
	public String getName() { return name; }
	public TileMaterial getMaterial() { return material; }
	public int getNumVariants() { return numVariants; }
	
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
	public static WorldTile getTileFromID(int id, int texNum) { return WorldTiles.getTileFromID(id, texNum); }
	
	public static int getIDFromTile(WorldTile in) {
		return (in != null) ? in.getID() : -1;
	}
	
	public static WorldTile getTileFromName(String nameIn) { return getTileFromArgs(nameIn, null); }
	public static WorldTile getTileFromArgs(String nameIn, String additional) {
		if (nameIn != null) {
			WorldTile t = WorldTiles.getTileFromName(nameIn);
			
			if (additional != null) {
				t.setAdditional(additional);
			}
			
			return t;
		}
		return null;
	}
	
}
