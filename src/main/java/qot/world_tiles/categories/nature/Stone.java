package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class Stone extends WorldTile {
	
	public Stone() {
		super(TileIDs.STONE);
		setSprite(new Sprite(StoneFloorTextures.stone_pad));
		setBlocksMovement(true);
		setWall(true);
		
		randomizeValues();
		setMiniMapColor(0xff707070);
	}
	
	@Override
	public void randomizeValues() {
	    wallHeight = ERandomUtil.getRoll(0.25, 0.75);
	    rotation = Rotation.random();
	}
	
	@Override public boolean hasVariation() { return true; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Stone());
	}
	
}
