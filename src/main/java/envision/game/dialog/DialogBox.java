package envision.game.dialog;

import envision.engine.windows.windowTypes.WindowObject;
import envision.game.entities.Entity;

public class DialogBox extends WindowObject {
	
	private Entity leftEntity;
	private Entity rightEntity;
	
	private EntityDialog currentlyDisplayedDialog;
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
	}
	
	
	
}
