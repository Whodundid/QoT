package world.mapEditor.windows;

import assets.textures.window.WindowTextures;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowCheckBox;
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
	WindowCheckBox drawMapBorders, drawCenterPosition, drawEntities, drawRegions;
	WindowCheckBox drawEntityHitBoxes, drawWallBox;
	
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
		
		addChild(close);
		
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
		
		{
			var checkX = decDistX.endX + 175;
			var gap = 5;
			
			drawMapBorders = new WindowCheckBox(this, checkX, startY + 10, 50, 50);
			drawCenterPosition = new WindowCheckBox(this, checkX, drawMapBorders.endY + gap, 50, 50);
			drawEntities = new WindowCheckBox(this, checkX, drawCenterPosition.endY + gap, 50, 50);
			drawRegions = new WindowCheckBox(this, checkX, drawEntities.endY + gap, 50, 50);
			drawEntityHitBoxes = new WindowCheckBox(this, checkX, drawRegions.endY + gap, 50, 50);
			drawWallBox = new WindowCheckBox(this, checkX, drawEntityHitBoxes.endY + gap, 50, 50);
			
			var drawMapBordersLbl = new WindowLabel(this, drawMapBorders.endX + 20, drawMapBorders.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Map Borders");
			var drawCenterPositionLbl = new WindowLabel(this, drawCenterPosition.endX + 20, drawCenterPosition.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Screen Mid");
			var drawEntitiesLbl = new WindowLabel(this, drawEntities.endX + 20, drawEntities.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Entities");
			var drawRegionsLbl = new WindowLabel(this, drawRegions.endX + 20, drawRegions.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Regions");
			var drawEntityHitBoxesLbl = new WindowLabel(this, drawEntityHitBoxes.endX + 20, drawEntityHitBoxes.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Entity Outlines");
			var drawWallBoxLbl = new WindowLabel(this, drawWallBox.endX + 20, drawWallBox.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Wall Box");
			
			drawMapBorders.setIsChecked(editor.getSettings().drawMapBorders);
			drawCenterPosition.setIsChecked(editor.getSettings().drawCenterPositionBox);
			drawEntities.setIsChecked(editor.getSettings().drawEntities);
			drawRegions.setIsChecked(editor.getSettings().drawRegions);
			drawEntityHitBoxes.setIsChecked(editor.getSettings().drawEntityHitBoxes);
			drawWallBox.setIsChecked(editor.getSettings().drawWallBox);
			
			addChild(drawMapBorders, drawCenterPosition, drawEntities, drawRegions, drawEntityHitBoxes, drawWallBox);
			addChild(drawMapBordersLbl, drawCenterPositionLbl, drawEntitiesLbl, drawRegionsLbl, drawEntityHitBoxesLbl, drawWallBoxLbl);
		}

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
		
		//this is garbage and you know it..
		updateCheckBoxes(object);
	}
	
	// this can be made ***sooooooo*** much better...
	private void updateCheckBoxes(IActionObject object) {
		if (object == drawMapBorders) editor.getSettings().drawMapBorders = !editor.getSettings().drawMapBorders;
		if (object == drawCenterPosition) editor.getSettings().drawCenterPositionBox = !editor.getSettings().drawCenterPositionBox;
		if (object == drawEntities) editor.getSettings().drawEntities = !editor.getSettings().drawEntities;
		if (object == drawRegions) editor.getSettings().drawRegions = !editor.getSettings().drawRegions;
		if (object == drawEntityHitBoxes) editor.getSettings().drawEntityHitBoxes = !editor.getSettings().drawEntityHitBoxes;
		if (object == drawWallBox) editor.getSettings().drawWallBox = !editor.getSettings().drawWallBox;
		
		drawMapBorders.setIsChecked(editor.getSettings().drawMapBorders);
		drawCenterPosition.setIsChecked(editor.getSettings().drawCenterPositionBox);
		drawEntities.setIsChecked(editor.getSettings().drawEntities);
		drawRegions.setIsChecked(editor.getSettings().drawRegions);
		drawEntityHitBoxes.setIsChecked(editor.getSettings().drawEntityHitBoxes);
		drawWallBox.setIsChecked(editor.getSettings().drawWallBox);
	}
	
}
