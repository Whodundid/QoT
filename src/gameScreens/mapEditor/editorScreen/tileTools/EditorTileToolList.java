package gameScreens.mapEditor.editorScreen.tileTools;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.advancedObjects.header.WindowHeader;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.MapEditorScreen;
import main.Game;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class EditorTileToolList extends WindowObject {
	
	MapEditorScreen editor;
	EArrayList<WindowButton<EditorTileTool>> tools = new EArrayList();
	
	WindowButton<EditorTileTool> rectangleSelect, move;
	WindowButton<EditorTileTool> magicWand, paintBucket;
	WindowButton<EditorTileTool> brush, pencil;
	WindowButton<EditorTileTool> eyeDropper, eraser;
	WindowButton<EditorTileTool> line, shape;
	
	WindowButton<EditorTileTool> curTool;
	EditorTileTool oldTool;
	
	int numTools = 10;
	double toolSize = 40;
	int toolWidth = 2;
	int gapSize = 4;
	
	public EditorTileToolList(MapEditorScreen in) {
		editor = in;
		
		//gap of 2 between each tool and a gap of 2 for each side
		double h = ((numTools / toolWidth) * toolSize) + ((numTools / toolWidth) * gapSize) + gapSize;
		double w = (toolWidth * toolSize) + (toolWidth * gapSize) + gapSize;
		
		init(in, 5, editor.getTopHeader().endY + 15, w, h);
	}
	
	@Override
	public void initObjects() {
		tools.clear();
		
		WindowHeader header = new WindowHeader(this, false, 10);
		header.setDrawTitle(false);
		header.setDrawParentFocus(false);
		
		rectangleSelect = new WindowButton(this, startX + gapSize, startY + gapSize, toolSize, toolSize);
		move = new WindowButton(this, rectangleSelect.endX + gapSize, startY + gapSize, toolSize, toolSize);
		
		magicWand = new WindowButton(this, startX + gapSize, rectangleSelect.endY + gapSize, toolSize, toolSize);
		paintBucket = new WindowButton(this, rectangleSelect.endX + gapSize, rectangleSelect.endY + gapSize, toolSize, toolSize);
		
		brush = new WindowButton(this, startX + gapSize, magicWand.endY + gapSize, toolSize, toolSize);
		pencil = new WindowButton(this, rectangleSelect.endX + gapSize, magicWand.endY + gapSize, toolSize, toolSize);
		
		eyeDropper = new WindowButton(this, startX + gapSize, brush.endY + gapSize, toolSize, toolSize);
		eraser = new WindowButton(this, rectangleSelect.endX + gapSize, brush.endY + gapSize, toolSize, toolSize);
		
		line = new WindowButton(this, startX + gapSize, eyeDropper.endY + gapSize, toolSize, toolSize);
		shape = new WindowButton(this, rectangleSelect.endX + gapSize, eyeDropper.endY + gapSize, toolSize, toolSize);
		
		applyValues(rectangleSelect, EditorTileTool.RECTSELECT);
		applyValues(move, EditorTileTool.MOVE);
		applyValues(magicWand, EditorTileTool.MAGICWAND);
		applyValues(paintBucket, EditorTileTool.PAINTBUCKET);
		applyValues(brush, EditorTileTool.BRUSH);
		applyValues(pencil, EditorTileTool.PENCIL);
		applyValues(eyeDropper, EditorTileTool.EYEDROPPER);
		applyValues(eraser, EditorTileTool.ERASER);
		applyValues(line, EditorTileTool.LINE);
		applyValues(shape, EditorTileTool.SHAPE);
		
		tools.add(rectangleSelect);
		tools.add(move);
		tools.add(magicWand);
		tools.add(paintBucket);
		tools.add(brush);
		tools.add(pencil);
		tools.add(eyeDropper);
		tools.add(eraser);
		tools.add(line);
		tools.add(shape);
		
		addObject(header);
		addObject(rectangleSelect, move);
		addObject(magicWand, paintBucket);
		addObject(brush, pencil);
		addObject(eyeDropper, eraser);
		addObject(line, shape);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.dgray, 1);
		
		EditorTileTool newTool = editor.getCurTileTool();
		if (newTool != EditorTileTool.NONE && oldTool != newTool) {
			oldTool = newTool;
			curTool = tools.getFirst(t -> t.getStoredObject() == newTool);
		}
		
		if (curTool != null) {
			curTool.drawRect(EColors.yellow, -1);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof WindowButton && object.getStoredObject() instanceof EditorTileTool) {
			EditorTileTool t = (EditorTileTool) object.getStoredObject();
			editor.setTileTool(t);
		}
	}
	
	private void applyValues(WindowButton<EditorTileTool> in, EditorTileTool value) {
		in.setStoredObject(value);
		in.setButtonTexture(value.texture);
		in.setHoverText(value.hoverText);
	}
	
}
