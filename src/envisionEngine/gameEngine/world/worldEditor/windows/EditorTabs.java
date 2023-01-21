package envisionEngine.gameEngine.world.worldEditor.windows;

import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envisionEngine.windowLib.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;

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
