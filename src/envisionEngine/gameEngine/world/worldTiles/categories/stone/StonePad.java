package envisionEngine.gameEngine.world.worldTiles.categories.stone;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;

public class StonePad extends WorldTile {
	
	public StonePad() {
		super(TileIDs.STONE_PAD);
		setTexture(StoneFloorTextures.stone_pad);
		setWall(true);
		wallHeight = 0.1;
		
		rotation = Rotation.random();
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StonePad());
	}
	
}
