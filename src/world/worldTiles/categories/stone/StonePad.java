package world.worldTiles.categories.stone;

import assets.textures.world.floors.stone.StoneFloorTextures;
import eutil.misc.Rotation;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class StonePad extends WorldTile {
	
	public StonePad() {
		super(TileIDs.STONE_PAD);
		setTexture(StoneFloorTextures.stone_pad);
		setWall(true);
		wallHeight = 0.1;
		
		rotation = Rotation.random();
	}
	
}
