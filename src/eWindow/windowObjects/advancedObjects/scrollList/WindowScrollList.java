package eWindow.windowObjects.advancedObjects.scrollList;

import eWindow.WindowObjectS;
import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowObjects.actionObjects.WindowScrollBar;
import eWindow.windowTypes.WindowObject;
import eWindow.windowTypes.WindowParent;
import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectEventType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eWindow.windowUtil.windowEvents.events.EventModify;
import eWindow.windowUtil.windowEvents.events.EventObjects;
import input.Keyboard;
import java.util.Iterator;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public class WindowScrollList extends WindowObject {
	
	protected EArrayList<IWindowObject> listContents = new EArrayList();
	protected EArrayList<IWindowObject> drawnListObjects = new EArrayList();
	protected WindowScrollBar verticalScroll, horizontalScroll;
	protected WindowButton reset;
	protected double scrollableHeight = 0;
	protected double scrollableWidth = 0;
	protected EArrayList<IWindowObject> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject> listObjsToBeAdded = new EArrayList();
	protected EArrayList<IWindowObject> ignoreList = new EArrayList();
	protected int backgroundColor = 0xff4D4D4D;
	protected int borderColor = 0xff000000;
	protected double heightToBeSet = 0, widthToBeSet = 0;
	protected boolean vScrollVis = true;
	protected boolean hScrollVis = true;
	protected boolean resetVis = false;
	protected boolean allowScrolling = true;
	protected boolean drawListObjects = true;
	
	protected WindowScrollList() {}
	public WindowScrollList(IWindowObject parentIn, double xIn, double yIn, double widthIn, double heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = (int) widthIn - 2;
		scrollableHeight = (int) heightIn - 2;
	}
	
	@Override
	public void initObjects() {
		verticalScroll = new WindowScrollBar(this, height - 2, scrollableHeight, ScreenLocation.right, 3);
		horizontalScroll = new WindowScrollBar(this, width - 2, scrollableWidth, ScreenLocation.bot, 3);
		
		reset = new WindowButton(this, endX - 5, endY - 5, 5, 5);
		
		verticalScroll.setVisible(vScrollVis);
		horizontalScroll.setVisible(hScrollVis);
		reset.setVisible(resetVis);

		addObject(verticalScroll, horizontalScroll, reset);
		
		setListHeight(heightToBeSet).setListWidth(widthToBeSet);
		
		updateVisuals();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, borderColor);
		
		verticalScroll.setVisible(isVScrollDrawn());
		horizontalScroll.setVisible(isHScrollDrawn());
		reset.setVisible(isResetDrawn());
		
		//int scale = res.getScaleFactor();
		try {
			if (checkDraw() && height > (isHScrollDrawn() ? 5 : 2) && width > (isVScrollDrawn() ? 5 : 2)) {
				//GlStateManager.pushMatrix();
				//GlStateManager.enableBlend();
				
				//draw list contents scissored
				scissor(startX + 1, startY + 1.0, endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 1), endY - (isHScrollDrawn() ? horizontalScroll.height + 2 : 1));
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //draw background
				
				if (drawListObjects) {
					//only draw the objects that are actually in the viewable area
					for (IWindowObject o : drawnListObjects) {
						if (o.checkDraw()) {
							if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
							//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
							EDimension d = o.getDimensions();
							o.drawObject(mXIn, mYIn);
						}
					}
				}
				endScissor();
				
				if (isVScrollDrawn()) { drawRect(endX - verticalScroll.width - 2, startY + 1, endX, endY, EColors.black); }
				if (isHScrollDrawn()) { drawRect(startX + 1, endY - horizontalScroll.height - 2, endX, endY, EColors.black); }
				
				//draw non list contents as normal (non scissored)
				for (IWindowObject o : windowObjects) {
					if (o.checkDraw() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
	    				//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mXIn, mYIn);
	    			}
				}
				
				//GlStateManager.popMatrix();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		updateDrawnObjects();
	}
	
	@Override
	public void move(double newX, double newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (moveable) {
			EArrayList<IWindowObject> objs = new EArrayList(windowObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IWindowObject> it = objs.iterator();
			while (it.hasNext()) {
				IWindowObject o = it.next();
				if (o.isMoveable()) {
					if (o instanceof WindowParent) {
						if (((WindowParent) o).movesWithParent()) { o.move(newX, newY); }
					}
					else {
						if (listContents.contains(o)) { o.setInitialPosition(o.getInitialPosition().getA() + newX, o.getInitialPosition().getB() + newY); }
						o.move(newX, newY);
					}
				}
			}
			startX += newX;
			startY += newY;
			if (boundaryDimension != null) { boundaryDimension.move(newX, newY); }
			setDimensions(startX, startY, width, height);
		}
	}
	
	@Override
	public WindowScrollList setPosition(double newX, double newY) {
		if (isMoveable()) {
			EDimension d = getDimensions();
			StorageBox<Double, Double> loc = new StorageBox(d.startX, d.startY);
			StorageBoxHolder<IWindowObject, StorageBox<Double, Double>> previousLocations = new StorageBoxHolder();
			EArrayList<IWindowObject> objs = new EArrayList();
			objs.addAll(getObjects());
			objs.addAll(getAddingObjects());
			for (IWindowObject o : objs) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getA(), o.getDimensions().startY - loc.getB()));
			}
			setDimensions(newX, newY, d.width, d.height);
			for (IWindowObject o : objs) {
				if (o.isMoveable()) {
					StorageBox<Double, Double> oldLoc = previousLocations.getBoxWithA(o).getB();
					o.setInitialPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
					
					if (listContents.contains(o) || listObjsToBeAdded.contains(o)) {
						double eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 1);
						double eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
						
						EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
						
						o.setBoundaryEnforcer(getDimensions());
						for (IWindowObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
						for (IWindowObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					}
					
					o.setPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
				}
			}
		}
		return this;
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == reset) {
			verticalScroll.reset();
			horizontalScroll.reset();
		}
		if (object == verticalScroll || object == horizontalScroll) {
			double vScrollPos = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
			double hScrollPos = horizontalScroll.getScrollPos() - horizontalScroll.getVisibleAmount();
			for (IWindowObject o : EArrayList.combineLists(listContents, listObjsToBeAdded)) {
				o.setPosition(o.getInitialPosition().getA() - hScrollPos, o.getInitialPosition().getB() - vScrollPos);
			}
			updateDrawnObjects();
		}
	}
	
	protected void updateDrawnObjects() {
		drawnListObjects.clear();
		drawnListObjects.addAll(listContents.stream().filter(o -> objectInstance.getDimensions().contains(o.getDimensions())).collect(EArrayList.toEArrayList()));
		drawnListObjects.addAll(listObjsToBeAdded.stream().filter(o -> objectInstance.getDimensions().contains(o.getDimensions())).collect(EArrayList.toEArrayList()));
	}
	
	public EDimension getListDimensions() {
		double w = (endX - (isVScrollDrawn() ? verticalScroll.width + 3 : 1)) - startX;
		double h = (endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1)) - startY - 2;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		verticalScroll.setScrollBarPos(0);
		verticalScroll.setScrollBarPos(0);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (allowScrolling) {
			if (Keyboard.isShiftDown()) {
				if (scrollableWidth - (width - 2) > 0) { horizontalScroll.setScrollBarPos(horizontalScroll.getScrollPos() - change * 18); }
			}
			else if (scrollableHeight - (height - 2) > 0) { verticalScroll.setScrollBarPos(verticalScroll.getScrollPos() - change * 18); }
		}
		super.mouseScrolled(change);
	}
	
	public WindowScrollList fitItemsInList() { return fitItemsInList(0, 0); }
	public WindowScrollList fitItemsInList(int overShootX, int overShootY) {
		double right = 0;
		double down = 0;
		
		//get both the current list objects and those being added
		EArrayList<IWindowObject> objs = EArrayList.combineLists(listContents, listObjsToBeAdded);
		EArrayList<IWindowObject> aObjs = new EArrayList();
		EArrayList<IWindowObject> ignored = new EArrayList(ignoreList);
		
		for (IWindowObject o : objs) {
			if (listObjsToBeRemoved.notContains(o) && ignored.notContains(o)) { aObjs.add(o); }
		}
		
		//find right
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endX > right) { right = od.endX; }
		}
		
		//find down
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endY > down) { down = od.endY; }
		}
		
		//prevent negative values
		double w = NumUtil.clamp((right - startX) + overShootX, 0, Integer.MAX_VALUE);
		double h = NumUtil.clamp((down - startY) + overShootY, 0, Integer.MAX_VALUE);
		
		setListSize(w, h);
		
		return this;
	}
	
	public WindowScrollList setListSize(double widthIn, double heightIn) {
		setListWidth(widthIn);
		setListHeight(heightIn);
		return this;
	}
	public WindowScrollList setListWidth(double widthIn) {
		if (horizontalScroll != null) {
			scrollableWidth = (int) widthIn;
			horizontalScroll.setHighVal((int) widthIn);
			updateVisuals();
		}
		else { widthToBeSet = (int) widthIn; }
		return this;
	}
	public WindowScrollList setListHeight(double heightIn) {
		if (verticalScroll != null) {
			scrollableHeight = (int) heightIn;
			verticalScroll.setHighVal((int) heightIn);
			updateVisuals();
		}
		else { heightToBeSet = (int) heightIn; }
		return this;
	}
	public WindowScrollList growList(double amount) {
		growListWidth(amount);
		growListHeight(amount);
		return this;
	}
	public WindowScrollList growListWidth(double amount) {
		if (horizontalScroll != null) { setListWidth(getListWidth() + amount); updateVisuals(); }
		else { widthToBeSet += amount; }
		return this;
	}
	public WindowScrollList growListHeight(double amount) {
		if (verticalScroll != null) { setListHeight(getListHeight() + amount); updateVisuals(); }
		else { heightToBeSet += amount; }
		return this;
	}
	
	public WindowScrollList addAndIgnore(IWindowObject... objsIn) { addToIgnoreList(objsIn); addObjectToList(objsIn); return this; }
	
	public WindowScrollList addObjectToList(IWindowObject... objsIn) { return addObjectToList(true, objsIn); }
	public WindowScrollList addObjectToList(boolean useRelativeCoords, IWindowObject... objsIn) {
		for (IWindowObject o : objsIn) {
			try {
				if (o != null && o != this) {
					
					double eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 2);
					double eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
					
					EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					if (useRelativeCoords) {
						o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					}
					
					o.setParent(this).initObjects();
					if (o instanceof WindowParent) { ((WindowParent) o).initWindow(); }
					o.completeInit();
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IWindowObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
					for (IWindowObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					listObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return this;
	}
	
	public WindowScrollList removeObjectFromList(IWindowObject... objsIn) {
		listObjsToBeRemoved.addAll(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	public WindowScrollList removeObject(IWindowObject obj, IWindowObject... additional) {
		objsToBeRemoved.addAll(additional);
		listObjsToBeRemoved.addAll(additional);
		return this;
	}
	
	@Override
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = Game.getWindowSize();
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseOver(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { WindowObjectS.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { WindowObjectS.addObjects(this, objsToBeAdded); }
		if (!listObjsToBeRemoved.isEmpty()) { removeListObjects(); }
		if (!listObjsToBeAdded.isEmpty()) { addListObjects(); }
	}
	
	protected void removeListObjects() {
		for (IWindowObject o : listObjsToBeRemoved ) {
			if (o != null) {
				Iterator it = listContents.iterator();
				while (it.hasNext()) {
					if (o.equals(it.next())) {
						if (!o.equals(getTopParent().getFocusedObject())) {
							for (IWindowObject child : o.getObjects()) {
								if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
							}
						}
						else { o.relinquishFocus(); }
						o.onClosed();
						if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
						it.remove();
					}
				} //while
			}
		}
		listObjsToBeRemoved.clear();
		updateDrawnObjects();
	}
	
	protected void addListObjects() {
		for (IWindowObject o : listObjsToBeAdded) {
			listContents.add(o);
		}
		listObjsToBeAdded.clear();
		updateDrawnObjects();
	}
	
	protected void updateVisuals() {
		if (isHScrollDrawn() && !isVScrollDrawn()) {
			EDimension h = horizontalScroll.getDimensions();
			horizontalScroll.setDimensions(h.startX, h.startY, width - 2 - (isResetDrawn() ? 4 : 0), h.height);
		}
		
		if (isVScrollDrawn() && !isHScrollDrawn()) {
			EDimension v = verticalScroll.getDimensions();
			verticalScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
		
		if (isHScrollDrawn() && isVScrollDrawn()) {
			EDimension h = horizontalScroll.getDimensions();
			EDimension v = verticalScroll.getDimensions();
			
			horizontalScroll.setDimensions(h.startX, h.startY, width - 3 - verticalScroll.width - (isResetDrawn() ? 3 : 0), h.height);
			verticalScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
	}
	
	public void clearList() {
		drawnListObjects.clear();
		listContents.clear();
		listObjsToBeAdded.clear();
		
		reInitObjects();
	}
	
	public WindowScrollList clearIgnoreList() { ignoreList = new EArrayList(); return this; }
	public WindowScrollList setIgnoreList(IWindowObject... objects) {
		ignoreList = new EArrayList<IWindowObject>().addA(objects);
		return this;
	}
	
	public WindowScrollList addToIgnoreList(IWindowObject... objects) {
		if (ignoreList == null) { ignoreList = new EArrayList<IWindowObject>(); }
		ignoreList.addA(objects);
		return this;
	}
	
	public WindowScrollList setDrawListObjects(boolean val) { drawListObjects = val; return this; }
	public WindowScrollList setAllowScrolling(boolean val) { allowScrolling = val; return this; }
	public WindowScrollList setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.intVal); }
	public WindowScrollList setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowScrollList setBorderColor(EColors colorIn) { return setBorderColor(colorIn.intVal); }
	public WindowScrollList setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowScrollList setVScrollDrawn(boolean valIn) { vScrollVis = valIn; if (verticalScroll != null) { verticalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList setHScrollDrawn(boolean valIn) { hScrollVis = valIn; if (horizontalScroll != null) { horizontalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList setResetDrawn(boolean valIn) { resetVis = valIn; if (reset != null) { reset.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList renderVScrollBarThumb(boolean val) { if (verticalScroll != null) { verticalScroll.setRenderThumb(val); } else { vScrollVis = val; } return this; }
	public WindowScrollList renderHScrollBarThumb(boolean val) { if (horizontalScroll != null) { horizontalScroll.setRenderThumb(val); } else { hScrollVis = val; } return this; }
	
	public double getListHeight() { return scrollableHeight - (isHScrollDrawn() ? horizontalScroll.getDimensions().height : 0); }
	public double getListWidth() { return scrollableWidth - (isVScrollDrawn() ? verticalScroll.getDimensions().width : 0); }
	public boolean getDrawListObjects() { return drawListObjects; }
	public boolean isVScrollDrawn() { return vScrollVis && (verticalScroll != null ? verticalScroll.getHighVal() > verticalScroll.getVisibleAmount() : false); }
	public boolean isHScrollDrawn() { return hScrollVis && (horizontalScroll != null ? horizontalScroll.getHighVal() > horizontalScroll.getVisibleAmount() : false); }
	public boolean isResetDrawn() { return resetVis && (isVScrollDrawn() || isHScrollDrawn()); }
	public WindowScrollBar getVScrollBar() { return verticalScroll; }
	public WindowScrollBar getHScrollBar() { return horizontalScroll; }
	public EArrayList<IWindowObject> getDrawnObjects() { return drawnListObjects; }
	public EArrayList<IWindowObject> getListObjects() { return listContents; }
	public EArrayList<IWindowObject> getAddingListObjects() { return listObjsToBeAdded; }
	
}
