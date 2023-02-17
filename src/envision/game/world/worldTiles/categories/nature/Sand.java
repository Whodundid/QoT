package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;

public class Sand extends WorldTile {
	
	public Sand() {
		super(TileIDs.SAND);
		//setTexture(SandTextures.sand);
		setWall(true);
		wallHeight = 0.025;
		
		setTexture(SandTextures.sand);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Sand());
	}
	
}
