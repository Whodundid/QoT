package world.worldTiles.categories.nature;

import assets.textures.world.nature.sand.SandTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		//setTexture(SandTextures.sand);
		setWall(true);
		wallHeight = 0.025;
		
		setTexture(SandTextures.sand);
	}
	
}
