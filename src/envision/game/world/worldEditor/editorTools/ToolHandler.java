package envision.game.world.worldEditor.editorTools;

import envision.engine.inputHandlers.Mouse;
import envision.game.world.GameWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorTools.tools.Tool_Brush;
import envision.game.world.worldEditor.editorTools.tools.Tool_Eraser;
import envision.game.world.worldEditor.editorTools.tools.Tool_EyeDropper;
import envision.game.world.worldEditor.editorTools.tools.Tool_Line;
import envision.game.world.worldEditor.editorTools.tools.Tool_MagicWand;
import envision.game.world.worldEditor.editorTools.tools.Tool_Move;
import envision.game.world.worldEditor.editorTools.tools.Tool_PaintBucket;
import envision.game.world.worldEditor.editorTools.tools.Tool_Pencil;
import envision.game.world.worldEditor.editorTools.tools.Tool_Place;
import envision.game.world.worldEditor.editorTools.tools.Tool_Region;
import envision.game.world.worldEditor.editorTools.tools.Tool_Selector;
import envision.game.world.worldEditor.editorTools.tools.Tool_Shape;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.boxes.BoxList;

public class ToolHandler {

	MapEditorScreen editor;
	
	private int button = -1;
	private int lastMx = -1, lastMy = -1;
	
	public enum ToolEvent { PRESS, RELEASE, UPDATE; }
	
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
	private final Tool_Place placeTool;
	
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
		placeTool = new Tool_Place(editor);
	}
	
	//---------------
	// Event Methods
	//---------------
	
	public void drawCurrentTool(double x, double y, double w, double h) {
		switch (editor.getSettings().getCurrentTool()) {
		case SELECTOR: selectorTool.drawTool(x, y, w, h); break;
		case ADD_REGION: regionTool.drawTool(x, y, w, h); break;
		case BRUSH: break;
		case ERASER: break; //always press
		case EYEDROPPER: break; //always press
		case LINE: break;
		case MAGICWAND: break; //always press
		case MOVE: break;
		case PAINTBUCKET: break;
		case PENCIL: break;
		case RECTSELECT: break;
		case SHAPE: break;
		case PLACE: placeTool.drawTool(x, y, w, h); break;
		default: break;
		}
	}
	
	public void handleToolPress(int buttonIn) {
		EditorToolType tool = editor.getSettings().getCurrentTool();
		if (tool != null) {
			button = buttonIn;
			handleTool(tool, ToolEvent.PRESS);
		}
	}
	
	public void handleToolRelease(int buttonIn) {
		EditorToolType tool = editor.getSettings().getCurrentTool();
		if (tool != null) {
			button = buttonIn;
			handleTool(tool, ToolEvent.RELEASE);
		}
	}
	
	public void handleToolUpdate() {
		EditorToolType tool = editor.getSettings().getCurrentTool();
		if (tool != null && Mouse.isAnyButtonDown()) {
			int mX = Mouse.getMx();
			int mY = Mouse.getMy();
			
			if (lastMx != mX || lastMy != mY) {
				lastMx = mX;
				lastMy = mY;
				handleTool(tool, ToolEvent.UPDATE);
			}
		}
	}
	
	private void handleTool(EditorToolType toolIn, ToolEvent event) {
		if (button < 0) return;
		
		switch (toolIn) {
		case SELECTOR: selectorTool.distributeEvent(event, button); break;
		case ADD_REGION: regionTool.distributeEvent(event, button); break;
		case BRUSH: break;
		case ERASER: eraserTool.distributeEvent(event, button); break; //always press
		case EYEDROPPER: eyeDropperTool.distributeEvent(event, button); break; //always press
		case LINE: break;
		case MAGICWAND: break; //always press
		case MOVE: break;
		case PAINTBUCKET: break;
		case PENCIL: pencilTool.distributeEvent(event, button); break;
		case RECTSELECT: break;
		case SHAPE: break;
		case PLACE: placeTool.distributeEvent(event, button); break;
		default: break;
		}
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
				w.setTileAt(WorldTile.randVariant(toSet), x, y);
				
				floodFill(w, x + 1, y, toReplace, toSet);
				floodFill(w, x - 1, y, toReplace, toSet);
				floodFill(w, x, y + 1, toReplace, toSet);
				floodFill(w, x, y - 1, toReplace, toSet);
			}
		}
	}
	
	/** Recursively finds all adjacent tiles matching 'toFind' and adds them to the given 'area'. */
	private void floodSelect(GameWorld w, int x, int y, WorldTile toFind, BoxList<Integer, Integer> area) {
		
	}
	
}
