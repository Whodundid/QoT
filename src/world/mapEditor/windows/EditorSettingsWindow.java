package world.mapEditor.windows;

import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import main.QoT;
import world.mapEditor.MapEditorScreen;

public class EditorSettingsWindow extends WindowParent {
	
	private MapEditorScreen editor;
	private EditorTabs tabs;
	
	public EditorSettingsWindow(MapEditorScreen editorIn) {
		super(editorIn);
		editor = editorIn;
	}
	
	@Override
	public void initWindow() {
		setSize(QoT.getWidth() / 2, QoT.getHeight() / 2);
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
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
	}
	
}
