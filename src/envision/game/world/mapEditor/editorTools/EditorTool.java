package envision.game.world.mapEditor.editorTools;

import envision.game.GameObject;
import envision.game.entity.Entity;
import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.MapEditorSettings;
import envision.game.world.mapEditor.editorParts.util.EditorItem;
import envision.game.world.mapEditor.editorTools.ToolHandler.ToolEvent;
import envision.game.world.worldTiles.WorldTile;
import envision.inputHandlers.Mouse;
import eutil.datatypes.Box2;

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
			double pw = ent.width; //pixel width
			double ph = ent.height; //pixel height
			double cmx = ent.collisionBox.midX; //collision mid x
			double cmy = ent.collisionBox.midY; //collision mid y
			double tw = editor.getWorld().getTileWidth(); //tile width
			double th = editor.getWorld().getTileHeight(); //tile height
			
			//need to -, - using difference of world coords of middle of collision box
			int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			ent.worldX = wx - mwcx;
			ent.worldY = wy - mwcy;
			//System.out.println(ent + " : " + ent.worldX + " : " + ent.worldY);
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