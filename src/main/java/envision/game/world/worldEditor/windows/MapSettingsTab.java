package envision.game.world.worldEditor.windows;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowObjects.basicObjects.WindowTextBox;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;
import eutil.misc.Direction;
import qot.assets.textures.window.WindowTextures;

public class MapSettingsTab extends ContainerTab {

	//--------
	// Fields
	//--------
	
	private MapEditorScreen editor;
	private EditorTabs tabbedContainer;
	private WindowScrollList list;
	
	private WindowLabel mapWidth, mapHeight;
	private WindowButton incWidth, decWidth;
	private WindowButton incHeight, decHeight;
	private WindowTextBox widthBox, heightBox;
	
	private WindowButton iN, iNE, iE, iSE, iS, iSW, iW, iNW;
	private WindowButton dN, dNE, dE, dSE, dS, dSW, dW, dNW;
	
	//--------------
	// Constructors
	//--------------
	
	public MapSettingsTab(EditorTabs parent, MapEditorScreen editorIn) {
		super(parent, "Map");
		editor = editorIn;
		tabbedContainer = parent;
		tabTextColor = EColors.seafoam.intVal;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		var dims = getTabDims();
		
		//list = new WindowScrollList(this, dims.startX, dims.startY, dims.width, dims.height);
		//list.setBackgroundColor(EColors.pdgray);
		
		var x = dims.midX + 100;
		var y = dims.midY - 50;
		
		mapWidth = new WindowLabel(this, x, y, "Map Width: " + editor.getEditorWorld().getWidth());
		mapHeight = new WindowLabel(this, x, y + 100, "Map Height: " + editor.getEditorWorld().getHeight());
//		incWidth = new WindowButton(this, mapWidth.startX, mapWidth.startY + 30, 50, 50);
//		decWidth = new WindowButton(this, incWidth.endX + 6, mapWidth.startY + 30, 50, 50);
		
		addObject(mapWidth, mapHeight);
		
		buildMapResizer();
		
//		incWidth.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
//		decWidth.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
//		
//		incHeight = new WindowButton(list, mapHeight.startX, mapHeight.startY + 30, 50, 50);
//		decHeight = new WindowButton(list, incHeight.endX + 6, mapHeight.startY + 30, 50, 50);
//		
//		incHeight.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
//		decHeight.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
//		
//		list.addObjectToList(mapWidth, incWidth, decWidth);
//		list.addObjectToList(mapHeight, incHeight, decHeight);
//		
//		IActionObject.setActionReceiver(this, incWidth, decWidth, incHeight, decHeight);
//		
//		list.fitItemsInList();
		
		//addObject(list);
	}
	
	private void buildMapResizer() {
		var dims = getTabDims();
		
		midX = dims.midX - 200;
		midY = dims.midY;
		
		int midW = 300;
		int midH = 300;
		int midW2 = midW / 2;
		int midH2 = midH / 2;
		int dist = 18;
		
		// North
		iN = button(midX, midY - midH2 - dist, true);
		dN = button(midX, midY - midH2 + dist, false);
		
		// North East
		iNE = button(midX + midW2 + dist, midY - midH2 - dist, true);
		dNE = button(midX + midW2 - dist, midY - midH2 + dist, false);
		
		// East
		iE = button(midX + midW2 + dist, midY, true);
		dE = button(midX + midW2 - dist, midY, false);
		
		// South East
		iSE = button(midX + midW2 + dist, midY + midH2 + dist, true);
		dSE = button(midX + midW2 - dist, midY + midH2 - dist, false);
		
		// South
		iS = button(midX, midY + midH2 + dist, true);
		dS = button(midX, midY + midH2 - dist, false);
		
		// South West
		iSW = button(midX - midW2 - dist, midY + midH2 + dist, true);
		dSW = button(midX - midW2 + dist, midY + midH2 - dist, false);
		
		// West
		iW = button(midX - midW2 - dist, midY, true);
		dW = button(midX - midW2 + dist, midY, false);
		
		// North West
		iNW = button(midX - midW2 - dist, midY - midH2 - dist, true);
		dNW = button(midX - midW2 + dist, midY - midH2 + dist, false);
		
		addObject(iN, iNE, iE, iSE, iS, iSW, iW, iNW);
		addObject(dN, dNE, dE, dSE, dS, dSW, dW, dNW);
	}
	
