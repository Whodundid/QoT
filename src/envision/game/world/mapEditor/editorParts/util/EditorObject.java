package envision.game.world.mapEditor.editorParts.util;

import envision.game.GameObject;
import envision.game.world.worldTiles.WorldTile;
import envision.renderEngine.textureSystem.GameTexture;

/**
 * An item that can be placed and used in the map editor hotbar. Can be used to
 * reference any of the game's assets without needing to individually specify
 * a different type for each.
 */
public class EditorObject {
	
	//--------
	// Fields
	//--------
	
	private WorldTile tile;
	private GameObject object;
	private EditorItemCategory type;
	private boolean isSelected = false;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorObject(WorldTile tileIn) {
		tile = tileIn;
		type = EditorItemCategory.TILE;
	}
	
	public EditorObject(GameObject objectIn) {
		object = objectIn;
		type = EditorItemCategory.GAME_OBJECT;
	}
	
	public boolean isTile() { return type == EditorItemCategory.TILE; }
	public boolean isGameObject() { return type == EditorItemCategory.GAME_OBJECT; }
	
	public EditorItemCategory getType() { return type; }
	public WorldTile getTile() { return tile; }
	public GameObject getGameObject() { return object; }
	
	public <TYPE> TYPE get() {
		return (TYPE) ((isTile()) ? tile : object);
	}
	
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
	
	public boolean isSelected() { return isSelected; }
	public void setSelected(boolean val) { isSelected = val; }
	
	public static EditorObject of(WorldTile tileIn) { return new EditorObject(tileIn); }
	public static EditorObject of(GameObject entityIn) { return new EditorObject(entityIn); }
	
}
