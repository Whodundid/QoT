package envision.game.world.worldEditor.windows;

import envision.debug.DebugSettings;
import envision.engine.assets.WindowTextures;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowCheckBox;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;

public class DrawSettingsTab extends ContainerTab {

	//========
    // Fields
    //========
    
	private MapEditorScreen editor;
	
	private WindowScrollList list;
	
	private WindowLabel distX, distY;
	private WindowButton incDistX, decDistX;
	private WindowButton incDistY, decDistY;
	private WindowCheckBox drawMapBorders, drawCenterPosition, drawEntities, drawRegions;
	private WindowCheckBox drawEntityOutlines, drawEntityHitBoxes, drawWallBox, drawFlatWalls;
	private WindowCheckBox drawTileGrid;
	private WindowCheckBox lockToTileGrid;
	
	//==============
    // Constructors
    //==============
	
	public DrawSettingsTab(EditorTabs parent, MapEditorScreen editorIn) {
		super(parent, "Draw");
		editor = editorIn;
		tabTextColor = EColors.seafoam.intVal;
	}
	
	//===========
    // Overrides
    //===========
	
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
			final int checkX = 340;
			final int gap = 5;
			
			drawMapBorders = new WindowCheckBox(list, checkX, 20, 50, 50);
			drawCenterPosition = new WindowCheckBox(list, checkX, drawMapBorders.endY + gap, 50, 50);
			drawEntities = new WindowCheckBox(list, checkX, drawCenterPosition.endY + gap, 50, 50);
			drawRegions = new WindowCheckBox(list, checkX, drawEntities.endY + gap, 50, 50);
			drawEntityOutlines = new WindowCheckBox(list, checkX, drawRegions.endY + gap, 50, 50);
			drawEntityHitBoxes = new WindowCheckBox(list, checkX, drawEntityOutlines.endY + gap, 50, 50);
			drawWallBox = new WindowCheckBox(list, checkX, drawEntityHitBoxes.endY + gap, 50, 50);
			drawFlatWalls = new WindowCheckBox(list, checkX, drawWallBox.endY + gap, 50, 50);
			drawTileGrid = new WindowCheckBox(list, checkX, drawFlatWalls.endY + gap, 50, 50);
			lockToTileGrid = new WindowCheckBox(list, checkX, drawTileGrid.endY + gap, 50, 50);
			
			final double mfh = FontRenderer.FONT_HEIGHT / 2; // mid font height
			
			var drawMapBordersLbl = new WindowLabel(list, drawMapBorders.endX + 20, drawMapBorders.midY - mfh, "Draw Map Borders");
			var drawCenterPositionLbl = new WindowLabel(list, drawCenterPosition.endX + 20, drawCenterPosition.midY - mfh, "Draw Screen Mid");
			var drawEntitiesLbl = new WindowLabel(list, drawEntities.endX + 20, drawEntities.midY - mfh, "Draw Entities");
			var drawRegionsLbl = new WindowLabel(list, drawRegions.endX + 20, drawRegions.midY - mfh, "Draw Regions");
			var drawEntityOutlinesLbl = new WindowLabel(list, drawEntityOutlines.endX + 20, drawEntityOutlines.midY - mfh, "Draw Entity Outlines");
			var drawEntityHitBoxesLbl = new WindowLabel(list, drawEntityHitBoxes.endX + 20, drawEntityHitBoxes.midY - mfh, "Draw Entity Hitboxes");
			var drawWallBoxLbl = new WindowLabel(list, drawWallBox.endX + 20, drawWallBox.midY - mfh, "Draw Wall Box");
			var drawFlatWallsLbl = new WindowLabel(list, drawFlatWalls.endX + 20, drawFlatWalls.midY - mfh, "Draw Flat Walls");
			var drawTileGridLbl = new WindowLabel(list, drawTileGrid.endX + 20, drawTileGrid.midY - mfh, "Draw Tile Grid");
			var drawLockGridLbl = new WindowLabel(list, lockToTileGrid.endX + 20, lockToTileGrid.midY - mfh, "Lock to Tile Grid");
			
			final var settings = editor.getSettings();
			
