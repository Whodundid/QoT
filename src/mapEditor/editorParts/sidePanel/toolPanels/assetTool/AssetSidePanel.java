package mapEditor.editorParts.sidePanel.toolPanels.assetTool;

import mapEditor.MapEditorScreen;
import mapEditor.editorParts.sidePanel.EditorSidePanel;
import mapEditor.editorParts.sidePanel.SidePanelType;
import mapEditor.editorParts.sidePanel.SidePanel;
import windowLib.windowTypes.interfaces.IActionObject;

public class AssetSidePanel extends SidePanel {

	public AssetSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.ASSET);
	}

	@Override
	public void loadTool() {
	}

	@Override
	public void drawTool(int mXIn, int mYIn) {
	}

	@Override
	public void onAction(IActionObject object, Object... args) {
	}
	
}
