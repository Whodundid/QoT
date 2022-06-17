package engine.windowLib.windowObjects.advancedObjects;

import engine.input.Keyboard;
import engine.renderEngine.GLSettings;
import engine.windowLib.StaticWindowObject;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowScrollBar;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import engine.windowLib.windowUtil.windowEvents.events.EventModify;
import engine.windowLib.windowUtil.windowEvents.events.EventObjects;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import eutil.misc.ScreenLocation;
import main.QoT;

//Author: Hunter Bragg

public class WindowScrollList<E> extends WindowObject<E> {
	
	protected EArrayList<IWindowObject<?>> listContents = new EArrayList();
	protected EArrayList<IWindowObject<?>> drawnListObjects = new EArrayList();
	protected WindowScrollBar vScroll, hScroll;
	protected WindowButton reset;
	protected double scrollableHeight = 0;
	protected double scrollableWidth = 0;
	protected EArrayList<IWindowObject<?>> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject<?>> listObjsToBeAdded = new EArrayList();
	protected EArrayList<IWindowObject<?>> ignoreList = new EArrayList();
	protected int backgroundColor = 0xff4D4D4D;
	protected int borderColor = 0xff000000;
	protected double heightToBeSet = 0, widthToBeSet = 0;
	protected boolean vScrollVis = true;
	protected boolean hScrollVis = true;
	protected boolean resetVis = false;
	protected boolean allowScrolling = true;
	protected boolean drawListObjects = true;
	
	//--------------
	// Constructors
	//--------------
	
