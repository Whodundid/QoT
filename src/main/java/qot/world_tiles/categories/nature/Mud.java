package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
		setSprite(new Sprite(NatureTextures.mud));
		setWall(true);
		wallHeight = -0.025;
		setMiniMapColor(0xff653617);
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Mud());
	}
	
}
