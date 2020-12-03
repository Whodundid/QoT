package gameSystems.mapSystem.worldTiles.tiles;

import gameSystems.mapSystem.worldTiles.WorldTile;
import gameTextures.WorldTextures;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(2, "Dirt");
		
		setTexture(WorldTextures.dirt);
		
		blocksMovement = false;
	}
	
}
