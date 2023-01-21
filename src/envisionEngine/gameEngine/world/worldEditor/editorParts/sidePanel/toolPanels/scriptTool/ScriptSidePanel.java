package envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.scriptTool;

import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanel;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;

public class ScriptSidePanel extends SidePanel {

	public ScriptSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.SCRIPTS);
	}

	@Override
	public void loadTool() {
		panel.setHidden(true);
	}

	@Override
	public void drawTool(int mXIn, int mYIn) {
	}

	@Override
	public void onAction(IActionObject object, Object... args) {
	}
	
}
