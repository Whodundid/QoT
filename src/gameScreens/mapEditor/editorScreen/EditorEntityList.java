package gameScreens.mapEditor.editorScreen;

import assets.entities.Entity;
import envisionEngine.eWindow.windowObjects.advancedObjects.textArea.WindowTextArea;
import envisionEngine.eWindow.windowObjects.basicObjects.WindowImageBox;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;

public class EditorEntityList extends WindowObject {
	
	WindowImageBox displayBox;
	WindowTextArea entityList;
	
	Entity curEntity;
	
	public EditorEntityList(MapEditorScreen screen) {
		
	}
	
	@Override
	public void initObjects() {
		displayBox = new WindowImageBox(this, startX + 2, startY + 2, endX - 2, 200);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	private void buildList() {
		
		
		
	}
	
}