			drawMapBorders.setIsChecked(settings.drawMapBorders);
			drawCenterPosition.setIsChecked(settings.drawCenterPositionBox);
			drawEntities.setIsChecked(settings.drawEntities);
			drawRegions.setIsChecked(settings.drawRegions);
			drawEntityOutlines.setIsChecked(settings.drawEntityOutlines);
			drawEntityHitBoxes.setIsChecked(settings.drawEntityHitBoxes);
			drawWallBox.setIsChecked(settings.drawWallBox);
			drawFlatWalls.setIsChecked(settings.drawFlatWalls);
			drawTileGrid.setIsChecked(settings.drawTileGrid);
			lockToTileGrid.setIsChecked(settings.lockToTileGrid);
			
			list.addObjectToList(drawMapBorders, drawCenterPosition, drawEntities, drawRegions);
			list.addObjectToList(drawEntityOutlines, drawEntityHitBoxes, drawWallBox, drawFlatWalls);
			list.addObjectToList(drawTileGrid, lockToTileGrid);
			list.addObjectToList(drawMapBordersLbl, drawCenterPositionLbl, drawEntitiesLbl, drawRegionsLbl);
			list.addObjectToList(drawEntityOutlinesLbl, drawEntityHitBoxesLbl, drawWallBoxLbl, drawFlatWallsLbl);
			list.addObjectToList(drawTileGridLbl, drawLockGridLbl);
		}
		
		IActionObject.setActionReceiver(this, incDistX, incDistY, decDistX, decDistY);
		IActionObject.setActionReceiver(this, drawMapBorders, drawCenterPosition, drawEntities);
		IActionObject.setActionReceiver(this, drawRegions, drawEntityOutlines, drawEntityHitBoxes);
		IActionObject.setActionReceiver(this, drawWallBox, drawFlatWalls, drawTileGrid, lockToTileGrid);
		
		list.fitItemsInList();
		
		addObject(list);
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
		
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
		var s = editor.getSettings();
		
		if (object == drawMapBorders) s.drawMapBorders = !s.drawMapBorders;
		if (object == drawCenterPosition) s.drawCenterPositionBox = !s.drawCenterPositionBox;
		if (object == drawEntities) s.drawEntities = !s.drawEntities;
		if (object == drawRegions) s.drawRegions = !s.drawRegions;
		if (object == drawEntityOutlines) s.drawEntityOutlines = !s.drawEntityOutlines;
		if (object == drawEntityHitBoxes) s.drawEntityHitBoxes = !s.drawEntityHitBoxes;
		if (object == drawWallBox) s.drawWallBox = !s.drawWallBox;
		if (object == drawFlatWalls) s.drawFlatWalls = !s.drawFlatWalls;
		if (object == drawTileGrid) s.drawTileGrid = !s.drawTileGrid;
		if (object == lockToTileGrid) s.lockToTileGrid = !s.lockToTileGrid;
		
//		if (object == drawFlatWalls) {
//			s.drawFlatWalls = !s.drawFlatWalls;
//			s.drawTileGrid = false;
//		}
//		
//		if (object == drawTileGrid) {
//			s.drawTileGrid = !s.drawTileGrid;
//			if (s.drawTileGrid) s.drawFlatWalls = true;
//		}
		
		drawMapBorders.setIsChecked(s.drawMapBorders);
		drawCenterPosition.setIsChecked(s.drawCenterPositionBox);
		drawEntities.setIsChecked(s.drawEntities);
		drawRegions.setIsChecked(s.drawRegions);
		drawEntityOutlines.setIsChecked(s.drawEntityOutlines);
		drawEntityHitBoxes.setIsChecked(s.drawEntityHitBoxes);
		drawWallBox.setIsChecked(s.drawWallBox);
		drawFlatWalls.setIsChecked(s.drawFlatWalls);
		drawTileGrid.setIsChecked(s.drawTileGrid);
		lockToTileGrid.setIsChecked(s.lockToTileGrid);
		
		DebugSettings.drawFlatWalls = s.drawFlatWalls;
		DebugSettings.drawTileGrid = s.drawTileGrid;
	}
	
}
