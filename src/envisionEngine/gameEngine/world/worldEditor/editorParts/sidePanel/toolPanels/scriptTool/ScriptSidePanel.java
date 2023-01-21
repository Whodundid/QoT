package envision.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.scriptTool;

import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanel;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.windowLib.windowTypes.interfaces.IActionObject;

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
