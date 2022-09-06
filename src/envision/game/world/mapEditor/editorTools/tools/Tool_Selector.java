package envision.game.world.mapEditor.editorTools.tools;

import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.mapEditor.editorTools.EditorTool;
import envision.inputHandlers.Mouse;

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
