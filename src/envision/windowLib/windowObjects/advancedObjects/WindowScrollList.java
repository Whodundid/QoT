package envision.windowLib.windowObjects.advancedObjects;

import envision.inputHandlers.Keyboard;
import envision.renderEngine.GLSettings;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.actionObjects.WindowScrollBar;
import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.windowLib.windowUtil.windowEvents.events.EventModify;
import envision.windowLib.windowUtil.windowEvents.events.EventObjects;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class WindowScrollList<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected EArrayList<IWindowObject<?>> listContents = new EArrayList();
	protected EArrayList<IWindowObject<?>> drawnListObjects = new EArrayList();
	protected WindowScrollBar<?> vScroll, hScroll;
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
	public WindowScrollList(IWindowObject<?> parentIn, double xIn, double yIn, double widthIn, double heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = (int) widthIn - 2;
		scrollableHeight = (int) heightIn - 2;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		vScroll = new WindowScrollBar(this, height - 2, scrollableHeight, ScreenLocation.RIGHT, 7);
		hScroll = new WindowScrollBar(this, width - 2, scrollableWidth, ScreenLocation.BOT, 7);
		
		reset = new WindowButton(this, endX - 5, endY - 5, 5, 5);
		
		vScroll.setVisible(vScrollVis);
		hScroll.setVisible(hScrollVis);
		reset.setVisible(resetVis);

		addObject(vScroll, hScroll, reset);
		
		setListHeight(heightToBeSet);
		setListWidth(widthToBeSet);
		updateVisuals();
	}
	
	@Override
	public void drawObject_i(int mXIn, int mYIn) {
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
					for (var o : drawnListObjects) {
						if (o.willBeDrawn()) {
							if (!o.hasFirstDraw()) o.onFirstDraw_i();
							GLSettings.fullBright();
							//EDimension d = o.getDimensions();
							o.drawObject_i(mXIn, mYIn);
						}
					}
				}
				endScissor();
				
				if (isVScrollDrawn()) drawRect(endX - vScroll.width - 2, startY + 1, endX, endY, EColors.black);
				if (isHScrollDrawn()) drawRect(startX + 1, endY - hScroll.height - 2, endX, endY, EColors.black);
				
				//draw non list contents as normal (non scissored)
				for (var o : getChildren()) {
					if (o.willBeDrawn() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) o.onFirstDraw_i();
						GLSettings.fullBright();
	    	        	o.drawObject_i(mXIn, mYIn);
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
		updateDrawnObjects();
	}
	
	@Override
	public void move(double newX, double newY) {
		postEvent(new EventModify(this, this, ObjectModifyType.MOVE));
		
		if (isMoveable()) {
			EArrayList<IWindowObject<?>> objs = new EArrayList(getChildren());
			objs.addAll(getAddingChildren());
			objs.addAll(listContents);
			objs.addAll(listObjsToBeAdded);
			
			for (var o : objs) {
				if (o.isMoveable()) {
					if (o instanceof WindowParent p) {
						if (p.movesWithParent()) o.move(newX, newY);
					}
					else {
						if (listContents.contains(o)) {
							var initial = o.getInitialPosition();
							o.setInitialPosition(initial.getA() + newX, initial.getB() + newY);
						}
						o.move(newX, newY);
					}
				}
			}
			
			startX += newX;
			startY += newY;
			var boundary = getBoundaryEnforcer();
			if (boundary != null) boundary.move(newX, newY);
			setDimensions(startX, startY, width, height);
		}
	}
	
	@Override
	public void setPosition(double newX, double newY) {
		if (isMoveable()) {
			EDimension d = getDimensions();
			var loc = new Box2<>(d.startX, d.startY);
			var previousLocations = new BoxList<IWindowObject<?>, Box2<Double, Double>>();
			var objs = getCombinedChildren();
			
			for (var o : objs) {
				var dims = o.getDimensions();
				previousLocations.add(o, new Box2<>(dims.startX - loc.getA(), dims.startY - loc.getB()));
			}
			
			setDimensions(newX, newY, d.width, d.height);
			
			for (var o : objs) {
				if (o.isMoveable()) {
					var oldLoc = previousLocations.getBoxWithA(o).getB();
					o.setInitialPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
					o.setPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
				}
			}
			
			// Now handle list objects
			
			objs.clear();
			previousLocations.clear();
			
			objs.addAll(listContents);
			objs.addAll(listObjsToBeAdded);
			
			for (var o : objs) {
				var dims = o.getDimensions();
				previousLocations.add(o, new Box2<>(dims.startX - loc.getA(), dims.startY - loc.getB()));
			}
			
			for (var o : objs) {
				double eX = endX - (isVScrollDrawn() ? vScroll.width + 2 : 1);
				double eY = endY - (isHScrollDrawn() ? hScroll.height - 4 : 1);
				
				EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
				
				o.setBoundaryEnforcer(getDimensions());
				for (var q : o.getCombinedChildren()) q.setBoundaryEnforcer(bounds);
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == reset) {
			vScroll.reset();
			hScroll.reset();
		}
		
		if (object == vScroll || object == hScroll) {
			double vScrollPos = vScroll.getScrollPos() - vScroll.getVisibleAmount();
			double hScrollPos = hScroll.getScrollPos() - hScroll.getVisibleAmount();
			
			for (var o : EArrayList.combineLists(listContents, listObjsToBeAdded)) {
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
	public EArrayList<IWindowObject<?>> getCombinedChildren() {
		var r = new EArrayList<>(getChildren());
		r.addAll(getAddingChildren());
		r.addAll(listContents);
		r.addAll(listObjsToBeAdded);
		return r;
	}
	
	@Override
	public EArrayList<IWindowObject<?>> getAllChildren() {
		EArrayList<IWindowObject<?>> foundObjs = new EArrayList<>();
		EArrayList<IWindowObject<?>> objsWithChildren = new EArrayList<>();
		EArrayList<IWindowObject<?>> workList = new EArrayList<>();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		getChildren().forEach(o -> {
			foundObjs.add(o);
			if (!o.getCombinedChildren().isEmpty()) objsWithChildren.add(o);
		});
		listContents.forEach(o -> {
			foundObjs.add(o);
			if (!o.getCombinedChildren().isEmpty()) objsWithChildren.add(o);
		});
		
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			foundObjs.addAll(workList);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.filterForEach(o -> o.getCombinedChildren().isNotEmpty(), objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		}
		
		return foundObjs;
	}

	@Override
	public void updateBeforeNextDraw(int mXIn, int mYIn) {
		WindowScrollList.super.updateBeforeNextDraw(mXIn, mYIn);
		if (!listObjsToBeRemoved.isEmpty()) removeListObjects();
		if (!listObjsToBeAdded.isEmpty()) addListObjects();
	}
	
	@Override
	public void removeObject(IWindowObject<?>... objs) {
		getRemovingChildren().add(objs);
		listObjsToBeRemoved.add(objs);
	}
	
	//---------
	// Methods
	//---------
	
	public EDimension getListDimensions() {
		double w = (endX - (isVScrollDrawn() ? vScroll.width + 3 : 1)) - startX;
		double h = (endY - (isHScrollDrawn() ? hScroll.height - 4 : 1)) - startY - 2;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		vScroll.setScrollPos(0);
		vScroll.setScrollPos(0);
	}
	
	public void fitItemsInList() { fitItemsInList(0, 0); }
	public void fitItemsInList(int overShootX, int overShootY) {
		double right = 0;
		double down = 0;
		
		//get both the current list objects and those being added
		var objs = EArrayList.combineLists(listContents, listObjsToBeAdded);
		var ignored = new EArrayList<>(ignoreList);
		EArrayList<IWindowObject<?>> aObjs = new EArrayList<>();
		
		
		for (var o : objs) {
			if (listObjsToBeRemoved.notContains(o) && ignored.notContains(o)) aObjs.add(o);
		}
		
		//find right
		for (var o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endX > right) right = od.endX;
		}
		
		//find down
		for (var o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endY > down) down = od.endY;
		}
		
		//prevent negative values
		double w = ENumUtil.clamp((right - startX) + overShootX, 0, Integer.MAX_VALUE);
		double h = ENumUtil.clamp((down - startY) + overShootY, 0, Integer.MAX_VALUE);
		
		setListSize(w, h);
	}
	
	public void setListSize(double widthIn, double heightIn) {
		setListWidth(widthIn);
		setListHeight(heightIn);
	}
	
	public void setListWidth(double widthIn) {
		if (hScroll != null) {
			scrollableWidth = (int) widthIn;
			hScroll.setHighVal((int) widthIn);
			updateVisuals();
		}
		else widthToBeSet = widthIn;
	}
	
	public void setListHeight(double heightIn) {
		if (vScroll != null) {
			scrollableHeight = (int) heightIn;
			vScroll.setHighVal((int) heightIn);
			updateVisuals();
		}
		else heightToBeSet = heightIn;
	}
	
	public void growList(double amount) {
		growListWidth(amount);
		growListHeight(amount);
	}
	
	public void growListWidth(double amount) {
		if (hScroll != null) {
			setListWidth(getListWidth() + amount);
			updateVisuals();
		}
		else widthToBeSet += amount;
	}
	
	public void growListHeight(double amount) {
		if (vScroll != null) {
			setListHeight(getListHeight() + amount);
			updateVisuals();
		}
		else heightToBeSet += amount;
	}
	
	public void addAndIgnore(IWindowObject<?>... objsIn) {
		addToIgnoreList(objsIn);
		addObjectToList(objsIn);
	}
	
	public void addObjectToList(IWindowObject<?>... objsIn) { addObjectToList(true, objsIn); }
	public void addObjectToList(boolean useRelativeCoords, IWindowObject<?>... objsIn) {
		for (var o : objsIn) {
			if (o == null) continue;
			if (o == this) continue;
			try {
				double eX = endX - (isVScrollDrawn() ? vScroll.width + 2 : 2);
				double eY = endY - (isHScrollDrawn() ? hScroll.height - 4 : 1);
				
				EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
				
				//apply offset to all added objects so their location is relative to this scrollList
				EDimension dims = o.getDimensions();
				if (useRelativeCoords) {
					o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
				}
				
				o.setParent(this);
				o.initChildren();
				o.onChildrenInit_i();
				
				//limit the boundary of each object to the list's boundary
				o.setBoundaryEnforcer(bounds);
				for (var q : o.getCombinedChildren()) q.setBoundaryEnforcer(bounds);
				
				//replace the original initial position coordinates with the relative ones
				o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
				
				listObjsToBeAdded.add(o);
				
				if (o instanceof WindowParent p) p.initWindow();
				o.onInit_i();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeObjectFromList(IWindowObject<?>... objsIn) {
		listObjsToBeRemoved.add(objsIn);
	}
	
	public void clearList() {
		drawnListObjects.clear();
		listContents.clear();
		listObjsToBeAdded.clear();
		
		reInitChildren();
	}
	
	public void clearIgnoreList() { ignoreList = new EArrayList(); }
	public void setIgnoreList(IWindowObject<?>... objects) {
		ignoreList = new EArrayList<IWindowObject<?>>().add(objects);
	}
	
	public void addToIgnoreList(IWindowObject<?>... objects) {
		if (ignoreList == null) ignoreList = new EArrayList<IWindowObject<?>>();
		ignoreList.add(objects);
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void updateDrawnObjects() {
		drawnListObjects.clear();
		drawnListObjects.addAll(listContents.filter(o -> getDimensions().partiallyContains(o.getDimensions())));
		drawnListObjects.addAll(listObjsToBeAdded.filter(o -> getDimensions().partiallyContains(o.getDimensions())));
	}
	
	void removeListObjects() {
		for (int i = 0; i < listObjsToBeRemoved.size(); i++) {
			IWindowObject<?> o = listObjsToBeRemoved.get(i);
			if (o == null) continue;
			
			if (!o.equals(getTopParent().getFocusedObject())) {
				for (IWindowObject child : o.getChildren()) {
					if (child.equals(getTopParent().getFocusedObject())) child.relinquishFocus();
				}
			}
			else o.relinquishFocus();
			
			o.properties().isBeingRemoved = true;
			listContents.remove(o);
			o.onRemoved_i();
			o.properties().isBeingRemoved = false;
			postEvent(new EventObjects(this, o, ObjectEventType.OBJECT_REMOVED));
		}
		
		listObjsToBeRemoved.clear();
		updateDrawnObjects();
	}
	
	void addListObjects() {
		for (var o : listObjsToBeAdded) {
			//prevent null children and self additions
			if (o == null || o == this) continue;
			
			o.properties().isBeingAdded = true;
			listContents.add(o);
			o.onAdded_i();
			o.properties().isBeingAdded = false;
			postEvent(new EventObjects(this, o, ObjectEventType.OBJECT_ADDED));
		}
		
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
	
	//---------
	// Getters
	//---------
	
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
	
	//---------
	// Setters
	//---------
	
	public void setDrawListObjects(boolean val) { drawListObjects = val; }
	public void setAllowScrolling(boolean val) { allowScrolling = val; }
	public void setBackgroundColor(EColors colorIn) { setBackgroundColor(colorIn.intVal); }
	public void setBackgroundColor(int colorIn) { backgroundColor = colorIn; }
	public void setBorderColor(EColors colorIn) { setBorderColor(colorIn.intVal); }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setVScrollDrawn(boolean valIn) { vScrollVis = valIn; if (vScroll != null) { vScroll.setVisible(valIn); } updateVisuals(); }
	public void setHScrollDrawn(boolean valIn) { hScrollVis = valIn; if (hScroll != null) { hScroll.setVisible(valIn); } updateVisuals(); }
	public void setResetDrawn(boolean valIn) { resetVis = valIn; if (reset != null) { reset.setVisible(valIn); } updateVisuals(); }
	public void renderVScrollBarThumb(boolean val) { if (vScroll != null) { vScroll.setRenderThumb(val); } else { vScrollVis = val; } }
	public void renderHScrollBarThumb(boolean val) { if (hScroll != null) { hScroll.setRenderThumb(val); } else { hScrollVis = val; }  }
	
}
