package envision.engine.notifications.util;

import envision.Envision;
import envision.engine.notifications.window.NotificationRCM;
import envision.engine.screens.ScreenLevel;
import envision.engine.windows.WindowSize;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import qot.assets.textures.window.WindowTextures;

//Author: Hunter Bragg

public abstract class NotificationObject extends WindowParent {

	protected String message = "";
	protected NotificationType type;
	protected WindowParent attentionObject = null;
	protected WindowButton close;
	protected boolean expires = true;
	protected boolean onlyDrawOnHud = false;
	boolean moveOut = false;
	boolean moving = false;
	boolean movingOut = false;
	long startTime = 0l;
	long birthTime = 0l;
	long timeOut = 4500l;
	double sX = -1;
	double v0 = -1;
	double a = -1;
	double dist = -1;
	double time = -1;
	
	protected NotificationObject(NotificationType typeIn) {
		super();
		type = typeIn;
	}
	
	@Override
	public void initChildren() {
		close = new WindowButton(this, endX - 10, startY + 2, 8, 8);
		close.setTextures(WindowTextures.close, WindowTextures.close_sel);
		
		close.setVisible(false);
		
		addObject(close);
	}

	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		if (onlyDrawOnHud && !Envision.developerDesktop.hasFocus()) close();
		
		drawRect(startX, startY, startX + 1, endY, 0xff000000); //left border
		drawRect(startX + 1, endY - 1, endX - 1, endY, 0xff000000); //bottom border
		drawRect(endX - 1, startY, endX, endY, 0xff000000); //right border
		drawRect(startX + 1, startY, endX - 1, startY + 1, 0xff000000); //top border
		
		int inColor = isMouseInsideGui(mXIn, mYIn) ? 0xff2b2b2b : 0xbb2b2b2b;
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, inColor); //background
		
		if (!isMouseInsideGui(mXIn, mYIn)) checkTime();
		else if (!moving) birthTime = System.currentTimeMillis();
		
		close.setVisible(isMouseInsideGui(mXIn, mYIn));
		
		calcPos();
		super.drawObject(dt, mXIn, mYIn);
	}
	
	protected void calcPos() {
	    if (!moving) return;
	    
	    double time = (double) (System.nanoTime() - startTime) / 1000000000;
        double nX = sX + (v0 * time) + (a * Math.pow(time, 2)) / 2;
        double dist = nX - startX;
        move((int) dist, 0);
        if (time >= this.time) {
            moving = false;
            startTime = 0l;
            if (moveOut) close();
        }
	}
	
	protected void checkTime() {
	    if (moveOut || !expires) return;
	    if (System.currentTimeMillis() - birthTime >= timeOut) {
            moveOut();
        }
	}

	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		birthTime = System.currentTimeMillis();
		setPosition(WindowSize.width() , startY);
		moveOverTime(-(width + 3), 0.45);
	}

	private void moveOverTime(double amount, double time) {
		this.time = time;
		sX = startX;
		double eX = sX + amount;
		v0 = (2 * (eX - sX)) / time;
		a = -v0 / time;
		startTime = System.nanoTime();
		moving = true;
	}

	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		//super.mousePressed(mXIn, mYIn, button);
		if (button == 1) {
		    Envision.displayWindow(ScreenLevel.TOP, new NotificationRCM(this), ObjectPosition.CURSOR_CORNER);
		}
	}

	@Override
	public void close() {
		super.close();
		Envision.getNotificationHandler().removeCurrentNotification();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == close) close();
	}

	public void moveOut() {
		moveOverTime(width + 3, 0.75);
		moveOut = true;
	}

	public String getMessage() { return message; }
	public NotificationType getType() { return type; }
	public WindowParent getAttentionObject() { return attentionObject; }
	public boolean onlyDrawsOnHud() { return onlyDrawOnHud; }

	public NotificationObject setOnlyDrawOnHud(boolean val) { onlyDrawOnHud = val; return this; }
	public NotificationObject setExpires(boolean val) { expires = val; return this; }
	public NotificationObject setTimeOut(long timeIn) { timeOut = timeIn; return this; }
	public NotificationObject setAttentionObject(WindowParent objIn) { attentionObject = objIn; return this; }
}
