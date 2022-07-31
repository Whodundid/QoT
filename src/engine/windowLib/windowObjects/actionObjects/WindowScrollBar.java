package engine.windowLib.windowObjects.actionObjects;

import engine.windowLib.windowTypes.ActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.windowEvents.events.EventFocus;
import eutil.datatypes.Box2;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class WindowScrollBar<E> extends ActionObject<E> {
	
	//--------
	// Fields
	//--------
	
	public boolean vertical = false;
	public double scrollBarThickness = 3;
	public double thumbSize = 50;
	public double scrollPos = 0;
	protected double lastScrollChange = 0;
	public double highVal = 0;
	protected double interval = 0;
	protected double blockIncrement = 0;
	protected double visibleAmount = 0;
	protected double thumbStartX = 0, thumbStartY = 0;
	protected double thumbEndX = 0, thumbEndY = 0;
	public boolean isScrolling = false;
	public boolean renderThumb = true;
	protected IWindowParent<?> window;
	private Box2<Integer, Integer> mousePos = new Box2<>(0, 0);
	
	//--------------
	// Constructors
	//--------------
	
	public WindowScrollBar(IWindowObject<?> parentIn, double visibleAmountIn, double highValIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, ScreenLocation.RIGHT, 3); 
	}
	
	public WindowScrollBar(IWindowObject<?> parentIn, double visibleAmountIn, double highValIn, double widthIn, double heightIn) {
		this(parentIn, visibleAmountIn, highValIn, widthIn, heightIn, ScreenLocation.RIGHT, 3);
	}
	
	public WindowScrollBar(IWindowObject<?> parentIn, double visibleAmountIn, double highValIn, ScreenLocation sideIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, sideIn, 3);
	}
	
	public WindowScrollBar(IWindowObject<?> parentIn, double visibleAmountIn, double highValIn, ScreenLocation sideIn, int thicknessIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, sideIn, thicknessIn);
	}
	
	public WindowScrollBar(IWindowObject parentIn, double visibleAmountIn, double highValIn, double widthIn, double heightIn, ScreenLocation sideIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, sideIn, 3);
	}
	
	public WindowScrollBar(IWindowObject<?> parentIn, double visibleAmountIn, double highValIn, double widthIn, double heightIn, ScreenLocation sideIn, double thicknessIn) {
		EDimension dim = parentIn.getDimensions();
		
		scrollBarThickness = thicknessIn;
		
		if (sideIn == ScreenLocation.TOP || sideIn == ScreenLocation.BOT) vertical = false;
		else vertical = true;
		
		double sWidth = vertical ? (widthIn < 0 ? scrollBarThickness : widthIn) : (widthIn < 0 ? dim.width - 2 : widthIn);
		double sHeight = vertical ? (heightIn < 0 ? dim.height - 2 : heightIn) : (heightIn < 0 ? scrollBarThickness : heightIn);
		
		switch (sideIn) {
		case TOP: init(parentIn, dim.startX + 1, dim.startY + 1, sWidth, sHeight); break;
		case BOT: init(parentIn, dim.startX + 1, dim.endY - scrollBarThickness - 1, sWidth, sHeight); break;
		case RIGHT: init(parentIn, dim.endX - scrollBarThickness - 1, dim.startY + 1, sWidth, sHeight); break;
		case LEFT: init(parentIn, dim.startX + 1, dim.startY + 1, sWidth, sHeight); break;
		default: init(parentIn, dim.endX - scrollBarThickness - 1, dim.startY + 1, sWidth, sHeight); break;
		}
		
		setThumb();
		
		setScrollBarValues(visibleAmountIn, highValIn, (int) (vertical ? height : width));
		window = getWindowParent();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void resetPosition() {
		super.resetPosition();
		setScrollPos(scrollPos);
	}
	
	@Override
	public void move(double newX, double newY) {
		thumbStartX += newX;
		thumbStartY += newY;
		thumbEndX += newX;
		thumbEndY += newY;
		super.move(newX, newY);
	}
	
	@Override
	public void setPosition(double newX, double newY) {
		super.setPosition(newX, newY);
		setScrollPos(scrollPos);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		if (isScrolling && mousePos != null && mousePos.getA() != null && mousePos.getB() != null) {
			if (vertical && mY - mousePos.getB() != 0) moveThumb(0, mY - mousePos.getB());
			else if (mX - mousePos.getA() != 0) moveThumb(mX - mousePos.getA(), 0);
			mousePos.set(mX, mY);
		}
		
		drawRect(startX, startY, startX + width, startY + height, 0xff444444);
		
		if (renderThumb) {
			int color = 0xffbbbbbb;
			if (isMouseInThumb(mX, mY) || isScrolling) {
				color = isScrolling ? 0xffffffff : 0xffdddddd;
			}
			drawRect(thumbStartX, thumbStartY, thumbEndX, thumbEndY, color);
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (isMouseInThumb(mX, mY)) {
			isScrolling = true;
			mousePos.set(mX, mY);
		}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		isScrolling = false;
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		isScrolling = false;
	}
	
	//---------
	// Methods
	//---------
	
	public void reset() { setScrollPos(0); }
	
	public boolean isMouseInThumb(double mX, double mY) {
		return (mX >= thumbStartX && mX <= thumbEndX && mY >= thumbStartY && mY <= thumbEndY);
	}
	
	public void onResizeUpdate(double val, double xIn, double yIn, ScreenLocation areaIn) {
		double hResizeVal = 0;
		double vResizeVal = 0;
		
		switch (areaIn) {
		case TOP: vResizeVal = -yIn; hResizeVal = 0; break;
		case BOT: vResizeVal = yIn; hResizeVal = 0; break;
		case LEFT: vResizeVal = 0; hResizeVal = -xIn; break;
		case RIGHT: vResizeVal = 0; hResizeVal = xIn; break;
		case BOT_LEFT: vResizeVal = yIn; hResizeVal = -xIn; break;
		case BOT_RIGHT: vResizeVal = yIn; hResizeVal = xIn; break;
		case TOP_LEFT: vResizeVal = -yIn; hResizeVal = -xIn; break;
		case TOP_RIGHT: vResizeVal = -yIn; hResizeVal = xIn; break;
		default: break;
		}
		
		if (window != null) {

			//IWindowParent p = window;
			//EDimension d = p.getDimensions();
			
			// unsure why this was commented out ~
			
			//if (p.getMinHeight() <= d.height || p.getMaxHeight() >= d.height) { vResizeVal = 0; }
			//if (p.getMinWidth() <= d.width || p.getMaxWidth() >= d.width) { hResizeVal = 0; }
		}
		
		setScrollPos((int) (val + (vertical ? vResizeVal : hResizeVal)));
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void setThumb() {
		if (vertical) {
			thumbStartX = endX - scrollBarThickness;
			thumbStartY = startY;
			thumbEndX = endX;
			thumbEndY = startY + thumbSize;
		}
		else {
			thumbStartX = startX;
			thumbStartY = startY;
			thumbEndX = startX + thumbSize;
			thumbEndY = startY + scrollBarThickness;
		}
	}
	
	private void moveThumb(double newX, double newY) {
		if (vertical) {
			if (!(thumbStartY + newY < startY) && (thumbEndY + newY < endY + 1)) {
				thumbStartY += newY;
				thumbEndY += newY;
			}
			else {
				if (newY < 0) {
					thumbStartY = startY;
					thumbEndY = startY + thumbSize;
				}
				else if (newY > 0) {
					thumbStartY = endY - thumbSize;
					thumbEndY = endY;
				}
			}
		}
		else {
			if (!(thumbStartX + newX < startX + 1) && (thumbEndX + newX < endX)) {
				thumbStartX += newX;
				thumbEndX += newX;
			}
			else {
				if (newX < 0) {
					thumbStartX = startX;
					thumbEndX = startX + thumbSize;
				}
				else if (newX > 0) {
					thumbStartX = endX - thumbSize;
					thumbEndX = endX;
				}
			}
		}
		calculateScrollPos();
		lastScrollChange = vertical ? newY : newX;
		getActionReceiver().actionPerformed(this);
	}
	
	private void calculateScrollPos() {
		double relativeThumbPos = 0;
		if (vertical) {
			relativeThumbPos = NumberUtil.clamp((double)(thumbStartY - startY) / (height - thumbSize), 0f, 1f);
		}
		else {
			relativeThumbPos = NumberUtil.clamp((double)(thumbStartX - startX) / (width - thumbSize), 0f, 1f);
		}
		double val = visibleAmount + (highVal - visibleAmount) * relativeThumbPos;
		scrollPos = (int) val;
	}
	
	private void recalculateInterval() {
		double dist = highVal - visibleAmount;
		if (dist > 0) {
			interval = vertical ? ((double) height - (double) thumbSize) / dist : ((double) width - (double) thumbSize) / dist;
		}
		else { interval = 0; }
	}
	
	private void setScrollBarValues(double visibleAmountIn, double highValIn, double visibleSize) {
		visibleAmount = visibleAmountIn;
		highVal = Math.max(highValIn, visibleAmount);
		double val = 1.0;
		if (highVal > visibleAmount) { val = (double) visibleAmount / (double) highVal; }
		thumbSize = ((vertical ? height : width) * val);
		thumbSize = Math.max(thumbSize, height * 0.125);
		thumbSize = Math.max(thumbSize, 1);
		thumbSize = Math.min(thumbSize, vertical ? height : width);
		blockIncrement = Math.max((visibleSize * 0.9), 1);
		recalculateInterval();
		setScrollPos(scrollPos);
	}
	
	//---------
	// Getters
	//---------
	
	public boolean getDrawVertical() { return vertical; }
	public boolean getIsThumbRendered() { return renderThumb; }
	public double getLastScrollChange() { return lastScrollChange; }
	public double getScrollBarThickness() { return scrollBarThickness; }
	public double getThumbSize() { return thumbSize; }
	public double getScrollPos() { return scrollPos; }
	public double getVisibleAmount() { return visibleAmount; }
	public double getHighVal() { return highVal; }
	
	//---------
	// Setters
	//---------
	
	public void setScrollPos(double pos) {
		//lastScrollChange = scrollPos + -;
		pos = pos < visibleAmount ? visibleAmount : pos;
		pos = pos > highVal ? highVal : pos;
		//System.out.println("pos pos: " + pos);
		scrollPos = pos;
		if (vertical) {
			thumbStartX = startX;
			thumbStartY = (int) (startY + (scrollPos - visibleAmount) * interval);
			thumbEndX = endX;
			thumbEndY = thumbStartY + thumbSize;
		}
		else {
			thumbStartX = (int) (startX + (scrollPos - visibleAmount) * interval);
			thumbStartY = startY;
			thumbEndX = thumbStartX + thumbSize;
			thumbEndY = endY;
		}
		performAction(this);
	}
	
	public void setRenderThumb(boolean val) { renderThumb = val; }
	public void setVisibleAmount(int sizeIn) { visibleAmount = sizeIn; setScrollBarValues(visibleAmount, highVal, (vertical ? height : width)); }
	public void setHighVal(double valIn) { setScrollBarValues(visibleAmount, valIn, (getDrawVertical() ? height : width)); }
	public void setLowVal(double valIn) { setScrollBarValues(valIn, highVal, (getDrawVertical() ? height : width)); }
	
}
