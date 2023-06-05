package envision.engine.windows.bundledWindows;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.ActionObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IUseScreenLocation;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class ScreenLocationSelector<E> extends ActionObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected IUseScreenLocation obj;
	protected WindowButton<ScreenLocation> bLeft, bRight, tLeft, tRight, center, custom, chatDraw;
	protected int heightRatio = 0, widthRatio = 0;
	public String drawName = "";
	
	//--------------
	// Constructors
	//--------------
	
	public ScreenLocationSelector(IWindowObject parentIn, IUseScreenLocation objIn, double posX, double posY, double size) {
		init(parentIn, posX, posY, size, size - 10);
		obj = objIn;
		
		heightRatio = (int) (height * 0.75);
		widthRatio = (int) (width * 0.5);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		bLeft = new WindowButton(this, startX + 4, startY + heightRatio - 19, 23, 15, "BL");
		bRight = new WindowButton(this, endX - 27, startY + heightRatio - 19, 23, 15, "BR");
		tLeft = new WindowButton(this, startX + 4, startY + 4, 23, 15, "TL");
		tRight = new WindowButton(this, endX - 27, startY + 4, 23, 15, "TR");
		center = new WindowButton(this, startX + width / 2 - 11, startY + (heightRatio / 2) - 7, 23, 15, "C");
		custom = new WindowButton(this, startX + width / 2 - (95 / 2), endY, 95, 16, "Custom location");
		
		bLeft.setGenericObject(ScreenLocation.BOT_LEFT);
		bRight.setGenericObject(ScreenLocation.BOT_RIGHT);
		tLeft.setGenericObject(ScreenLocation.TOP_LEFT);
		tRight.setGenericObject(ScreenLocation.TOP_RIGHT);
		center.setGenericObject(ScreenLocation.CENTER);
		custom.setGenericObject(ScreenLocation.CUSTOM);
		
		addObject(bLeft, bRight, tLeft, tRight, center, custom);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(startX, startY, endX, startY + heightRatio, EColors.black); //border
		drawRect(startX + widthRatio - (widthRatio / 16), startY + heightRatio, startX + widthRatio + (widthRatio / 16), endY - (heightRatio / 8), EColors.black); //pole
		drawRect(startX + widthRatio - (widthRatio / 2), endY - (heightRatio / 8), startX + widthRatio + (widthRatio / 2), endY - (heightRatio / 8) + 3, EColors.black); //base
		drawRect(startX + 3, startY + 3, endX - 3, startY + heightRatio - 3, 0xffC9FFFF); //screen
		
		drawStringS("Select a location to draw " + drawName + ".", midX - strWidth("Select a location to draw " + drawName + ".") / 2, startY - heightRatio / 5 - 12, 0xb2b2b2);
		String msg = "";
		switch (obj.getScreenLocation()) {
		case BOT_LEFT: msg = "Bottom Left"; bLeft.drawRect(EColors.lred, -1); break;
		case BOT_RIGHT: msg = "Bottom Right"; bRight.drawRect(EColors.lred, -1); break;
		case TOP_LEFT: msg = "Top Left"; tLeft.drawRect(EColors.lred, -1); break;
		case TOP_RIGHT: msg = "Top Right"; tRight.drawRect(EColors.lred, -1); break;
		case CENTER: msg = "Center"; center.drawRect(EColors.lred, -1); break;
		case CUSTOM: msg = "Custom (" + obj.getLocation().getA() + ", " + obj.getLocation().getB() + ")"; break;
		default: msg = "Center"; break;
		}
		
		drawStringS("Current Location: ", midX - strWidth("Current Location: " + msg) / 2, startY - heightRatio / 5, 0xffd800);
		drawStringS(msg, midX - strWidth("Current Location: " + msg) / 2 + strWidth("Current Location: "), startY - heightRatio / 5, 0x00ff00);
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == bLeft) 	obj.setLocation(ScreenLocation.BOT_LEFT);
		if (object == bRight) 	obj.setLocation(ScreenLocation.BOT_RIGHT);
		if (object == tLeft) 	obj.setLocation(ScreenLocation.TOP_LEFT);
		if (object == tRight) 	obj.setLocation(ScreenLocation.TOP_RIGHT);
		if (object == center) 	obj.setLocation(ScreenLocation.CENTER);
		if (object == custom) 	getTopParent().displayWindow(obj.getScreenLocationGui());
		performAction();
	}
	
	//---------
	// Setters
	//---------
	
	public void setDisplayName(String nameIn) { drawName = nameIn; }
	
}
