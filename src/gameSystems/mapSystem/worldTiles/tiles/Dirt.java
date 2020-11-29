package gameSystems.mapSystem.worldTiles.tiles;

import gameSystems.mapSystem.worldTiles.WorldTile;
import gameTextures.WorldTextures;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super("Dirt");
		
		setTexture(WorldTextures.dirt);
		
		blocksMovement = false;
	}
	
}
