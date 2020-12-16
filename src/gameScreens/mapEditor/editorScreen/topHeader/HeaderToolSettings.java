package gameScreens.mapEditor.editorScreen.topHeader;

import envisionEngine.eWindow.windowTypes.WindowObject;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTool;

public abstract class HeaderToolSettings extends WindowObject {
	
	EditorScreenTopHeader header;
	EditorTool tool;
	
	protected HeaderToolSettings(EditorScreenTopHeader headerIn, EditorTool toolIn) {
		header = headerIn;
		tool = toolIn;
	}
	
	public EditorScreenTopHeader getEditorHeader() { return header; }
	public EditorTool getToolType() { return tool; }
	
}
