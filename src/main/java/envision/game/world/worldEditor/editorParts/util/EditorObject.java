package envision.game.world.worldEditor.editorParts.util;

import envision.engine.resourceLoaders.Sprite;
import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.util.IDrawable;
import envision.game.world.worldTiles.WorldTile;

/**
 * An item that can be placed and used in the map editor hotbar. Can be used to
 * reference any of the game's assets without needing to individually specify
 * a different type for each.
 */
public class EditorObject implements IDrawable {
	
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
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "ED[" + theObject + "]";
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
	
	public Sprite getSprite() {
	    return (theObject != null) ? theObject.getSprite() : null;
	}
	
	public boolean isSelected() { return isSelected; }
	public void setSelected(boolean val) { isSelected = val; }
	
	public static EditorObject of(WorldTile tileIn) { return new EditorObject(tileIn); }
	public static EditorObject of(GameObject entityIn) { return new EditorObject(entityIn); }
	
	@Override
	public double getSortPoint() {
		return theObject.getSortPoint();
	}
	
}
