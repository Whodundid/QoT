package gameSystems.mapSystem.worldTiles.tiles;

import gameSystems.mapSystem.worldTiles.WorldTile;
import gameTextures.WorldTextures;

public class Grass extends WorldTile {
	
	public Grass() {
		super(1, "Grass");
		
		setTexture(WorldTextures.grass);
		
		blocksMovement = false;
	}
	
}
