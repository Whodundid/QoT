package gameSystems.mapSystem.worldTiles.tiles;

import gameSystems.mapSystem.worldTiles.WorldTile;
import gameTextures.WorldTextures;

public class Water extends WorldTile {
	
	public Water() {
		super("Water");
		
		setTexture(WorldTextures.water);
		
		blocksMovement = true;
	}
	
}
