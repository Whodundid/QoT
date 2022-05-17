package world.mapEditor.editorParts.sidePanel.toolPanels.assetTool;

import engine.windowLib.windowTypes.interfaces.IActionObject;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import world.mapEditor.editorParts.sidePanel.PaletteSidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanelType;

public class AssetSidePanel extends PaletteSidePanel {

	public AssetSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.ASSET);
	}

	@Override
	public void loadTool() {
		editor.getToolBox().setToolsWithSelector();
	}

	@Override
	public void drawTool(int mXIn, int mYIn) {
		super.drawTool(mXIn, mYIn);
	}

	@Override
	public void onAction(IActionObject object, Object... args) {
	}
	
}
