package envision.game.world.worldEditor.editorParts.toolBox;

import static envision.game.world.worldEditor.editorTools.EditorToolType.*;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowSlider;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowObjects.basicObjects.WindowRect;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class EditorToolBox extends WindowObject {
	
	public final ToolCategory selector = ToolCategory.from("Selection", SELECTOR, RECTSELECT);
	
	MapEditorScreen editor;
	//EArrayList<ToolCategory> tools = new EArrayList();
	EList<WindowButton<EditorToolType>> toolButtons = EList.newList();
	
	/** The physical square size of each tool button. */
	int toolSize = 40;
	/** The gap in between each tool button. */
	int gapSize = 4;
	/** The maximum number of tools displayed on each row. */
	int rowWidth = 2;
	
	WindowSlider sizeSlider;
	
	//==============
	// Constructors
	//==============
	
	public EditorToolBox(MapEditorScreen in) {
		editor = in;
		init(in, 5, editor.getTopHeader().endY + 15);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void initChildren() {
		//position each tool within the list and actually create and apply the dimensions
		buildTools(selector);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.dgray, 1);
		
		IWindowObject obj = getTopParent().getFocusedObject();
		if (obj != null) {
			if (obj == this || obj.isChildOf(this)) {
				obj.transferFocus(getTopParent());
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof WindowButton && object.getGenericObject() instanceof EditorToolType) {
			EditorToolType t = (EditorToolType) object.getGenericObject();
			editor.getSettings().setCurrentTool(t);
		}
	}
	
	public EditorToolBox setTools(ToolCategory... categories) {
		buildTools(categories);
		WindowHeader header = new WindowHeader(this, false, 10);
		header.setDrawTitle(false);
		header.setDrawParentFocus(false);
		
		addObject(header);
		return this;
	}
	
	public EditorToolBox setToolsWithSelector(ToolCategory... categories) {
		buildTools(EUtil.add(selector, categories));
		WindowHeader header = new WindowHeader(this, false, 10);
		header.setDrawTitle(false);
		header.setDrawParentFocus(false);
		
		addObject(header);
		return this;
	}
	
	private void buildTools(ToolCategory... categories) {
		//getObjects().removeAll(toolButtons);
		getChildren().clear();
		toolButtons.clear();
		
		int w = (rowWidth * toolSize) + rowWidth + 3;
		int h = 2; //initial height value
		
		//position each tool
		double curY = startY + 2;
		for (int c = 0; c < categories.length; c++) {
			ToolCategory cat = categories[c];
			if (cat == null) continue;
			int rows = (int) (Math.ceil((double) cat.getTypes().size() / (double) rowWidth));
			
			EList<WindowButton<EditorToolType>> buttons = cat.buildButtons(this);
			
			for (int i = 0; i < buttons.size(); i++) {
				WindowButton<EditorToolType> button = buttons.get(i);
				EditorToolType theTool = button.getGenericObject();
				double xPos = i % rowWidth; //x offset multiple starting at 0
				double yPos = i / rowWidth; //y offset multiple starting at 0
				
				double bx = startX + 2 + (toolSize * xPos) + xPos;
				double by = curY + (toolSize * yPos) + yPos;
				
				//apply tool resources to button
				button.setButtonTexture(theTool.texture);
				button.setHoverText(theTool.hoverText);
				
				//add to list first then dimension
				addObject(button);
				button.setDimensions(bx, by, toolSize, toolSize);
			}
			
			//update height values
			curY += (rows * toolSize) + rows;
			h += (rows * toolSize) + rows;
			
			//add a small divider in between categories
			if (c < categories.length - 1) {
				WindowRect r = new WindowRect(this, 0, 0, 0, 0, 0xff303030);
				WindowRect t = new WindowRect(this, 0, 0, 0, 0, EColors.vdgray);
				addObject(t);
				r.setDimensions(startX + 1, curY, w - 2, 3);
				t.setDimensions(startX + 1, curY + 1, w - 2, 1);
				
				curY += 4;
				h += 4;
			}
		}
		
		//add space to match top
		h += 1;
		
		//assign our dimensions
		setSize(w, h);
	}
	
	public MapEditorScreen getEditor() { return editor; }
	
}