package envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.scriptTool;

import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;

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
