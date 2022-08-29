package envision.game.world.mapEditor;

import java.io.File;

import envision.game.world.GameWorld;
import envision.game.world.mapEditor.editorParts.util.EditorObject;
import envision.game.world.mapEditor.editorTools.EditorToolType;

/** A holder that keeps track of active map editor properties. */
public class MapEditorSettings {
	
	//----------
	// Settings
	//----------
	
	protected final MapEditorScreen editor;
	
	protected EditorObject primaryPalette, secondaryPalette;
	
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
	public GameWorld getGameWorld() { return editor.actualWorld; }
	
	public EditorObject getPrimaryPalette() { return primaryPalette; }
	public EditorObject getSecondaryPalette() { return secondaryPalette; }
	
	public EditorToolType getCurrentTool() { return currentTool; }
	
	
	//---------
	// Setters
	//---------
	
	public void setPrimaryPalette(EditorObject itemIn) { primaryPalette = itemIn; }
	public void setSecondaryPalette(EditorObject itemIn) { secondaryPalette = itemIn; }
	
	public void setCurrentTool(EditorToolType tIn) { currentTool = tIn; }
	
}
