package gameScreens.mapEditor.editorScreen.topHeader;

import envisionEngine.eWindow.windowTypes.WindowObject;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTileTool;

public abstract class HeaderToolSettings extends WindowObject {
	
	EditorScreenTopHeader header;
	EditorTileTool tool;
	
	protected HeaderToolSettings(EditorScreenTopHeader headerIn, EditorTileTool toolIn) {
		header = headerIn;
		tool = toolIn;
	}
	
	public EditorScreenTopHeader getEditorHeader() { return header; }
	public EditorTileTool getToolType() { return tool; }
	
}
