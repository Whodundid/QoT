package envisionEngine.dialogSystem;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.windowLib.windowTypes.WindowObject;

public class DialogBox extends WindowObject {
	
	private Entity leftEntity;
	private Entity rightEntity;
	
	private EntityDialog currentlyDisplayedDialog;
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	
	
}
