package envisionEngine.gameEngine.world.worldEditor.editorParts.toolBox;

import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

import java.util.List;

import envisionEngine.gameEngine.world.worldEditor.editorTools.EditorToolType;
import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;

public class ToolCategory {
	
	public String name;
	public EArrayList<EditorToolType> tools = new EArrayList();
	
	public ToolCategory(String nameIn) {
		name = nameIn;
	}
	
	public ToolCategory addToolType(List<EditorToolType> typesIn) {
		tools.addAll(typesIn);
		return this;
	}
	
	public ToolCategory addToolType(EditorToolType... typesIn) {
		tools.addA(typesIn);
		return this;
	}
	
	public String getName() { return name; }
	public EArrayList<EditorToolType> getTypes() { return tools; }
	
	public EArrayList<WindowButton<EditorToolType>> buildButtons(EditorToolBox parentIn) {
		EArrayList<WindowButton<EditorToolType>> buttons = new EArrayList();
		
		for (EditorToolType t : tools) {
			WindowButton<EditorToolType> b = new WindowButton(parentIn) {
				@Override
				public void drawObject(int mXIn, int mYIn) {
					super.drawObject(mXIn, mYIn);
					
					//compare this tool button's type to the active one
					if (getGenericObject() == parentIn.editor.getSettings().getCurrentTool()) {
						drawHRect(EColors.lgreen, 1);
					}
				}
			};
			
			b.setGenericObject(t);
			buttons.add(b);
		}
		
		return buttons;
	}
	
	public static ToolCategory from(String nameIn, EditorToolType... typesIn) {
		return new ToolCategory(nameIn).addToolType(typesIn);
	}
	
}
