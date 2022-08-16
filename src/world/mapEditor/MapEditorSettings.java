package world.mapEditor;

import java.io.File;

import world.GameWorld;
import world.mapEditor.editorParts.util.EditorItem;
import world.mapEditor.editorTools.EditorToolType;

/** A holder that keeps track of active map editor properties. */
public class MapEditorSettings {
	
	//----------
	// Settings
	//----------
	
	protected final MapEditorScreen editor;
	
	protected EditorItem primaryPalette, secondaryPalette;
	
	public boolean drawCenterPositionBox = false;
	public boolean drawMapBorders = false;
	public boolean drawEntities = true;
	public boolean drawRegions = true;
	public boolean drawEntityHitBoxes = false;
	public boolean drawWallBox = false;
	
	public EditorToolType currentTool;


	
	//--------------
	// Constructors
	//--------------
	
	public MapEditorSettings(MapEditorScreen editorIn) {
		editor = editorIn;
	}
	
	//---------
	// Getters
	//---------
	
	public File getMapFile() { return editor.mapFile; }
	public GameWorld getGameWorld() { return editor.world; }
	
	public EditorItem getPrimaryPalette() { return primaryPalette; }
	public EditorItem getSecondaryPalette() { return secondaryPalette; }
	
	public EditorToolType getCurrentTool() { return currentTool; }
	
	
	//---------
	// Setters
	//---------
	
	public void setPrimaryPalette(EditorItem itemIn) { primaryPalette = itemIn; }
	public void setSecondaryPalette(EditorItem itemIn) { secondaryPalette = itemIn; }
	
	public void setCurrentTool(EditorToolType tIn) { currentTool = tIn; }
	
}
