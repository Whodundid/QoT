package mapEditor.editorParts.toolBox;

import java.util.List;
import mapEditor.editorTools.EditorToolType;
import renderUtil.EColors;
import storageUtil.EArrayList;
import windowLib.windowObjects.actionObjects.WindowButton;

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
					
					if (getGenericObject() == parentIn.getEditor().getCurTileTool()) {
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
