package world.mapEditor.windows;

import assets.textures.WindowTextures;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.basicObjects.WindowLabel;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import main.QoT;
import world.mapEditor.MapEditorScreen;

public class EditorSettingsWindow extends WindowParent {
	
	MapEditorScreen editor;
	
	WindowButton ok, close;
	
	WindowLabel distX, distY;
	WindowButton incDistX, decDistX;
	WindowButton incDistY, decDistY;
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
	public void initChildren() {
		defaultHeader(this);
		
		close = new WindowButton(this, startX + 3, endY - 38, 150, 35, "Close");
		
		distX = new WindowLabel(this, startX + 10, startY + 10, "Draw Dist X: " + editor.drawDistX);
		incDistX = new WindowButton(this, distX.startX, distX.startY + 30, 50, 50);
		decDistX = new WindowButton(this, incDistX.endX + 6, distX.startY + 30, 50, 50);
		
		incDistX.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decDistX.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		distY = new WindowLabel(this, startX + 10, incDistX.endY + 10, "Draw Dist Y: " + editor.drawDistY);
		incDistY = new WindowButton(this, distY.startX, distY.startY + 30, 50, 50);
		decDistY = new WindowButton(this, incDistY.endX + 6, distY.startY + 30, 50, 50);
		
		incDistY.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decDistY.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		addChild(distX, incDistX, decDistX);
		addChild(distY, incDistY, decDistY);
		
		addChild(close);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX + 1, startY, endX - 1, endY - 1, EColors.dgray);
		
		distX.setString("Draw Dist X: " + editor.drawDistX);
		distY.setString("Draw Dist Y: " + editor.drawDistY);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == close) fileUpAndClose();
		if (object == incDistX) editor.drawDistX++;
		if (object == incDistY) editor.drawDistY++;
		if (object == decDistX) editor.drawDistX--;
		if (object == decDistY) editor.drawDistY--;
	}
	
}
