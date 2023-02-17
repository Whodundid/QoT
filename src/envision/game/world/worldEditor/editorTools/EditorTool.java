package envision.game.world.worldEditor.editorTools;

import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.GLObject;
import envision.game.objects.GameObject;
import envision.game.objects.entities.Entity;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.MapEditorSettings;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.ToolHandler.ToolEvent;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.boxes.Box2;

public abstract class EditorTool extends GLObject {
	
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
	
	protected WorldTile getTile() { return editor.getActualWorld().getTileAt(wx, wy); }
	protected WorldTile getTile(int x, int y) { return editor.getActualWorld().getTileAt(x, y); }
	protected Box2<Integer, Integer> getHoverTileCoords() { return editor.getHoverTileCoords(); }
	protected Box2<Integer, Integer> getHoverPixelCoords() { return editor.getHoverPixelCoords(); }
	
	protected void addObjectToWorld(GameObject obj) {
		// logic to add object to world ~
	}
	
	protected void addEntityToWorld(Entity ent) {
		double cmx = ent.collisionBox.midX; //collision mid x
		double cmy = ent.collisionBox.midY; //collision mid y
		double tw = editor.getEditorWorld().getTileWidth(); //tile width
		double th = editor.getEditorWorld().getTileHeight(); //tile height
		
		//need to -, - using difference of world coords of middle of collision box
		int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
		int mwcy = (int) Math.floor(cmy / th); //mid world coords y
		
		ent.world = editor.getEditorWorld();
		ent.setWorldPos(wx - mwcx, wy - mwcy);
		
		//System.out.println(ent + " : " + ent.worldX + " : " + ent.worldY);
		editor.getEditorWorld().addEntity(ent);
		editor.getEditorWorld().addEntitySpawn(ent);
	}
	
	protected void setTile(WorldTile t) { editor.setTileAt(wx, wy, t); }
	protected void setTile(int x, int y, WorldTile t) { editor.setTileAt(x, y, t); }
	
	protected EditorObject getPrimary() { return editor.getSettings().getPrimaryPalette(); }
	protected EditorObject getSecondary() { return editor.getSettings().getSecondaryPalette(); }
	protected void setPrimary(EditorObject i) { editor.getSettings().setPrimaryPalette(i); }
	
	protected void setPrimary(WorldTile t) { editor.getSettings().setPrimaryPalette(EditorObject.of(t)); }
	protected void setPrimary(Entity t) { editor.getSettings().setPrimaryPalette(EditorObject.of(t)); }
	protected void setSecondary(EditorObject i) { editor.getSettings().setSecondaryPalette(i); }
	protected void setSecondary(WorldTile t) { editor.getSettings().setSecondaryPalette(EditorObject.of(t)); }
	protected void setSecondary(Entity t) { editor.getSettings().setSecondaryPalette(EditorObject.of(t)); }
	
	//------------------
	// Abstract Methods
	//------------------
	
	protected void drawTool(double x, double y, double w, double h) {}
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
		if (editor.getCurrentSidePanel() == SidePanelType.TERRAIN) {
			if (!oldPoint.compare(editor.worldXPos, editor.worldYPos)) {
				oldPoint.set(editor.worldXPos, editor.worldYPos); //update the old point
				if (editor.isMouseInMap()) {
					updateWorldPoint();
					onUpdate();
				}
			}
		}
		else {
			oldPoint.set(editor.worldXPos, editor.worldYPos); //update the old point
			if (editor.isMouseInMap()) {
				updateWorldPoint();
				onUpdate();
			}
		}

		
//		if (pressed) {

//		}
	}
	
	private void updateWorldPoint() {
		wx = editor.worldXPos;
		wy = editor.worldYPos;
	}
	
}
