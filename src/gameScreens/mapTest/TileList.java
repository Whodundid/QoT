package gameScreens.mapTest;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.WindowObject;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.mapSystem.worldTiles.WorldTiles;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class TileList extends WindowObject {
	
	EArrayList<WindowButton<WorldTile>> tiles = new EArrayList();
	WindowButton up, down;
	WorldTile currentTile;
	int yPos = 0;
	int total = 4;
	
	public TileList(WindowObject parent, double xIn, double yIn) {
		init(parent, xIn, yIn, 150, 300);
	}
	
	@Override
	public void initObjects() {
		up = new WindowButton(this, startX + 2, startY + 2, width - 4, 20);
		down = new WindowButton(this, startX + 2, endY - 22, width - 4, 20);
		
		buildList();
		
		addObject(up, down);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.pdgray);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == up) {
			
		}
		
		if (object == down) {
			
		}
		
		if (object instanceof WindowButton b && b.getStoredObject() instanceof WorldTile) {
			currentTile = (WorldTile) b.getStoredObject();
		}
	}
	
	public WorldTile getCurrentTile() { return currentTile; }
	
	public TileList setCurrentTile(WorldTile tileIn) {
		//
		return this;
	}
	
	private void buildList() {
		EArrayList<WorldTile> tileList = WorldTiles.getTiles();
		
		for (int i = yPos, j = 0; i < tileList.size() && j < total; i++) {
			WorldTile t = tileList.get(i);
			
			if (t.hasTexture()) {
				double bH = (down.startY - up.endY - (2 * total) - 2) / total;
				
				WindowButton<WorldTile> b = new WindowButton(this, startX + 2, up.endY + 2 + (j * bH) + (j * 2), width - 4, bH) {
					@Override
					public void drawObject(int mXIn, int mYIn) {
						super.drawObject(mXIn, mYIn);
						
						if (getStoredObject() == currentTile) {
							drawHRect(EColors.red, 1);
						}
					}
				};
				
				b.setTextures(t.getTexture(), t.getTexture());
				b.setStoredObject(t);
				b.setDrawStretched(false);
				
				tiles.add(b);
				j++;
				
				if (currentTile == null) { currentTile = t; }
			}
		}
		
		tiles.forEach(b -> addObject(b));
	}
	
}
