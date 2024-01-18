package envision.game.world.worldEditor.editorTools;

import envision.engine.inputHandlers.Mouse;
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
import envision.game.world.worldEditor.editorTools.tools.Tool_RectSelect;
import envision.game.world.worldEditor.editorTools.tools.Tool_Region;
import envision.game.world.worldEditor.editorTools.tools.Tool_Selector;
import envision.game.world.worldEditor.editorTools.tools.Tool_Shape;

public class ToolHandler {

    //========
    // Fields
    //========
    
	MapEditorScreen editor;
	
	private int button = -1;
	private int lastMx = -1, lastMy = -1;
	
	public enum ToolEvent { PRESS, RELEASE, UPDATE; }
	
	//=======
	// Tools
	//=======
	
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
	private final Tool_RectSelect rectSelectTool;
	
	//==============
    // Constructors
    //==============
	
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
		rectSelectTool = new Tool_RectSelect(editor);
	}
	
	//===============
	// Event Methods
	//===============
	
	public void drawCurrentTool(double x, double y, double w, double h) {
		final EditorTool tool = getCurrentTool();
		if (tool != null) tool.drawTool(x, y, w, h);
	}
	
	public EditorTool getCurrentTool() {
	    return getToolForType(editor.getSettings().getCurrentTool());
	}
	
    public EditorTool getToolForType(EditorToolType type) {
        if (type == null) return null;
        return switch (type) {
        case SELECTOR -> selectorTool;
        case ADD_REGION -> regionTool;
        case BRUSH -> brushTool;
        case ERASER -> eraserTool;
        case EYEDROPPER -> eyeDropperTool;
        case LINE -> lineTool;
        case MAGICWAND -> magicWandTool;
        case MOVE -> moveTool;
        case PAINTBUCKET -> paintBucketTool;
        case PENCIL -> pencilTool;
        case RECTSELECT -> rectSelectTool;
        case SHAPE -> shapeTool;
        case PLACE -> placeTool;
        default -> null;
        };
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
		
		var tool = getToolForType(toolIn);
		if (tool != null) tool.distributeEvent(event, button);
		
//		switch (toolIn) {
//		case SELECTOR: selectorTool.distributeEvent(event, button); break;
//		case ADD_REGION: regionTool.distributeEvent(event, button); break;
//		case BRUSH: brushTool.distributeEvent(event, button); break;
//		case ERASER: eraserTool.distributeEvent(event, button); break; //always press
//		case EYEDROPPER: eyeDropperTool.distributeEvent(event, button); break; //always press
//		case LINE: break;
//		case MAGICWAND: break; //always press
//		case MOVE: break;
//		case PAINTBUCKET: paintBucketTool.distributeEvent(event, button); break;
//		case PENCIL: pencilTool.distributeEvent(event, button); break;
//		case RECTSELECT: break;
//		case SHAPE: break;
//		case PLACE: placeTool.distributeEvent(event, button); break;
//		default: break;
//		}
	}
	
}
