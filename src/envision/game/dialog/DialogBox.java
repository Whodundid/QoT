package envision.game.dialog;

import envision.engine.windows.windowTypes.WindowObject;
import envision.game.objects.entities.Entity;

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
