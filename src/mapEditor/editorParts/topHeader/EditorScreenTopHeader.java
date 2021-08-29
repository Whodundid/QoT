package mapEditor.editorParts.topHeader;

import eutil.colors.EColors;
import eutil.storage.EArrayList;
import main.QoT;
import mapEditor.MapEditorScreen;
import mapEditor.editorParts.sidePanel.SidePanel;
import mapEditor.editorParts.sidePanel.SidePanelType;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IActionObject;
import world.GameWorld;

public class EditorScreenTopHeader extends WindowObject {
	
	MapEditorScreen editor;
	WindowButton<SidePanelType> terrain, assets, regions, scripts;
	private final EArrayList<WindowButton> toolButtons = new EArrayList();
	
	public EditorScreenTopHeader(MapEditorScreen editorIn) {
		editor = editorIn;
		init(editor, 0, 0, QoT.getWidth(), 34);
	}
	
	@Override
	public void initObjects() {
		terrain = new WindowButton(this, startX + 3, startY + 3, 140, 28, "Terrain");
		assets = new WindowButton(this, terrain.endX + 3, startY + 3, 140, 28, "Assets");
		regions = new WindowButton(this, assets.endX + 3, startY + 3, 140, 28, "Regions");
		scripts = new WindowButton(this, regions.endX + 6, startY + 3, 240, 28, "Script Editor");
		
		toolButtons.clear();
		toolButtons.add(terrain, assets, regions, scripts);
		
		terrain.setGenericObject(SidePanelType.TERRAIN);
		assets.setGenericObject(SidePanelType.ASSET);
		regions.setGenericObject(SidePanelType.REGION);
		scripts.setGenericObject(SidePanelType.SCRIPTS);
		
		WindowButton.setBackgroundColor(EColors.mgray, terrain, assets, regions, scripts);
		WindowButton.setStringColor(EColors.chalk, terrain, assets, regions, scripts);
		WindowButton.setSelectedColor(EColors.green, terrain, assets, regions, scripts);
		
		addObject(terrain, scripts, assets, regions);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		double div = scripts.endX + 2;
		
		drawRect(EColors.black); //background
		drawRect(startX, startY, scripts.endX + 3, endY - 1, EColors.dgray); //inner
		drawRect(regions.endX + 2, startY, regions.endX + 4, endY, EColors.black); //divider
		drawRect(div, startY, div + 2, endY, EColors.black); //divider
		drawRect(div + 2, startY, endX, endY - 1, EColors.pdgray); //map stuff
		
		GameWorld world = editor.getWorld();
		
		double endName = drawString("Map: " + world.getName(), scripts.endX + 25, startY + 7, EColors.lime);
		double endSize = drawString("Size: (" + world.getTileWidth() + ", " + world.getTileHeight() + ")", endName + 30, startY + 7, EColors.aquamarine);
		double endMouse = drawString("Mouse: (" + editor.getWorldMX() + ", " + editor.getWorldMY() + ")", endSize + 30, startY + 7, EColors.lorange);
		double endZoom = drawString("Zoom: x" + editor.getZoom(), endMouse + 30, startY + 7);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == terrain) { terrain(); }
		if (object == assets) { assets(); }
		if (object == regions) { regions(); }
		if (object == scripts) { scripts(); }
	}
	
	public void updateTool() {
		SidePanel panel = editor.getCurrentPanel();
		if (panel != null) {
			updateSelected(panel.getType());
		}
	}
	
	private void terrain() { updateSelected(SidePanelType.TERRAIN); }
	private void assets() { updateSelected(SidePanelType.ASSET); }
	private void regions() { updateSelected(SidePanelType.REGION); }
	private void scripts() { updateSelected(SidePanelType.SCRIPTS); }
	
	private void updateSelected(SidePanelType typeIn) {
		for (WindowButton<SidePanelType> b : toolButtons) {
			boolean val = b.getGenericObject() == typeIn;
			b.setDrawSelected(val);
			//only load if it is not already loaded
			if (val && editor.getCurrentPanel() != null && editor.getCurrentPanel().getType() != typeIn) {
				editor.getSidePanel().setActivePanel(typeIn);
			}
		}
		//return focus to editor
		editor.requestFocus();
	}
	
}
