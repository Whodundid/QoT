package gameSystems.mapSystem.worldTiles.tiles;

import assets.textures.WorldTextures;
import gameSystems.mapSystem.worldTiles.WorldTile;

public class Water extends WorldTile {
	
	public Water() {
		super(4, "Water", WorldTextures.water);
		
		setBlocksMovement(true);
	}
	
}
