package world.mapEditor.editorParts.sidePanel.toolPanels.scriptTool;

import engine.windowLib.windowTypes.interfaces.IActionObject;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanelType;

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
