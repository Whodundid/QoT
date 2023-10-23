package envision.game.dialog;

import envision.Envision;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.WindowParent;
import envision.game.entities.Entity;
import eutil.colors.EColors;

public class DialogueBox extends WindowParent {
	
	public void speak(Entity currentEntity, EntityDialogue dialogue) {
		double xPos = currentEntity.startX;
		double yPos = currentEntity.startY;
		
		//init(xPos, yPos, 10, 10);
		
	}
	
	@Override
	public void initWindow() {
		setGuiSize(Envision.getWidth() / 2, Envision.getHeight() / 2);
		setMinDims(300, 300);
		
		setMaximizable(true);
		setResizeable(true);
		setMinimizable(false);
		
		setObjectName("DialogueBox Testing");
	}
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.white);
		drawRect(startX + 1, startY, endX - 1, endY - 1, EColors.dgray);
		super.drawObject(mXIn, mYIn);
	}
	
	
	
}
