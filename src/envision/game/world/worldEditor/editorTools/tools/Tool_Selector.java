package envision.game.world.worldEditor.editorTools.tools;

import envision.engine.inputHandlers.Mouse;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorTools.EditorTool;

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
