package envisionEngine.gameEngine.world.worldEditor.editorTools.tools;

import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.gameEngine.world.worldEditor.editorTools.EditorTool;

public class Tool_Eraser extends EditorTool {

	public Tool_Eraser(MapEditorScreen in) {
		super(in);
	}
	
	@Override public void onPress() { setTile(null); }
	@Override public void onUpdate() { setTile(null); }
	
}
