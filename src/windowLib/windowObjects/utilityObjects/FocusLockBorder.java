package windowLib.windowObjects.utilityObjects;

import storageUtil.EDimension;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class FocusLockBorder extends WindowObject {
	
	protected long startTime = 0l;
	protected boolean first = false;
	protected boolean second = false;
	protected boolean drawingBorder = true;
	protected int borderColor = 0xffff2222;
	protected int preservedColor = 0;
	
	public FocusLockBorder(IWindowObject parentIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY, dim.width, dim.height);
			startTime = System.currentTimeMillis();
			//mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F));
		}
	}
	
	public FocusLockBorder(IWindowObject parentIn, double startXIn, double startYIn, double widthIn, double heightIn) {
		if (parentIn != null) {
			init(parentIn, startXIn, startYIn, widthIn, heightIn);
			startTime = System.currentTimeMillis();
			//mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F));
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (System.currentTimeMillis() - startTime >= 200) {
			//mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F));
			if (second) { parent.removeObject(this); drawingBorder = false; }
			if (first) { second = true; drawingBorder = true; }
			if (!first) { first = true; drawingBorder = false; }
			startTime = System.currentTimeMillis();
		}
		if (drawingBorder) { drawBorder(); }
	}
	
	protected void drawBorder() {
		drawRect(startX - 1, startY, startX, endY, borderColor);
		drawRect(startX - 1, startY - 1, endX + 1, startY, borderColor);
		drawRect(endX, startY, endX + 1, endY, borderColor);
		drawRect(startX - 1, endY + 1, endX + 1, endY, borderColor);
	}
	
}
