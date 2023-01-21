package envisionEngine.gameEngine.world.worldEditor.editorTools.tools;

import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envisionEngine.gameEngine.world.worldEditor.editorTools.EditorTool;
import envisionEngine.inputHandlers.Mouse;

public class Tool_Selector extends EditorTool {
	
	public Tool_Selector(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		//get current mode
		if (editor.getSidePanel().getCurrentPanelType() == SidePanelType.ASSET) {
			//currently need to iterate through entity spawns as
			//they're not actually in the world

			double mX = Mouse.getMx();
			double mY = Mouse.getMy();
			
			for (var spawn : editor.getActualWorld().getEntitySpawns()) {
				
			}
			
		}
	}
	
}
