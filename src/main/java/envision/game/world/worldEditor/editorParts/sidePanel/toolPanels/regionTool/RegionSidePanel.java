package envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.regionTool;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextAreaLine;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.Region;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.toolBox.ToolCategory;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class RegionSidePanel extends SidePanel {
	
	WindowButton edit, delete;
	WindowTextArea<Region> regionList;
	
	Box2<Integer, Integer> clickPos = new Box2(-1, -1);
	
	public RegionSidePanel(EditorSidePanel panelIn, MapEditorScreen editorIn) {
		super(panelIn, editorIn, SidePanelType.REGION);
	}
	
	@Override
	public void loadTool() {
		int bHeight = 35;
		
		edit = new WindowButton(panel, startX + 10, endY - 10 - bHeight, (width - 25) / 2, bHeight, "Edit");
		delete = new WindowButton(panel, edit.endX + 5, endY - 10 - bHeight, (width - 25) / 2, bHeight, "Delete");
		
		regionList = new WindowTextArea(panel, startX + 10, startY + 10, width - 20, edit.startY - startY - 20);
		regionList.setDrawLineNumbers(true);
		
		IWindowObject.setEnabled(false, edit, delete);
		
		panel.addObject(regionList);
		panel.addObject(edit, delete);
		
		loadRegions();
		
		editor.getToolBox().setToolsWithSelector(ToolCategory.from("Region", EditorToolType.ADD_REGION));
		editor.getSettings().setCurrentTool(EditorToolType.SELECTOR);
	}
	
	public void loadRegions() {
		regionList.clear();
		
		IGameWorld world = editor.getEditorWorld();
		EList<Region> regions = world.getRegionData();
		for (Region r : regions) {
			TextAreaLine<Region> line = new TextAreaLine(regionList, r.getName()) {
				@Override
				public void onDoubleClick() {
					super.onDoubleClick();
					edit(this);
				}
			};
			
			regionList.addTextLine(line);
			line.setGenericObject(r);
			line.setTextColor(r.getColor());
		}
	}
	
	@Override
	public void drawTool(int mXIn, int mYIn) {
		IWindowObject.setEnabled(regionList.getCurrentLine() != null, edit, delete);
		
		drawRect(EColors.black);
		drawRect(EColors.dgray, 1);
	}
	
	@Override
	public void onAction(IActionObject object, Object... args) {
		if (object == edit) edit();
		if (object == delete) delete();
		
		if (object instanceof WindowDialogueBox b) {
			if (args.length >= 1 && args[0] instanceof Integer code &&
				b.getGenericObject() instanceof TextAreaLine line &&
				line.getGenericObject() instanceof Region)
			{
				try {
					switch (code) {
					case 1: onDelete(line); break;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//-----------------------------------------------------------------------
	
	private void edit() { edit(null); }
	private void edit(TextAreaLine<Region> lineIn) {
		if (lineIn == null) {
			TextAreaLine<Region> line = regionList.getCurrentLine();
			if (line == null) return;
			
			Region r = line.getGenericObject();
			if (r == null) throw new RuntimeException("Region on line: '" + line + "' is null!");
			
			openEditor(line);
		}
		else {
			openEditor(lineIn);
		}
	}
	
	private void delete() {	
		TextAreaLine<Region> line = regionList.getCurrentLine();
		if (line == null) return;
		
		Region r = line.getGenericObject();
		if (r == null) throw new RuntimeException("Region on line: '" + line + "' is null!");
		
		openDeleter(line);
	}
	
	private void onDelete(TextAreaLine lineIn) {
		Region r = (Region) lineIn.getGenericObject();
		
		GameWorld world = editor.getActualWorld();
		world.getRegionData().remove(r);
		
		IGameWorld editorWorld = editor.getEditorWorld();
		editorWorld.getRegionData().remove(r);
		
		regionList.deleteLine(lineIn);
	}
	
	//-----------------------------------------------------------------------
	
	private void openEditor(TextAreaLine<Region> lineIn) {
		IWindowParent p = panel.getTopParent().displayWindow(new RegionEditWindow(lineIn));
		panel.getTopParent().setFocusLockObject(p);
	}
	
	private void openDeleter(TextAreaLine<Region> lineIn) {
		WindowDialogueBox deletingBox = new WindowDialogueBox(panel) {
			
			WindowButton yes, cancel;
			
			@Override
			public void initWindow() {
				setGenericObject(lineIn);
				setTitle("Delete Region");
				setTitleColor(EColors.gray.intVal);
				//setSize(getMinWidth(), getMinHeight());
			}
			
			@Override
			public void initChildren() {
				super.initChildren();
				
				setMessage(EColors.orange + "Are you sure? " + EColors.lgray + "(" + lineIn.getGenericObject().getName() + ")");
				
				double bw = ENumUtil.clamp((width - 10) / 3, 100, 200);
				double g = width / 30;
				
				yes = new WindowButton(this, midX - g - bw, endY - 50, bw, 28, "Delete");
				cancel = new WindowButton(this, midX + g, endY - 50, bw, 28, "Cancel");
				
				yes.setStringColor(EColors.lred);
				cancel.setStringColor(EColors.green);
				
				addObject(yes, cancel);
			}
			
			@Override
			public void actionPerformed(IActionObject object, Object... args) {
				if (object == yes) { performAction(1); close(); }
				if (object == cancel) { close(); }
			}
			
		};
		
		panel.getTopParent().displayWindow(deletingBox);
		panel.getTopParent().setFocusLockObject(deletingBox);
	}
	
	//-----------------------------------------------------------------------
	
}