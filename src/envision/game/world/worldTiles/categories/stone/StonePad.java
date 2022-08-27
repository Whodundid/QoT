package envision.game.world.worldTiles.categories.stone;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import game.assets.textures.world.floors.stone.StoneFloorTextures;

public class StonePad extends WorldTile {
	
	public StonePad() {
		super(TileIDs.STONE_PAD);
		setTexture(StoneFloorTextures.stone_pad);
		setWall(true);
		wallHeight = 0.1;
		
		rotation = Rotation.random();
	}
	
}