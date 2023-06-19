package envision.game.world.worldEditor.editorTools.tools;

import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorTools.EditorTool;

public class Tool_Eraser extends EditorTool {

	public Tool_Eraser(MapEditorScreen in) {
		super(in);
	}
	
	@Override public void onPress() { setTile(null); }
	@Override public void onUpdate() { setTile(null); }
	
}