package gameScreens.mapEditor.editorScreen;

import assets.textures.WindowTextures;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.advancedObjects.textArea.WindowTextArea;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTool;
import gameSystems.mapSystem.Region;
import renderUtil.EColors;
import storageUtil.EDimension;
import storageUtil.StorageBox;

public class RegionEditorMenu extends WindowParent {
	
	MapEditorScreen editor;
	
	WindowButton addRegion, rename, delete;
	WindowTextArea<Region> regionList;
	
	StorageBox<Integer, Integer> clickPos = new StorageBox(-1, -1);
	
	public RegionEditorMenu(MapEditorScreen editorIn) {
		super();
		editor = editorIn;
	}
	
	@Override
	public void initWindow() {
		setDimensions(250, 450);
		setResizeable(true);
		setMinDims(250, 250);
		setObjectName("Regions");
		
		getTopParent().registerListener(this);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		addRegion = new WindowButton(this, startX + 10, startY + 10, 35, 35);
		addRegion.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		
		regionList = new WindowTextArea(this, startX + 10, addRegion.endY + 10, width - 20, height - 110);
		
		rename = new WindowButton(this, startX + 10, regionList.endY + 10, (width - 25) / 2, 35, "Rename");
		delete = new WindowButton(this, rename.endX + 5, regionList.endY + 10, (width - 25) / 2, 35, "Delete");
		
		setEnabled(false, rename, delete);
		
		addObject(addRegion);
		addObject(regionList);
		addObject(rename, delete);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		setEnabled(regionList.getCurrentLine() != null, rename, delete);
		
		drawRect(EColors.black);
		drawRect(EColors.dgray, 1);
		
		drawString("Add Region", addRegion.endX + 15, addRegion.startY + 9);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == addRegion) {
			editor.setCurrentTool(EditorTool.REGION);
		}
		
		if (object == rename) {
			//Region r = regionList.getCurrentLine().getStoredObject();
		}
	}
	
	@Override
	public void onClosed() {
		getTopParent().unregisterListener(this);
	}
	
	public void createRegion(EDimension dims) {
		
	}
	
}
