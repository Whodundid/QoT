package gameSystems.mapSystem.worldTiles.tiles;

import assets.textures.WorldTextures;
import gameSystems.mapSystem.worldTiles.WorldTile;

public class Dirt extends WorldTile {
	
	public Dirt() {
		super(2, "Dirt");
		
		setTexture(WorldTextures.dirt);
		
		blocksMovement = false;
	}
	
}
