package qot.world_tiles.categories.stone;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.stone.StoneWallTextures;
import qot.world_tiles.TileIDs;

public class StoneWall extends WorldTile {
	
	public StoneWall() { this(-1); }
	public StoneWall(int id) {
		super(TileIDs.STONE_WALL);
		setWall(true);
		wallHeight = 0.75;
		blocksMovement = true;
		//rotation = Rotation.random();
		//drawFlipped = RandomUtil.randomBool();
		numVariants = StoneWallTextures.stone_wall.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneWallTextures.stone_wall.getRandVariant());
		}
		else {
			setTexture(StoneWallTextures.stone_wall.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StoneWall());
	}
	
}
