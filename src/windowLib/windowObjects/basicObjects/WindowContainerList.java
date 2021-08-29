package windowLib.windowObjects.basicObjects;

import eutil.storage.Box2;
import eutil.storage.BoxHolder;
import eutil.storage.EArrayList;
import eutil.storage.EDims;
import java.util.Iterator;
import windowLib.windowObjects.advancedObjects.header.WindowHeader;
import windowLib.windowObjects.advancedObjects.scrollList.WindowScrollList;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import windowLib.windowUtil.windowEvents.events.EventModify;

//Author: Hunter Bragg

public class WindowContainerList<E> extends WindowContainer<E> {
	
	WindowContainer<?> container;
	WindowScrollList<?> list;
	String title = "noname";
	protected EArrayList<IWindowObject<?>> containerContents = new EArrayList();
	protected EArrayList<IWindowObject<?>> containerObjsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject<?>> containerObjsToBeAdded = new EArrayList();
	protected boolean centerTitle = false;

	public WindowContainerList(IWindowObject<?> parentIn, int posX, int posY, int widthIn, int heightIn, String titleIn) {
		super(parentIn, posX, posY, widthIn, heightIn);
		title = titleIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); } //border
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); } //inner
		if (drawTitle) {
			titleAreaHeight = height >= 18 ? 18 : height;
			double drawWidth = titleWidth + 6;
			if (useCustomWidth) { drawWidth = titleAreaWidth; }
			if (drawTitleFullWidth) { drawWidth = width - 1; }
			drawRect(startX + 1, startY + 1, startX + drawWidth + 1, startY + titleAreaHeight, titleBorderColor);
			drawRect(startX + 1, startY + 1, startX + drawWidth, startY + titleAreaHeight - 1, titleBackgroundColor);
			drawStringWithShadow(title, startX + 4, startY + 5, titleColor);
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void move(double newX, double newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (!moveable) {
			EArrayList<IWindowObject<?>> objs = new EArrayList(windowObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IWindowObject<?>> it = objs.iterator();
			while (it.hasNext()) {
				IWindowObject<?> o = it.next();
				if (!o.isMoveable()) {
					if (o instanceof WindowParent<?>) {
						if (((WindowParent<?>) o).movesWithParent()) { o.move(newX, newY); }
					}
					else {
						if (containerContents.contains(o)) { o.setInitialPosition(o.getInitialPosition().getA() + newX, o.getInitialPosition().getB() + newY); }
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
	public WindowContainerList<E> setPosition(double newX, double newY) {
		EDims d = getDimensions();
		Box2<Integer, Integer> loc = new Box2(d.startX, d.startY);
		BoxHolder<IWindowObject<?>, Box2<Integer, Integer>> previousLocations = new BoxHolder();
		EArrayList<IWindowObject<?>> objs = EArrayList.combineLists(getObjects(), getAddingObjects());
		for (IWindowObject o : objs) {
			previousLocations.add(o, new Box2(o.getDimensions().startX - loc.getA(), o.getDimensions().startY - loc.getB()));
		}
		setDimensions(newX, newY, d.width, d.height);
		for (IWindowObject o : objs) {
			if (!o.isMoveable()) {
				Box2<Integer, Integer> oldLoc = previousLocations.getBoxWithA(o).getB();
				o.setInitialPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
				o.setPosition(newX + oldLoc.getA(), newY + oldLoc.getB());
			}
		}
		return this;
	}
	
	public EDims getListDimensions() {
		double w = (endX - 1) + (startX + 1);
		double h = (endY - 1) + (startY + titleAreaHeight);
		return new EDims(0, 0, w, h);
	}
	
	public WindowContainerList<E> addObjectToContainer(IWindowObject<?>... objsIn) {
		for (IWindowObject<?> o : objsIn) {
			try {
				if (o != null && o != this) {
					//if (o instanceof EnhancedGui) { continue; }
					if (o instanceof WindowHeader && hasHeader()) { continue; }
					
					EDims bounds = new EDims(startX + 1, startY + titleAreaHeight, endX - 1, endY - 1);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDims dims = o.getDimensions();
					o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					
					o.setParent(this).initObjects();
					if (o instanceof WindowParent) { ((WindowParent) o).initWindow(); }
					o.completeInit();
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IWindowObject<?> q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
					for (IWindowObject<?> q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					containerObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return this;
	}
	
	public WindowContainerList<E> removeObjectFromList(IWindowObject<?>... objsIn) {
		containerObjsToBeRemoved.add(objsIn);
		objsToBeRemoved.add(objsIn);
		return this;
	}
	
	@Override
	public WindowContainerList<E> removeObject(IWindowObject<?> obj, IWindowObject<?>... additional) {
		objsToBeRemoved.add(additional);
		containerObjsToBeRemoved.add(additional);
		return this;
	}
	
	public boolean drawsTitle() { return drawTitle; }
	public boolean drawsTitleCentered() { return centerTitle; }
	
	public WindowContainerList<E> setDrawTitleCentered(boolean val) { centerTitle = val; return this; }
	
}
