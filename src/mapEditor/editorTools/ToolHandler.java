package mapEditor.editorTools;

import assets.worldTiles.WorldTile;
import gameSystems.mapSystem.GameWorld;
import mapEditor.MapEditorScreen;
import mapEditor.editorTools.tools.Tool_Brush;
import mapEditor.editorTools.tools.Tool_Eraser;
import mapEditor.editorTools.tools.Tool_EyeDropper;
import mapEditor.editorTools.tools.Tool_Line;
import mapEditor.editorTools.tools.Tool_MagicWand;
import mapEditor.editorTools.tools.Tool_Move;
import mapEditor.editorTools.tools.Tool_PaintBucket;
import mapEditor.editorTools.tools.Tool_Pencil;
import mapEditor.editorTools.tools.Tool_Region;
import mapEditor.editorTools.tools.Tool_Selector;
import mapEditor.editorTools.tools.Tool_Shape;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;

public class ToolHandler {

	MapEditorScreen editor;
	
	private StorageBox<Integer, Integer> oldPoint = new StorageBox(-1, -1);
	private StorageBox<Integer, Integer> clickPoint = new StorageBox(-1, -1);
	
	private int button = -1;
	private boolean pressed = false;
	private boolean held = false;
	
	//-------
	// Tools
	//-------
	
	private final Tool_Brush brushTool;
	private final Tool_Eraser eraserTool;
	private final Tool_EyeDropper eyeDropperTool;
	private final Tool_Line lineTool;
	private final Tool_MagicWand magicWandTool;
	private final Tool_Move moveTool;
	private final Tool_PaintBucket paintBucketTool;
	private final Tool_Pencil pencilTool;
	private final Tool_Region regionTool;
	private final Tool_Selector selectorTool;
	private final Tool_Shape shapeTool;
	
	//--------------
	// Constructors
	//--------------
	
	public ToolHandler(MapEditorScreen editorIn) {
		editor = editorIn;
		
		brushTool = new Tool_Brush(editor);
		eraserTool = new Tool_Eraser(editor);
		eyeDropperTool = new Tool_EyeDropper(editor);
		lineTool = new Tool_Line(editor);
		magicWandTool = new Tool_MagicWand(editor);
		moveTool = new Tool_Move(editor);
		paintBucketTool = new Tool_PaintBucket(editor);
		pencilTool = new Tool_Pencil(editor);
		regionTool = new Tool_Region(editor);
		selectorTool = new Tool_Selector(editor);
		shapeTool = new Tool_Shape(editor);
	}
	
	//---------------
	// Event Methods
	//---------------
	
	public void handleToolPress(EditorToolType toolIn, int buttonIn) {
		button = buttonIn;
		held = false;
		pressed = true;
		handleTool(toolIn);
	}
	
	public void handleToolRelease(EditorToolType toolIn, int buttonIn) {
		button = buttonIn;
		held = false;
		pressed = false;
		oldPoint.setValues(-1, -1);
		handleTool(toolIn);
	}
	
	public void handleToolUpdate(EditorToolType toolIn) {
		held = true;
		handleTool(toolIn);
	}
	
	private void handleTool(EditorToolType toolIn) {
		switch (toolIn) {
		case SELECTOR: selector(); break;
		case REGION: region(); break;
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
	
	private void selector() {
		
	}
	
	private void region() {
		
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
			//editor.getHotbar().setCurrent(editor.getTileHoveringOver());
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
			/*
			EditorItem item = editor.getCurItem();
			
			if (item != null && item.isTile()) {
				GameWorld w = editor.getWorld();
				
				int sX = editor.getWorldMX();
				int sY = editor.getWorldMY();
				
				if (inWorld(sX, sY, w)) {
					//the starting tile
					WorldTile tile = editor.getTileHoveringOver();
					
					if (!testTile(tile, item.getTile())) {
						floodFill(w, sX, sY, tile, item.getTile());
					}
					
				} //posValid
			}
			*/
		} //pressed
	}
	
	private void pencil() {
		if (pressed && (held && !oldPoint.compare(editor.getWorldMX(), editor.getWorldMY()))) {
			oldPoint.setValues(editor.getWorldMX(), editor.getWorldMY());
			
			/*
			EditorItem item = editor.getCurItem();
			if (item != null) {
				editor.setTileAtMouse(WorldTile.randVariant(item.getTile()));
			}
			*/
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
				w.setTileAt(x, y, WorldTile.randVariant(toSet));
				
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
