package world.mapEditor.editorTools;

import engine.inputHandlers.Mouse;
import eutil.datatypes.Box2;
import game.GameObject;
import game.entities.Entity;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.MapEditorSettings;
import world.mapEditor.editorParts.util.EditorItem;
import world.mapEditor.editorTools.ToolHandler.ToolEvent;
import world.worldTiles.WorldTile;

public abstract class EditorTool {
	
	protected MapEditorScreen editor;
	protected MapEditorSettings settings;
	protected boolean isSelected = false;
	protected Box2<Integer, Integer> pressPoint = new Box2(-1, -1);
	protected Box2<Integer, Integer> oldPoint = new Box2(-1, -1);
	protected int button = -1;
	protected boolean pressed = false;
	protected int wx, wy;
	
	//--------------
	// Constructors
	//--------------
	
	protected EditorTool(MapEditorScreen in) {
		editor = in;
		settings = in.getSettings();
	}
	
	//---------
	// Methods
	//---------
	
	public void setSelected(boolean val) { isSelected = val; }
	public boolean isSelected() { return isSelected; }
	
	protected WorldTile getTile() { return editor.getWorld().getTileAt(wx, wy); }
	protected WorldTile getTile(int x, int y) { return editor.getWorld().getTileAt(x, y); }
	
	protected void addObjectToWorld(GameObject obj) {
		if (obj instanceof Entity ent) {
			ent.worldX = wx - (ent.width / editor.getWorld().getTileWidth()) / 2;
			ent.worldY = wy - (ent.height / editor.getWorld().getTileHeight()) / 2 - 2;
			System.out.println(ent + " : " + ent.worldX + " : " + ent.worldY);
			editor.getWorld().addEntity(ent);
			editor.getWorld().addEntitySpawn(ent);
		}
	}
	
	protected void setTile(WorldTile t) { editor.getWorld().setTileAt(wx, wy, t); }
	protected void setTile(int x, int y, WorldTile t) { editor.getWorld().setTileAt(x, y, t); }
	
	protected EditorItem getPrimary() { return editor.getSettings().getPrimaryPalette(); }
	protected EditorItem getSecondary() { return editor.getSettings().getSecondaryPalette(); }
	protected void setPrimary(EditorItem i) { editor.getSettings().setPrimaryPalette(i); }
	
	protected void setPrimary(WorldTile t) { editor.getSettings().setPrimaryPalette(EditorItem.of(t)); }
	protected void setPrimary(Entity t) { editor.getSettings().setPrimaryPalette(EditorItem.of(t)); }
	protected void setSecondary(EditorItem i) { editor.getSettings().setSecondaryPalette(i); }
	protected void setSecondary(WorldTile t) { editor.getSettings().setSecondaryPalette(EditorItem.of(t)); }
	protected void setSecondary(Entity t) { editor.getSettings().setSecondaryPalette(EditorItem.of(t)); }
	
	//------------------
	// Abstract Methods
	//------------------
	
	protected void onPress() {}
	protected void onRelease() {}
	protected void onUpdate() {}
	
	//----------
	// Internal
	//----------
	
	public void distributeEvent(ToolEvent event, int button) {
		switch (event) {
		case PRESS: onPressI(button); break;
		case RELEASE:onReleaseI(button); break;
		case UPDATE: onUpdateI(); break;
		}
	}
	
	private void onPressI(int buttonIn) {
		pressed = true;
		button = buttonIn;
		pressPoint.set(Mouse.getPos());
		oldPoint.set(pressPoint);
		if (editor.isMouseInMap()) {
			updateWorldPoint();
			onPress();
		}
	}
	
	private void onReleaseI(int buttonIn) {
		pressed = false;
		button = buttonIn;
		if (editor.isMouseInMap()) {
			onRelease();
		}
	}
	
	private void onUpdateI() {
		if (pressed) {
			if (!Mouse.isAnyButtonDown()) { pressed = false; return; }
			if (!oldPoint.compare(editor.worldXPos, editor.worldYPos)) {
				oldPoint.set(editor.worldXPos, editor.worldYPos); //update the old point
				if (editor.isMouseInMap()) {
					updateWorldPoint();
					onUpdate();
				}
			}
		}
	}
	
	private void updateWorldPoint() {
		wx = editor.worldXPos;
		wy = editor.worldYPos;
	}
	
}
