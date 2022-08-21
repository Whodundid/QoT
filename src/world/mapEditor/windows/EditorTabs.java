package world.mapEditor.windows;

import engine.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import engine.windowLib.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import world.mapEditor.MapEditorScreen;

public class EditorTabs extends TabbedContainer {
	
	private MapEditorScreen editor;
	private ContainerTab map, draw;
	
	public EditorTabs(IWindowObject<?> parent, MapEditorScreen editorIn, double x, double y, double w, double h) {
		super(parent, x, y, w, h);
		editor = editorIn;
	}
	
	@Override
	public void initChildren() {
		map = new MapSettingsTab(this, editor);
		draw = new DrawSettingsTab(this, editor);
		
		addTab(draw);
		addTab(map);
		
		super.initChildren();
	}
	
}
