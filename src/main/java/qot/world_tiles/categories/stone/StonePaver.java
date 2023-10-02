package qot.world_tiles.categories.stone;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class StonePaver extends WorldTile {
	
	public StonePaver() { this(-1); }
	public StonePaver(int id) {
		super(TileIDs.STONE_PAVER);
		//rotation = Rotation.random();
		drawFlipped = ERandomUtil.randomBool();
		numVariants = StoneFloorTextures.stone_paver.getChildren().size();
		
		if (id < 0) {
		    setSprite(new Sprite(StoneFloorTextures.stone_paver.getRandVariant()));
		}
		else {
		    setSprite(new Sprite(StoneFloorTextures.stone_paver.getChild(id)));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StonePaver());
	}
	
}