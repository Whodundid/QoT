package gameScreens.mapEditor.editorScreen.util;

import assets.entities.Entity;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.textureSystem.GameTexture;

/** An item that can be placed and used in the map editor hotbar. 
 *  Can be used to reference any of the game's assests without needing to individually specificy
 *  a different type for each. */
public class EditorItem {
	
	WorldTile tile;
	Entity entity;
	
	EditorItemCategory type;
	
	public EditorItem(WorldTile tileIn) {
		tile = tileIn;
		type = EditorItemCategory.TILE;
	}
	
	public EditorItem(Entity entityIn) {
		entity = entityIn;
		type = EditorItemCategory.ENTITY;
	}
	
	public boolean isTile() { return type == EditorItemCategory.TILE; }
	public boolean isEntity() { return type == EditorItemCategory.ENTITY; }
	
	public EditorItemCategory getType() { return type; }
	public WorldTile getTile() { return tile; }
	public Entity getEntity() { return entity; }
	
	public String getName() {
		return switch (type) {
		case TILE -> (tile != null) ? tile.getName() : "null";
		case ENTITY -> (entity != null) ? entity.getName() : "null";
		default -> null;
		};
	}
	
	public GameTexture getTexture() {
		return switch (type) {
		case TILE -> (tile != null) ? tile.getTexture() : null;
		case ENTITY -> (entity != null) ? entity.getTexture() : null;
		default -> null;
		};
	}
	
	public static EditorItem of(WorldTile tileIn) { return new EditorItem(tileIn); }
	public static EditorItem of(Entity entityIn) { return new EditorItem(entityIn); }
	
}
