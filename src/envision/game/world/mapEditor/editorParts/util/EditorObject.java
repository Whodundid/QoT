package envision.game.world.mapEditor.editorParts.util;

import envision.game.GameObject;
import envision.game.entity.Entity;
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
	
	private GameObject theObject;
	private EditorObjectType type;
	private boolean isSelected = false;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorObject(GameObject objectIn) { this(objectIn, EditorObjectType.GAME_OBJECT); }
	public EditorObject(WorldTile tileIn) { this(tileIn, EditorObjectType.TILE); }
	public EditorObject(Entity entityIn) { this(entityIn, EditorObjectType.TILE); }
	
	protected EditorObject(GameObject objectIn, EditorObjectType typeIn) {
		theObject = objectIn;
		type = typeIn;
	}
	
	//---------
	// Methods
	//---------
	
	public boolean isTile() { return type == EditorObjectType.TILE; }
	public boolean isEntity() { return type == EditorObjectType.ENTITY; }
	
	public EditorObjectType getType() { return type; }
	
	public GameObject getGameObject() { return theObject; }
	public Entity getEntity() { return (Entity) theObject; }
	public WorldTile getTile() { return (WorldTile) theObject; }
	
	public <TYPE extends GameObject> TYPE get() {
		return (TYPE) switch (type) {
		case GAME_OBJECT -> theObject;
		case ENTITY -> (Entity) theObject;
		case TILE -> (WorldTile) theObject;
		};
	}
	
	public String getName() {
		return (theObject != null) ? theObject.getName() : "null";
	}
	
	public GameTexture getTexture() {
		return (theObject != null) ? theObject.getTexture() : null;
	}
	
	public boolean isSelected() { return isSelected; }
	public void setSelected(boolean val) { isSelected = val; }
	
	public static EditorObject of(WorldTile tileIn) { return new EditorObject(tileIn); }
	public static EditorObject of(GameObject entityIn) { return new EditorObject(entityIn); }
	
}
