package envision.game.world.worldEditor.windows;

import envision.engine.internal.windows.windowObjects.advanced.tabPane.WindowTabPane;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.game.world.worldEditor.MapEditorScreen;

public class EditorTabs extends WindowTabPane {
    
    private MapEditorScreen editor;
    private IWindowObject map, draw;
    
    public EditorTabs(IWindowObject parent, MapEditorScreen editorIn, double x, double y, double w, double h) {
        super(parent, x, y, w, h);
        editor = editorIn;
    }
    
    @Override
    public void initChildren() {
        super.initChildren();

        map = new MapSettingsTab(this, editor);
        draw = new DrawSettingsTab(this, editor);
        
        addTab("Draw", draw);
        addTab("Map", map);
    }
    
}
