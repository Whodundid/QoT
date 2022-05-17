package world.mapEditor.windows;

import engine.QoT;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import world.mapEditor.MapEditorScreen;

public class EditorSettingsWindow extends WindowParent {
	
	MapEditorScreen editor;
	
	WindowButton ok, close;
	
	WindowButton drawMapBorders, drawCenterPosition, drawEntities, drawRegions;
	WindowButton drawEntityHitBoxes;
	
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
		
		setObjectName("Settings");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		close = new WindowButton(this, startX + 3, endY - 38, 150, 35, "Close");
		
		addObject(close);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX + 1, startY, endX - 1, endY - 1, EColors.dgray);
		
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == close) { fileUpAndClose(); }
	}
	
}
