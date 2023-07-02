package envision.game.world.worldEditor.windows;

import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.TabbedContainer;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.world.worldEditor.MapEditorScreen;

public class EditorTabs extends TabbedContainer {
	
	private MapEditorScreen editor;
	private ContainerTab map, draw;
	
	public EditorTabs(IWindowObject parent, MapEditorScreen editorIn, double x, double y, double w, double h) {
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
