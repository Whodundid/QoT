package envision.game.world.worldEditor.editorParts.topHeader;

import envision.Envision;
import envision.engine.screens.ScreenLevel;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.EditorWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.windows.EditorSettingsWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import qot.assets.textures.window.WindowTextures;

public class EditorScreenTopHeader extends WindowObject {
	
	MapEditorScreen editor;
	WindowButton settings;
	WindowButton<SidePanelType> terrain, assets, regions, scripts;
	private final EArrayList<WindowButton> toolButtons = new EArrayList();
	
	public EditorScreenTopHeader(MapEditorScreen editorIn) {
		editor = editorIn;
		init(editor, 0, 0, Envision.getWidth(), 34);
	}
	
	@Override
	public void initChildren() {
		settings = new WindowButton(this, startX + 3, startY + 3, 28, 28);
		terrain = new WindowButton(this, settings.endX + 3, startY + 3, 140, 28, "Terrain");
		assets = new WindowButton(this, terrain.endX + 3, startY + 3, 140, 28, "Assets");
		regions = new WindowButton(this, assets.endX + 3, startY + 3, 140, 28, "Regions");
		scripts = new WindowButton(this, regions.endX + 6, startY + 3, 240, 28, "Script Editor");
		
		settings.setButtonTexture(WindowTextures.settings);
		
		toolButtons.clear();
		toolButtons.add(terrain, assets, regions, scripts);
		
		terrain.setGenericObject(SidePanelType.TERRAIN);
		assets.setGenericObject(SidePanelType.ASSET);
		regions.setGenericObject(SidePanelType.REGION);
		scripts.setGenericObject(SidePanelType.SCRIPTS);
		
		WindowButton.setBackgroundColor(EColors.mgray, terrain, assets, regions, scripts);
		WindowButton.setStringColor(EColors.chalk, terrain, assets, regions, scripts);
		WindowButton.setSelectedColor(EColors.green, terrain, assets, regions, scripts);
		
		addObject(settings);
		addObject(terrain, scripts, assets, regions);
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		double div = scripts.endX + 2;
		
		drawRect(EColors.black); //background
		drawRect(startX, startY, scripts.endX + 3, endY - 1, EColors.dgray); //inner
		drawRect(regions.endX + 2, startY, regions.endX + 4, endY, EColors.black); //divider
		drawRect(div, startY, div + 2, endY, EColors.black); //divider
		drawRect(div + 2, startY, endX, endY - 1, EColors.pdgray); //map stuff
		
		EditorWorld world = editor.getEditorWorld();
		
		if (world != null) {
			double endName = drawString("Map: " + world.getWorldName(), scripts.endX + 25, startY + 7, EColors.lime);
			double endSize = drawString("Size: (" + world.getWidth() + ", " + world.getHeight() + ")", endName + 30, startY + 7, EColors.aquamarine);
			double endMouse = drawString("Mouse: (" + (editor.getWorldMX() + 1) + ", " + (editor.getWorldMY() + 1) + ")", endSize + 30, startY + 7, EColors.lorange);
			/*double endZoom = */drawString("Zoom: x" + editor.getCameraZoom(), endMouse + 30, startY + 7);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == settings) Envision.displayWindow(ScreenLevel.SCREEN, new EditorSettingsWindow(editor));
		if (object == terrain) terrain();
		if (object == assets) assets();
		if (object == regions) regions();
		if (object == scripts) scripts();
	}
	
	public void updateTool() {
		SidePanel panel = editor.getSidePanel().getCurrentPanel();
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
			EditorSidePanel sp = editor.getSidePanel();
			SidePanel p = sp.getCurrentPanel();
			if (val && p != null && p.getType() != typeIn) {
				sp.setCurrentPanel(typeIn, false);
			}
		}
		//return focus to editor
		editor.requestFocus();
	}
	
}
