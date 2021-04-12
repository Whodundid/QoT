package envisionEngine.eWindow.windowObjects.utilityObjects;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.ActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IUseScreenLocation;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import renderUtil.EColors;
import renderUtil.ScreenLocation;

//Author: Hunter Bragg

public class ScreenLocationSelector<E> extends ActionObject<E> {
	
	protected IUseScreenLocation obj;
	protected WindowButton<ScreenLocation> bLeft, bRight, tLeft, tRight, center, custom, chatDraw;
	protected int heightRatio = 0, widthRatio = 0;
	public String drawName = "";
	
	public ScreenLocationSelector(IWindowObject parentIn, IUseScreenLocation objIn, double posX, double posY, double size) {
		init(parentIn, posX, posY, size, size - 10);
		obj = objIn;
		
		heightRatio = (int) (height * 0.75);
		widthRatio = (int) (width * 0.5);
	}
	
	@Override
	public void initObjects() {
		bLeft = (WindowButton<ScreenLocation>) new WindowButton(this, startX + 4, startY + heightRatio - 19, 23, 15, "BL").setStoredObject(ScreenLocation.botLeft);
		bRight = (WindowButton<ScreenLocation>) new WindowButton(this, endX - 27, startY + heightRatio - 19, 23, 15, "BR").setStoredObject(ScreenLocation.botRight);
		tLeft = (WindowButton<ScreenLocation>) new WindowButton(this, startX + 4, startY + 4, 23, 15, "TL").setStoredObject(ScreenLocation.topLeft);
		tRight = (WindowButton<ScreenLocation>) new WindowButton(this, endX - 27, startY + 4, 23, 15, "TR").setStoredObject(ScreenLocation.topRight);
		center = (WindowButton<ScreenLocation>) new WindowButton(this, startX + width / 2 - 11, startY + (heightRatio / 2) - 7, 23, 15, "C").setStoredObject(ScreenLocation.center);
		custom = (WindowButton<ScreenLocation>) new WindowButton(this, startX + width / 2 - (95 / 2), endY, 95, 16, "Custom location").setStoredObject(ScreenLocation.custom);
		
		addObject(bLeft, bRight, tLeft, tRight, center, custom);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(startX, startY, endX, startY + heightRatio, EColors.black); //border
		drawRect(startX + widthRatio - (widthRatio / 16), startY + heightRatio, startX + widthRatio + (widthRatio / 16), endY - (heightRatio / 8), EColors.black); //pole
		drawRect(startX + widthRatio - (widthRatio / 2), endY - (heightRatio / 8), startX + widthRatio + (widthRatio / 2), endY - (heightRatio / 8) + 3, EColors.black); //base
		drawRect(startX + 3, startY + 3, endX - 3, startY + heightRatio - 3, 0xffC9FFFF); //screen
		
		drawStringS("Select a location to draw " + drawName + ".", midX - getStringWidth("Select a location to draw " + drawName + ".") / 2, startY - heightRatio / 5 - 12, 0xb2b2b2);
		String msg = "";
		switch (obj.getScreenLocation()) {
		case botLeft: msg = "Bottom Left"; bLeft.drawRect(EColors.lred, -1); break;
		case botRight: msg = "Bottom Right"; bRight.drawRect(EColors.lred, -1); break;
		case topLeft: msg = "Top Left"; tLeft.drawRect(EColors.lred, -1); break;
		case topRight: msg = "Top Right"; tRight.drawRect(EColors.lred, -1); break;
		case center: msg = "Center"; center.drawRect(EColors.lred, -1); break;
		case custom: msg = "Custom (" + obj.getLocation().getA() + ", " + obj.getLocation().getB() + ")"; break;
		default: msg = "Center"; break;
		}
		
		drawStringS("Current Location: ", midX - getStringWidth("Current Location: " + msg) / 2, startY - heightRatio / 5, 0xffd800);
		drawStringS(msg, midX - getStringWidth("Current Location: " + msg) / 2 + getStringWidth("Current Location: "), startY - heightRatio / 5, 0x00ff00);
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == bLeft) { obj.setLocation(ScreenLocation.botLeft); }
		if (object == bRight) { obj.setLocation(ScreenLocation.botRight); }
		if (object == tLeft) { obj.setLocation(ScreenLocation.topLeft); }
		if (object == tRight) { obj.setLocation(ScreenLocation.topRight); }
		if (object == center) { obj.setLocation(ScreenLocation.center); }
		if (object == custom) { getTopParent().displayWindow(obj.getScreenLocationGui()); }
		performAction();
	}
	
	public ScreenLocationSelector<E> setDisplayName(String nameIn) { drawName = nameIn; return this; }
	
}
