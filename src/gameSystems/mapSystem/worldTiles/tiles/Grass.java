package gameSystems.mapSystem.worldTiles.tiles;

import assets.textures.WorldTextures;
import gameSystems.mapSystem.worldTiles.WorldTile;

public class Grass extends WorldTile {
	
	public Grass() { this(-1); }
	public Grass(int id) {
		super(1, "Grass");
		
		numVariants = 4;
		
		if (id < 0) {
			setTexture(WorldTextures.grass.getRandVariant());
		}
		else {
			setTexture(WorldTextures.grass.getChild(id));
		}
		
		blocksMovement = false;
	}
	
}
