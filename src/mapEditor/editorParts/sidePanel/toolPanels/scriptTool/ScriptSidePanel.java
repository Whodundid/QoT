package mapEditor.editorParts.sidePanel.toolPanels.scriptTool;

import mapEditor.MapEditorScreen;
import mapEditor.editorParts.sidePanel.EditorSidePanel;
import mapEditor.editorParts.sidePanel.SidePanelType;
import mapEditor.editorParts.sidePanel.SidePanel;
import windowLib.windowTypes.interfaces.IActionObject;

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
