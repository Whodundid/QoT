package gameSystems.mapSystem.worldTiles;

import gameSystems.textureSystem.GameTexture;

public class WorldTile {
	
	protected String name;
	protected GameTexture tex;
	protected boolean blocksMovement = false;
	protected TileMaterial material = TileMaterial.VOID;
	
	protected WorldTile() { this("unnamed tile", null); }
	protected WorldTile(String nameIn) { this(nameIn, null); }
	protected WorldTile(GameTexture texIn) { this("unnamed tile", texIn); }
	protected WorldTile(String nameIn, GameTexture texIn) {
		name = nameIn;
		tex = texIn;
	}
	
	//---------
	// Getters
	//---------
	
	public boolean hasTexture() { return tex != null; }
	public boolean blocksMovement() { return blocksMovement; }
	
	public String getName() { return name; }
	public GameTexture getTexture() { return tex; }
	public TileMaterial getMaterial() { return material; }
	
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
	
	public static WorldTile of(String nameIn) { return of(nameIn, null); }
	public static WorldTile of(String nameIn, String additional) {
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
