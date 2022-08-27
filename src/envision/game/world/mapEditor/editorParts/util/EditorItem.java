package envision.game.world.mapEditor.editorParts.util;

import envision.game.GameObject;
import envision.game.world.worldTiles.WorldTile;
import envision.renderEngine.textureSystem.GameTexture;

/**
 * An item that can be placed and used in the map editor hotbar. Can be used to
 * reference any of the game's assets without needing to individually specify
 * a different type for each.
 */
public class EditorItem {
	
	WorldTile tile;
	GameObject object;
	
	EditorItemCategory type;
	
	public EditorItem(WorldTile tileIn) {
		tile = tileIn;
		type = EditorItemCategory.TILE;
	}
	
	public EditorItem(GameObject objectIn) {
		object = objectIn;
		type = EditorItemCategory.GAME_OBJECT;
	}
	
	public boolean isTile() { return type == EditorItemCategory.TILE; }
	public boolean isGameObject() { return type == EditorItemCategory.GAME_OBJECT; }
	
	public EditorItemCategory getType() { return type; }
	public WorldTile getTile() { return tile; }
	public GameObject getGameObject() { return object; }
	
	public String getName() {
		return switch (type) {
		case TILE -> (tile != null) ? tile.getName() : "null";
		case GAME_OBJECT -> (object != null) ? object.getName() : "null";
		default -> null;
		};
	}
	
	public GameTexture getTexture() {
		return switch (type) {
		case TILE -> (tile != null) ? tile.getTexture() : null;
		case GAME_OBJECT -> (object != null) ? object.getTexture() : null;
		default -> null;
		};
	}
	
	public static EditorItem of(WorldTile tileIn) { return new EditorItem(tileIn); }
	public static EditorItem of(GameObject entityIn) { return new EditorItem(entityIn); }
	
}