	private WindowButton button(double x, double y, boolean inc) {
		var b = new WindowButton(this, x, y, 30, 30);
		if (inc) b.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		else b.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		b.setActionReceiver(this);
		return b;
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
		if (this != tabbedContainer.getSelectedTab()) return;
		
		var dims = getTabDims();
		
		midX = dims.midX - 200;
		midY = dims.midY;
		
		int midW = 300;
		int midH = 300;
		int midW2 = midW / 2;
		int midH2 = midH / 2;
		int dist = 17;
		
		int spacer = 5;
		int buttonWidth = 30;
		int buttonHeight = 30;
		
		var bsx = midX - midW2 - dist - spacer;
		var bsy = midY - midH2 - dist - spacer;
		var bex = midX + midW2 + dist + spacer + buttonWidth;
		var bey = midY + midH2 + dist + spacer + buttonHeight;
		
		drawRect(bsx, bsy, bex, bey, EColors.black);
		
		var msx = midX - midW2 + buttonWidth / 2;
		var msy = midY - midH2 + buttonHeight / 2;
		var mex = midX + midW2 + buttonWidth / 2;
		var mey = midY + midH2 + buttonHeight / 2;
		
		drawRect(msx, msy, mex, mey, EColors.blue);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
		
		// North ----------------------------------------------------------------------------
		if (object == iN) 	{								resizeH(Direction.N, 1);	}
		if (object == dN)	{								resizeH(Direction.N, -1);	}
		// North East -----------------------------------------------------------------------
		if (object == iNE) 	{	resizeW(Direction.E, 1); 	resizeH(Direction.N, 1); 	}
		if (object == dNE) 	{	resizeW(Direction.E, -1);	resizeH(Direction.N, -1); 	}
		// East -----------------------------------------------------------------------------
		if (object == iE)	{	resizeW(Direction.E, 1);								}
		if (object == dE)	{	resizeW(Direction.E, -1);								}
		// South East -----------------------------------------------------------------------
		if (object == iSE)	{	resizeW(Direction.E, 1);	resizeH(Direction.S, 1);	}
		if (object == dSE)	{	resizeW(Direction.E, -1);	resizeH(Direction.S, -1);	}
		// South ----------------------------------------------------------------------------
		if (object == iS)	{								resizeH(Direction.S, 1);	}
		if (object == dS)	{								resizeH(Direction.S, -1);	}
		// South West -----------------------------------------------------------------------
		if (object == iSW)	{	resizeW(Direction.W, 1);	resizeH(Direction.S, 1);	}
		if (object == dSW)	{	resizeW(Direction.W, -1);	resizeH(Direction.S, -1);	}
		// West -----------------------------------------------------------------------------
		if (object == iW)	{	resizeW(Direction.W, 1);								}
		if (object == dW)	{	resizeW(Direction.W, -1);								}
		// North West -----------------------------------------------------------------------
		if (object == iNW)	{	resizeW(Direction.W, 1);	resizeH(Direction.N, 1);	}
		if (object == dNW)	{	resizeW(Direction.W, -1);	resizeH(Direction.N, -1);	}
	}
	
	private void resizeW(Direction dir, int w) {
		editor.getEditorWorld().expandWorld(dir, w);
		mapWidth.setString("Map Width: " + editor.getEditorWorld().getWidth());
	}
	
	private void resizeH(Direction dir, int h) {
		editor.getEditorWorld().expandWorld(dir, h);
		mapHeight.setString("Map Height: " + editor.getEditorWorld().getHeight());
	}
	
}
