package envision.gameEngine.world.worldEditor.windows;

import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.actionObjects.WindowCheckBox;
import envision.windowLib.windowObjects.advancedObjects.WindowScrollList;
import envision.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.windowLib.windowObjects.basicObjects.WindowLabel;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.assets.textures.window.WindowTextures;

public class DrawSettingsTab extends ContainerTab {

	//--------
	// Fields
	//--------
	
	private MapEditorScreen editor;
	
	private WindowScrollList list;
	
	private WindowLabel distX, distY;
	private WindowButton incDistX, decDistX;
	private WindowButton incDistY, decDistY;
	private WindowCheckBox drawMapBorders, drawCenterPosition, drawEntities, drawRegions;
	private WindowCheckBox drawEntityHitBoxes, drawWallBox, drawFlatWalls, drawTileGrid;
	
	//--------------
	// Constructors
	//--------------
	
	public DrawSettingsTab(EditorTabs parent, MapEditorScreen editorIn) {
		super(parent, "Draw");
		editor = editorIn;
		tabTextColor = EColors.seafoam.intVal;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		var dims = getTabDims();
		
		list = new WindowScrollList(this, dims.startX, dims.startY, dims.width, dims.height);
		list.setBackgroundColor(EColors.pdgray);
		
		distX = new WindowLabel(list, 20, 20, "Draw Dist X: " + editor.drawDistX);
		incDistX = new WindowButton(list, distX.startX, distX.startY + 30, 50, 50);
		decDistX = new WindowButton(list, incDistX.endX + 6, distX.startY + 30, 50, 50);
		
		incDistX.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decDistX.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		distY = new WindowLabel(list, 20, incDistX.endY + 20, "Draw Dist Y: " + editor.drawDistY);
		incDistY = new WindowButton(list, distY.startX, distY.startY + 30, 50, 50);
		decDistY = new WindowButton(list, incDistY.endX + 6, distY.startY + 30, 50, 50);
		
		incDistY.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decDistY.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		list.addObjectToList(distX, incDistX, decDistX);
		list.addObjectToList(distY, incDistY, decDistY);
		
		{
			var checkX = 340;
			var gap = 5;
			
			drawMapBorders = new WindowCheckBox(list, checkX, 20, 50, 50);
			drawCenterPosition = new WindowCheckBox(list, checkX, drawMapBorders.endY + gap, 50, 50);
			drawEntities = new WindowCheckBox(list, checkX, drawCenterPosition.endY + gap, 50, 50);
			drawRegions = new WindowCheckBox(list, checkX, drawEntities.endY + gap, 50, 50);
			drawEntityHitBoxes = new WindowCheckBox(list, checkX, drawRegions.endY + gap, 50, 50);
			drawWallBox = new WindowCheckBox(list, checkX, drawEntityHitBoxes.endY + gap, 50, 50);
			drawFlatWalls = new WindowCheckBox(list, checkX, drawWallBox.endY + gap, 50, 50);
			drawTileGrid = new WindowCheckBox(list, checkX, drawFlatWalls.endY + gap, 50, 50);
			
			var drawMapBordersLbl = new WindowLabel(list, drawMapBorders.endX + 20, drawMapBorders.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Map Borders");
			var drawCenterPositionLbl = new WindowLabel(list, drawCenterPosition.endX + 20, drawCenterPosition.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Screen Mid");
			var drawEntitiesLbl = new WindowLabel(list, drawEntities.endX + 20, drawEntities.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Entities");
			var drawRegionsLbl = new WindowLabel(list, drawRegions.endX + 20, drawRegions.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Regions");
			var drawEntityHitBoxesLbl = new WindowLabel(list, drawEntityHitBoxes.endX + 20, drawEntityHitBoxes.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Entity Outlines");
			var drawWallBoxLbl = new WindowLabel(list, drawWallBox.endX + 20, drawWallBox.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Wall Box");
			var drawFlatWallsLbl = new WindowLabel(list, drawFlatWalls.endX + 20, drawFlatWalls.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Flat Walls");
			var drawTileGridLbl = new WindowLabel(list, drawTileGrid.endX + 20, drawTileGrid.midY - FontRenderer.FONT_HEIGHT / 2, "Draw Tile Grid");
			
			drawMapBorders.setIsChecked(editor.getSettings().drawMapBorders);
			drawCenterPosition.setIsChecked(editor.getSettings().drawCenterPositionBox);
			drawEntities.setIsChecked(editor.getSettings().drawEntities);
			drawRegions.setIsChecked(editor.getSettings().drawRegions);
			drawEntityHitBoxes.setIsChecked(editor.getSettings().drawEntityHitBoxes);
			drawWallBox.setIsChecked(editor.getSettings().drawWallBox);
			drawFlatWalls.setIsChecked(editor.getSettings().drawFlatWalls);
			drawTileGrid.setIsChecked(editor.getSettings().drawTileGrid);
			
			list.addObjectToList(drawMapBorders, drawCenterPosition, drawEntities, drawRegions);
			list.addObjectToList(drawEntityHitBoxes, drawWallBox, drawFlatWalls, drawTileGrid);
			list.addObjectToList(drawMapBordersLbl, drawCenterPositionLbl, drawEntitiesLbl, drawRegionsLbl);
			list.addObjectToList(drawEntityHitBoxesLbl, drawWallBoxLbl, drawFlatWallsLbl, drawTileGridLbl);
		}
		
		IActionObject.setActionReceiver(this, incDistX, incDistY, decDistX, decDistY);
		IActionObject.setActionReceiver(this, drawMapBorders, drawCenterPosition, drawEntities);
		IActionObject.setActionReceiver(this, drawRegions, drawEntityHitBoxes, drawWallBox);
		IActionObject.setActionReceiver(this, drawFlatWalls, drawTileGrid);
		
		list.fitItemsInList();
		
		addObject(list);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		distX.setString("Draw Dist X: " + editor.drawDistX);
		distY.setString("Draw Dist Y: " + editor.drawDistY);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
		
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
		if (object == drawFlatWalls) editor.getSettings().drawFlatWalls = !editor.getSettings().drawFlatWalls;
		if (object == drawTileGrid) editor.getSettings().drawTileGrid = !editor.getSettings().drawTileGrid;
		
		drawMapBorders.setIsChecked(editor.getSettings().drawMapBorders);
		drawCenterPosition.setIsChecked(editor.getSettings().drawCenterPositionBox);
		drawEntities.setIsChecked(editor.getSettings().drawEntities);
		drawRegions.setIsChecked(editor.getSettings().drawRegions);
		drawEntityHitBoxes.setIsChecked(editor.getSettings().drawEntityHitBoxes);
		drawWallBox.setIsChecked(editor.getSettings().drawWallBox);
		drawFlatWalls.setIsChecked(editor.getSettings().drawFlatWalls);
		drawTileGrid.setIsChecked(editor.getSettings().drawTileGrid);
	}
	
}