	protected WindowScrollList() {}
	public WindowScrollList(IWindowObject parentIn, double xIn, double yIn, double widthIn, double heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = (int) widthIn - 2;
		scrollableHeight = (int) heightIn - 2;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initObjects() {
		vScroll = new WindowScrollBar(this, height - 2, scrollableHeight, ScreenLocation.RIGHT, 7);
		hScroll = new WindowScrollBar(this, width - 2, scrollableWidth, ScreenLocation.BOT, 7);
		
		reset = new WindowButton(this, endX - 5, endY - 5, 5, 5);
		
		vScroll.setVisible(vScrollVis);
		hScroll.setVisible(hScrollVis);
		reset.setVisible(resetVis);

		addObject(vScroll, hScroll, reset);
		
		setListHeight(heightToBeSet).setListWidth(widthToBeSet);
		updateVisuals();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, borderColor);
		
		vScroll.setVisible(isVScrollDrawn());
		hScroll.setVisible(isHScrollDrawn());
		reset.setVisible(isResetDrawn());
		
		//int scale = res.getScaleFactor();
		try {
			if (willBeDrawn() && height > (isHScrollDrawn() ? 5 : 2) && width > (isVScrollDrawn() ? 5 : 2)) {
				GLSettings.pushMatrix();
				GLSettings.enableBlend();
				
				//draw list contents scissored
				scissor(startX + 1, startY + 1.0, endX - (isVScrollDrawn() ? vScroll.width + 2 : 1), endY - (isHScrollDrawn() ? hScroll.height + 2 : 0));
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //draw background
				
				if (drawListObjects) {
					//only draw the objects that are actually in the viewable area
					for (IWindowObject<?> o : drawnListObjects) {
						if (o.willBeDrawn()) {
							if (!o.hasFirstDraw()) { o.onFirstDraw(); }
							GLSettings.fullBright();
							//EDimension d = o.getDimensions();
							o.drawObject(mXIn, mYIn);
						}
					}
				}
				endScissor();
				
				if (isVScrollDrawn()) drawRect(endX - vScroll.width - 2, startY + 1, endX, endY, EColors.black);
				if (isHScrollDrawn()) drawRect(startX + 1, endY - hScroll.height - 2, endX, endY, EColors.black);
				
				//draw non list contents as normal (non scissored)
				for (IWindowObject<?> o : windowObjects) {
					if (o.willBeDrawn() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) { o.onFirstDraw(); }
						GLSettings.fullBright();
	    	        	o.drawObject(mXIn, mYIn);
	    			}
				}
				
				GLSettings.popMatrix();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		updateDrawnObjects();
	}
	
	@Override
	public void move(double newX, double newY) {
		if (eventHandler != null) eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move));
		if (moveable) {
			EArrayList<IWindowObject<?>> objs = new EArrayList(windowObjects);
			objs.addAll(objsToBeAdded);
			objs.addAll(listContents);
			objs.addAll(listObjsToBeAdded);
			
			for (var o : objs) {
				if (o.isMoveable()) {
					if (o instanceof WindowParent p) {
						if (p.movesWithParent()) o.move(newX, newY);
					}
					else {
						if (listContents.contains(o)) {
							o.setInitialPosition(o.getInitialPosition().getA() + newX, o.getInitialPosition().getB() + newY);
						}
						o.move(newX, newY);
					}
				}
			}
			
			startX += newX;
			startY += newY;
			if (boundaryDimension != null) boundaryDimension.move(newX, newY);
			setDimensions(startX, startY, width, height);
		}
	}
	
	@Override
	public void setPosition(double newX, double newY) {
		if (isMoveable()) {
			EDimension d = getDimensions();
			Box2<Double, Double> loc = new Box2(d.startX, d.startY);
			BoxList<IWindowObject<?>, Box2<Double, Double>> previousLocations = new BoxList();
			
			EArrayList<IWindowObject<?>> objs = getCombinedObjects();
			
			for (IWindowObject<?> o : objs) {
				var dims = o.getDimensions();
				previousLocations.add(o, new Box2(dims.startX - loc.getA(), dims.startY - loc.getB()));
			}
			
			setDimensions(newX, newY, d.width, d.height);
			
			for (IWindowObject<?> o : objs) {
				if (o.isMoveable()) {
					Box2<Double, Double> oldLoc = previousLocations.getBoxWithA(o).getB();
					o.setInitialPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
					o.setPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
				}
			}
			
			// Now handle list objects
			
			objs.clear();
			previousLocations.clear();
			
			objs.addAll(listContents);
			objs.addAll(listObjsToBeAdded);
			
			for (IWindowObject<?> o : objs) {
				var dims = o.getDimensions();
				previousLocations.add(o, new Box2(dims.startX - loc.getA(), dims.startY - loc.getB()));
			}
			
			for (IWindowObject<?> o : objs) {
				double eX = endX - (isVScrollDrawn() ? vScroll.width + 2 : 1);
				double eY = endY - (isHScrollDrawn() ? hScroll.height - 4 : 1);
				
				EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
				
				o.setBoundaryEnforcer(getDimensions());
				for (IWindowObject<?> q : o.getCombinedObjects()) q.setBoundaryEnforcer(bounds);
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == reset) {
			vScroll.reset();
			hScroll.reset();
		}
		
		if (object == vScroll || object == hScroll) {
			double vScrollPos = vScroll.getScrollPos() - vScroll.getVisibleAmount();
			double hScrollPos = hScroll.getScrollPos() - hScroll.getVisibleAmount();
			
			for (IWindowObject<?> o : EArrayList.combineLists(listContents, listObjsToBeAdded)) {
				o.setPosition(o.getInitialPosition().getA() - hScrollPos, o.getInitialPosition().getB() - vScrollPos);
			}
			
			updateDrawnObjects();
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (allowScrolling) {
			if (Keyboard.isShiftDown()) {
				if (scrollableWidth - (width - 2) > 0) hScroll.setScrollPos(hScroll.getScrollPos() - change * 18);
			}
			else if (scrollableHeight - (height - 2) > 0) vScroll.setScrollPos(vScroll.getScrollPos() - change * 18);
		}
		super.mouseScrolled(change);
	}
	
	@Override
	public EArrayList<IWindowObject<?>> getObjects() {
		//var r = new EArrayList<IWindowObject<?>>(windowObjects);
		//r.addAll(listContents);
		return this.windowObjects;
	}
	
	@Override
	public EArrayList<IWindowObject<?>> getCombinedObjects() {
		var r = new EArrayList<IWindowObject<?>>(windowObjects);
		r.addAll(objsToBeAdded);
		r.addAll(listContents);
		r.addAll(listObjsToBeAdded);
		return r;
	}
	
	@Override
	public EArrayList<IWindowObject<?>> getAllChildren() {
		EArrayList<IWindowObject<?>> foundObjs = new EArrayList();
		EArrayList<IWindowObject<?>> objsWithChildren = new EArrayList();
		EArrayList<IWindowObject<?>> workList = new EArrayList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		getObjects().forEach(o -> {
			foundObjs.add(o);
			if (!o.getCombinedObjects().isEmpty()) objsWithChildren.add(o);
		});
		listContents.forEach(o -> {
			foundObjs.add(o);
			if (!o.getCombinedObjects().isEmpty()) objsWithChildren.add(o);
		});
		
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getCombinedObjects()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			foundObjs.addAll(workList);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.filterForEach(o -> o.getCombinedObjects().isNotEmpty(), objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getCombinedObjects()));
		}
		
		return foundObjs;
	}

	@Override
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = QoT.getWindowSize();
		mX = mXIn; mY = mYIn;
		
		if (!mouseEntered && isMouseOver()) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver()) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) StaticWindowObject.removeObjects(this, objsToBeRemoved);
		if (!objsToBeAdded.isEmpty()) StaticWindowObject.addObjects(this, objsToBeAdded);
		if (!listObjsToBeRemoved.isEmpty()) removeListObjects();
		if (!listObjsToBeAdded.isEmpty()) addListObjects();
	}
	
	//---------
	// Methods
	//---------
	
	protected void updateDrawnObjects() {
		drawnListObjects.clear();
		drawnListObjects.addAll(listContents.stream().filter(o -> instance.getDimensions().contains(o.getDimensions())).collect(EArrayList.toEArrayList()));
		drawnListObjects.addAll(listObjsToBeAdded.stream().filter(o -> instance.getDimensions().contains(o.getDimensions())).collect(EArrayList.toEArrayList()));
	}
	
	public EDimension getListDimensions() {
		double w = (endX - (isVScrollDrawn() ? vScroll.width + 3 : 1)) - startX;
		double h = (endY - (isHScrollDrawn() ? hScroll.height - 4 : 1)) - startY - 2;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		vScroll.setScrollPos(0);
		vScroll.setScrollPos(0);
	}
	
	public WindowScrollList fitItemsInList() { return fitItemsInList(0, 0); }
	public WindowScrollList fitItemsInList(int overShootX, int overShootY) {
		double right = 0;
		double down = 0;
		
		//get both the current list objects and those being added
		EArrayList<IWindowObject<?>> objs = EArrayList.combineLists(listContents, listObjsToBeAdded);
		EArrayList<IWindowObject<?>> aObjs = new EArrayList();
		EArrayList<IWindowObject<?>> ignored = new EArrayList(ignoreList);
		
		for (IWindowObject o : objs) {
			if (listObjsToBeRemoved.notContains(o) && ignored.notContains(o)) aObjs.add(o);
		}
		
		//find right
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endX > right) right = od.endX;
		}
		
		//find down
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endY > down) down = od.endY;
		}
		
		//prevent negative values
		double w = NumberUtil.clamp((right - startX) + overShootX, 0, Integer.MAX_VALUE);
		double h = NumberUtil.clamp((down - startY) + overShootY, 0, Integer.MAX_VALUE);
		
		setListSize(w, h);
		
		return this;
	}
	
	public WindowScrollList setListSize(double widthIn, double heightIn) {
		setListWidth(widthIn);
		setListHeight(heightIn);
		return this;
	}
	
	public WindowScrollList setListWidth(double widthIn) {
		if (hScroll != null) {
			scrollableWidth = (int) widthIn;
			hScroll.setHighVal((int) widthIn);
			updateVisuals();
		}
		else widthToBeSet = widthIn;
		return this;
	}
	
	public WindowScrollList setListHeight(double heightIn) {
		if (vScroll != null) {
			scrollableHeight = (int) heightIn;
			vScroll.setHighVal((int) heightIn);
			updateVisuals();
		}
		else heightToBeSet = heightIn;
		return this;
	}
	
	public WindowScrollList growList(double amount) {
		growListWidth(amount);
		growListHeight(amount);
		return this;
	}
	
	public WindowScrollList growListWidth(double amount) {
		if (hScroll != null) {
			setListWidth(getListWidth() + amount);
			updateVisuals();
		}
		else widthToBeSet += amount;
		return this;
	}
	
	public WindowScrollList growListHeight(double amount) {
		if (vScroll != null) {
			setListHeight(getListHeight() + amount);
			updateVisuals();
		}
		else heightToBeSet += amount;
		return this;
	}
	
	public WindowScrollList addAndIgnore(IWindowObject... objsIn) {
		addToIgnoreList(objsIn);
		addObjectToList(objsIn);
		return this;
	}
	
	public WindowScrollList addObjectToList(IWindowObject... objsIn) { return addObjectToList(true, objsIn); }
	public WindowScrollList addObjectToList(boolean useRelativeCoords, IWindowObject... objsIn) {
		for (IWindowObject<?> o : objsIn) {
			try {
				if (o != null && o != this) {
					
					double eX = endX - (isVScrollDrawn() ? vScroll.width + 2 : 2);
					double eY = endY - (isHScrollDrawn() ? hScroll.height - 4 : 1);
					
					EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					if (useRelativeCoords) {
						o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					}
					
					o.setParent(this).initObjects();
					if (o instanceof WindowParent p) p.initWindow();
					o.completeInit();
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IWindowObject q : o.getCombinedObjects()) q.setBoundaryEnforcer(bounds);
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					listObjsToBeAdded.add(o);
					//objsToBeAdded.add(o);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public WindowScrollList<E> removeObjectFromList(IWindowObject<?>... objsIn) {
		listObjsToBeRemoved.add(objsIn);
		return this;
	}
	
	@Override
	public WindowScrollList<E> removeObject(IWindowObject<?>... objs) {
		objsToBeRemoved.add(objs);
		listObjsToBeRemoved.add(objs);
		return this;
	}
	
	protected void removeListObjects() {
		for (int i = 0; i < listObjsToBeRemoved.size(); i++) {
			IWindowObject<?> o = listObjsToBeRemoved.get(i);
			if (o == null) continue;
			
			if (!o.equals(getTopParent().getFocusedObject())) {
				for (IWindowObject child : o.getObjects()) {
					if (child.equals(getTopParent().getFocusedObject())) child.relinquishFocus();
				}
			}
			else o.relinquishFocus();
			o.onClosed();
			if (eventHandler != null) eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved));
			
			listContents.remove(o);
		}
		
		listObjsToBeRemoved.clear();
		updateDrawnObjects();
	}
	
	protected void addListObjects() {
		listContents.addAll(listObjsToBeAdded);
		listObjsToBeAdded.clear();
		updateDrawnObjects();
	}
	
	protected void updateVisuals() {
		if (isHScrollDrawn() && !isVScrollDrawn()) {
			EDimension h = hScroll.getDimensions();
			hScroll.setDimensions(h.startX, h.startY, width - 2 - (isResetDrawn() ? 4 : 0), h.height);
		}
		
		if (isVScrollDrawn() && !isHScrollDrawn()) {
			EDimension v = vScroll.getDimensions();
			vScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
		
		if (isHScrollDrawn() && isVScrollDrawn()) {
			EDimension h = hScroll.getDimensions();
			EDimension v = vScroll.getDimensions();
			
			hScroll.setDimensions(h.startX, h.startY, width - 3 - vScroll.width - (isResetDrawn() ? 3 : 0), h.height);
			vScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
	}
	
	public void clearList() {
		drawnListObjects.clear();
		listContents.clear();
		listObjsToBeAdded.clear();
		
		reInitObjects();
	}
	
	public WindowScrollList<E> clearIgnoreList() { ignoreList = new EArrayList(); return this; }
	public WindowScrollList<E> setIgnoreList(IWindowObject<?>... objects) {
		ignoreList = new EArrayList<IWindowObject<?>>().add(objects);
		return this;
	}
	
	public WindowScrollList<E> addToIgnoreList(IWindowObject<?>... objects) {
		if (ignoreList == null) ignoreList = new EArrayList<IWindowObject<?>>();
		ignoreList.add(objects);
		return this;
	}
	
	public WindowScrollList<E> setDrawListObjects(boolean val) { drawListObjects = val; return this; }
	public WindowScrollList<E> setAllowScrolling(boolean val) { allowScrolling = val; return this; }
	public WindowScrollList<E> setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.intVal); }
	public WindowScrollList<E> setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowScrollList<E> setBorderColor(EColors colorIn) { return setBorderColor(colorIn.intVal); }
	public WindowScrollList<E> setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowScrollList<E> setVScrollDrawn(boolean valIn) { vScrollVis = valIn; if (vScroll != null) { vScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList<E> setHScrollDrawn(boolean valIn) { hScrollVis = valIn; if (hScroll != null) { hScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList<E> setResetDrawn(boolean valIn) { resetVis = valIn; if (reset != null) { reset.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList<E> renderVScrollBarThumb(boolean val) { if (vScroll != null) { vScroll.setRenderThumb(val); } else { vScrollVis = val; } return this; }
	public WindowScrollList<E> renderHScrollBarThumb(boolean val) { if (hScroll != null) { hScroll.setRenderThumb(val); } else { hScrollVis = val; } return this; }
	
	public double getListHeight() { return scrollableHeight - (isHScrollDrawn() ? hScroll.getDimensions().height : 0); }
	public double getListWidth() { return scrollableWidth - (isVScrollDrawn() ? vScroll.getDimensions().width : 0); }
	public boolean getDrawListObjects() { return drawListObjects; }
	public boolean isVScrollDrawn() { return vScrollVis && (vScroll != null ? vScroll.getHighVal() > vScroll.getVisibleAmount() : false); }
	public boolean isHScrollDrawn() { return hScrollVis && (hScroll != null ? hScroll.getHighVal() > hScroll.getVisibleAmount() : false); }
	public boolean isResetDrawn() { return resetVis && (isVScrollDrawn() || isHScrollDrawn()); }
	public WindowScrollBar<?> getVScrollBar() { return vScroll; }
	public WindowScrollBar<?> getHScrollBar() { return hScroll; }
	public EArrayList<IWindowObject<?>> getDrawnObjects() { return drawnListObjects; }
	public EArrayList<IWindowObject<?>> getListObjects() { return listContents; }
	public EArrayList<IWindowObject<?>> getAddingListObjects() { return listObjsToBeAdded; }
	
}
