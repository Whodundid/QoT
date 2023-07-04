package envision.game.world.worldEditor.windows;

import envision.Envision;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;

public class EditorSettingsWindow extends WindowParent {
	
	private MapEditorScreen editor;
	private EditorTabs tabs;
	
	public EditorSettingsWindow(MapEditorScreen editorIn) {
		super(editorIn);
		editor = editorIn;
	}
	
	@Override
	public void initWindow() {
		setGuiSize(Envision.getWidth() / 2, Envision.getHeight() / 2);
		setMinDims(300, 300);
		setMaximizable(true);
		setResizeable(true);
		setMinimizable(false);
		
		setObjectName("Editor Settings");
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		tabs = new EditorTabs(this, editor, startX + 5, startY + 5, width - 10, height - 10);
		
		addObject(tabs);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX + 1, startY, endX - 1, endY - 1, EColors.dgray);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
	}
	
}
