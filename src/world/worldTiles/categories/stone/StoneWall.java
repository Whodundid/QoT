package world.worldTiles.categories.stone;

import assets.textures.world.walls.stone.StoneWallTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class StoneWall extends WorldTile {
	
	public StoneWall() { this(-1); }
	public StoneWall(int id) {
		super(TileIDs.STONE_WALL);
		setWall(true);
		wallHeight = 1;
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
	
}
