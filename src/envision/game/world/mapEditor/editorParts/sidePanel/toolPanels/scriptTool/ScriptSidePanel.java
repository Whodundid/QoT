package envision.game.world.mapEditor.editorParts.sidePanel.toolPanels.scriptTool;

import envision.game.world.mapEditor.MapEditorScreen;
import envision.game.world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.mapEditor.editorParts.sidePanel.SidePanel;
import envision.game.world.mapEditor.editorParts.sidePanel.SidePanelType;
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
