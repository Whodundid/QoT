package qot.world_tiles.categories.house;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Door extends WorldTile {
	
    public boolean locked = false;
    public boolean open = false;
    
	public Door() {
		super(TileIDs.COUNTER);
		setSprite(new Sprite(HouseTextures.counter));
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Door());
	}
	
	@Override
	public void onTileClicked(Entity entity, int button) {
	    super.onTileClicked(entity, button);
	}
	
	public void open() {
	    blocksMovement = false;
	    setSprite(new Sprite(null));
	}
	
	public void close() {
	    blocksMovement = true;
	    setSprite(new Sprite(null));
	}
	
	public void unlock() { locked = false; }
	public void lock() { locked = true; }
	public void setLocked(boolean val) { locked = val; }
	public boolean isLocked() { return locked; }
	
}