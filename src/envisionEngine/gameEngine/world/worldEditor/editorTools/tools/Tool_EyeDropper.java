package envisionEngine.gameEngine.world.worldEditor.editorTools.tools;

import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.gameEngine.world.worldEditor.editorTools.EditorTool;

public class Tool_EyeDropper extends EditorTool {

	public Tool_EyeDropper(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		if (button == 0) setPrimary(getTile());
		else if (button == 1) setSecondary(getTile());
	}

	@Override
	public void onUpdate() {
		if (button == 0) setPrimary(getTile());
		else if (button == 1) setSecondary(getTile());
	}
	
}
