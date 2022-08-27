package envision.game.world.mapEditor.editorTools.tools;

import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.editorTools.EditorTool;

public class Tool_Eraser extends EditorTool {

	public Tool_Eraser(MapEditorScreen in) {
		super(in);
	}
	
	@Override public void onPress() { setTile(null); }
	@Override public void onUpdate() { setTile(null); }
	
}
