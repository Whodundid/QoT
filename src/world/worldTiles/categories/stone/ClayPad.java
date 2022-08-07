package world.worldTiles.categories.stone;

import assets.textures.world.floors.stone.StoneFloorTextures;
import eutil.random.RandomUtil;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class ClayPad extends WorldTile {
	
	public ClayPad() { this(-1); }
	public ClayPad(int id) {
		super(TileIDs.CLAY_PAD);
		setWall(true);
		wallHeight = 0.1;
		//rotation = Rotation.random();
		drawFlipped = RandomUtil.randomBool();
		numVariants = StoneFloorTextures.clay_pad.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneFloorTextures.clay_pad.getRandVariant());
		}
		else {
			setTexture(StoneFloorTextures.clay_pad.getChild(id));
		}
	}
	
}