package gameScreens.mapEditor.editorScreen;

import gameScreens.mapEditor.editorScreen.tileTools.EditorTileTool;
import gameScreens.mapEditor.editorScreen.util.EditorItem;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.textureSystem.GameTexture;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

public class ToolHandler {

	MapEditorScreen editor;
	
	private int button = -1;
	private boolean pressed = false;
	private boolean held = false;
	
	public ToolHandler(MapEditorScreen editorIn) {
		editor = editorIn;
	}
	
	public void handleToolPress(EditorTileTool toolIn, int buttonIn) {
		button = buttonIn;
		held = false;
		pressed = true;
		handleTool(toolIn);
		
	}
	
	public void handleToolRelease(EditorTileTool toolIn, int buttonIn) {
		button = buttonIn;
		held = false;
		pressed = false;
		handleTool(toolIn);
	}
	
	public void handleToolUpdate(EditorTileTool toolIn) {
		held = true;
		handleTool(toolIn);
	}
	
	private void handleTool(EditorTileTool toolIn) {
		switch (toolIn) {
		case BRUSH: brush(); break;
		case ERASER: eraser(); break; //always press
		case EYEDROPPER: eyeDropper(); break; //always press
		case LINE: line(); break;
		case MAGICWAND: magicWand(); break; //always press
		case MOVE: move(); break;
		case PAINTBUCKET: paintBucket(); break;
		case PENCIL: pencil(); break;
		case RECTSELECT: rectSelect(); break;
		case SHAPE: shape(); break;
		default: break;
		}
	}
	
	private void brush() {
		
	}
	
	private void eraser() {
		if (pressed && held) {
			editor.getWorld().setTileAt((int) editor.getWorldMX(), (int) editor.getWorldMY(), null);
		}
	}
	
	private void eyeDropper() {
		if (pressed) {
			editor.hotbar.setCurrent(editor.getTileHoveringOver());
		}
	}
	
	private void line() {
		
	}
	
	private void magicWand() {
		
	}
	
	private void move() {
		
	}
	
	private void paintBucket() {
		if (pressed) {
			EditorItem item = editor.getCurItem();
			
			if (item != null && item.isTile()) {
				GameWorld w = editor.getWorld();
				
				int sX = editor.getWorldMX();
				int sY = editor.getWorldMY();
				
				if (inWorld(sX, sY, w)) {
					//the starting tile
					WorldTile tile = editor.getTileHoveringOver();
					
					if (!testTile(tile, item.getTile())) {
						StorageBoxHolder<Integer, Integer> area = new StorageBoxHolder();
						area.add(sX, sY);
						
						floodFill(w, sX, sY, tile, item.getTile());
						
						//set all of the found tiles to the current hotbar tile
						for (StorageBox<Integer, Integer> t : area) {
							
						}
					}
					
				} //posValid
			}
		} //pressed
	}
	
	private void pencil() {
		if (pressed && held) {
			EditorItem item = editor.getCurItem();
			if (item != null) {
				WorldTile t = item.getTile();
				
				GameTexture tex = t.getTexture().getRandVariant();
				//System.out.println(tex.getChildID());
				t.setTexture(tex);
				
				editor.getWorld().setTileAt((int) editor.getWorldMX(), (int) editor.getWorldMY(), t);
			}
		}
	}
	
	private void rectSelect() {
		
	}
	
	private void shape() {
		
	}
	
	//----------------
	
	/** Returns true if the (x, y) coordinate is actually in the given world. */
	private boolean inWorld(int x, int y, GameWorld w) {
		return x >= 0 && x < w.getWidth() && y >= 0 && y < w.getHeight();
	}
	
	/** Returns true if the given two tiles match. */
	private boolean testTile(WorldTile a, WorldTile b) {
		if (a != null && b != null) { return a.getID() == b.getID(); }
		else if (a == null && b == null) { return true; }
		return false;
	}
	
	/** Recursively finds all adjacent tiles matching 'toReplace' with 'toSet'. */
	private void floodFill(GameWorld w, int x, int y, WorldTile toReplace, WorldTile toSet) {
		if (inWorld(x, y, w)) {
			WorldTile t = w.getTileAt(x, y);
			
			if (testTile(toReplace, t)) {
				w.setTileAt(x, y, toSet);
				
				floodFill(w, x + 1, y, toReplace, toSet);
				floodFill(w, x - 1, y, toReplace, toSet);
				floodFill(w, x, y + 1, toReplace, toSet);
				floodFill(w, x, y - 1, toReplace, toSet);
			}
		}
	}
	
	/** Recursively finds all adjacent tiles matching 'toFind' and adds them to the given 'area'. */
	private void floodSelect(GameWorld w, int x, int y, WorldTile toFind, StorageBoxHolder<Integer, Integer> area) {
		
	}
	
}
