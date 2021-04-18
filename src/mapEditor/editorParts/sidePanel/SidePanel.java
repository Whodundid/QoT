package mapEditor.editorParts.sidePanel;

import mapEditor.MapEditorScreen;
import windowLib.windowTypes.interfaces.IActionObject;
import windowLib.windowUtil.EGui;

public abstract class SidePanel extends EGui {
	
	protected EditorSidePanel panel;
	protected MapEditorScreen editor;
	public final SidePanelType type;
	
	protected SidePanel(EditorSidePanel panelIn, MapEditorScreen in, SidePanelType typeIn) {
		panel = panelIn;
		editor = in;
		type = typeIn;
		setDimensions(panel.getPanelDims()); //match dimensions of the panel
	}
	
	public abstract void loadTool();
	public abstract void drawTool(int mXIn, int mYIn);
	public abstract void onAction(IActionObject object, Object... args);
	
	public SidePanelType getType() { return type; }
	
}
