package envision.game.world.worldEditor.editorParts.toolBox;

import java.util.List;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class ToolCategory {
	
	public String name;
	public EList<EditorToolType> tools = EList.newList();
	
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
	public EList<EditorToolType> getTypes() { return tools; }
	
	public EList<WindowButton<EditorToolType>> buildButtons(EditorToolBox parentIn) {
		EList<WindowButton<EditorToolType>> buttons = EList.newList();
		
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
