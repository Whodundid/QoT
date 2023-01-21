package envision.gameEngine.world.worldEditor.windows;

import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.windowLib.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.windowLib.windowTypes.interfaces.IWindowObject;

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
