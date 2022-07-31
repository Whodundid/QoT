package world.mapEditor.editorTools;

import eutil.datatypes.BoxList;
import world.GameWorld;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorTools.tools.Tool_Brush;
import world.mapEditor.editorTools.tools.Tool_Eraser;
import world.mapEditor.editorTools.tools.Tool_EyeDropper;
import world.mapEditor.editorTools.tools.Tool_Line;
import world.mapEditor.editorTools.tools.Tool_MagicWand;
import world.mapEditor.editorTools.tools.Tool_Move;
import world.mapEditor.editorTools.tools.Tool_PaintBucket;
import world.mapEditor.editorTools.tools.Tool_Pencil;
import world.mapEditor.editorTools.tools.Tool_Region;
import world.mapEditor.editorTools.tools.Tool_Selector;
import world.mapEditor.editorTools.tools.Tool_Shape;
import world.worldTiles.WorldTile;

public class ToolHandler {

	MapEditorScreen editor;
	
	private int button = -1;
	
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
		if (tool != null) {
			handleTool(tool, ToolEvent.UPDATE);
		}
	}
	
	private void handleTool(EditorToolType toolIn, ToolEvent event) {
		switch (toolIn) {
		case SELECTOR: selectorTool.distributeEvent(event, button); break;
		case REGION: break;
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
				w.setTileAt(x, y, WorldTile.randVariant(toSet));
				
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
