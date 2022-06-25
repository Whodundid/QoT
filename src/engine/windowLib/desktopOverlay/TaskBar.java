package engine.windowLib.desktopOverlay;

import java.util.Collections;

import assets.textures.WindowTextures;
import engine.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import engine.windowLib.windowObjects.utilityObjects.RightClickMenu;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.ScreenLocation;
import main.QoT;

public class TaskBar<E> extends WindowObject<E> {
	
	public static int drawSize() { return QoT.getHeight() / 20; }
	
	public static EArrayList<TaskButton> buttons = new EArrayList();
	protected static EArrayList<IWindowParent> toAdd = new EArrayList();
	protected static EArrayList<IWindowParent> toRemove = new EArrayList();
	ScreenLocation drawSide = ScreenLocation.TOP;
	
	//--------------
	// Constructors
	//--------------
	
	public TaskBar() { this(false); }
	public TaskBar(boolean fromScratch) {
		reorient();
		if (fromScratch) buildFromScratch();
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static synchronized void windowOpened(IWindowParent window) { toAdd.add(window); }
	public static synchronized void windowClosed(IWindowParent window) { toRemove.add(window); }
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		//validate buttons
		updateLists();
		
		//draw background
		drawRect(startX, startY - 1, res.getWidth(), endY, 0xff232323);
		drawRect(startX - 1, endY - 1, res.getWidth(), endY, 0xff000000);
		
		if (buttons.isEmpty() && toAdd.isEmpty()) {
			drawString("No currently open windows..", startX + 5, midY - 4, EColors.lgray);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			RightClickMenu menu = new RightClickMenu();
			menu.setTitle("Taskbar");
			menu.setActionReceiver(this);
			menu.setGenericObject(this);
			menu.addOption("Open Window..", WindowTextures.plus);
			QoT.getTopRenderer().displayWindow(menu, ObjectPosition.CURSOR_CORNER);
		}
		
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof TaskButton b) {
			if (args.length > 0 && args[0] instanceof WindowParent window) {
				if (window.isMinimized()) window.setMinimized(false);
				window.bringToFront();
				QoT.getTopRenderer().setFocusedObject(window);
			}
		}
		if (object instanceof RightClickMenu rcm) {
			Object genObj = rcm.getGenericObject();
			
			if (genObj == this) {
				if (genObj.equals("Open Window..")) {
					//QoT.displayWindow(new EMCGuiSelectionList(), CenterType.screen);
				}
			}
			else if (genObj instanceof WindowParent w) {
				if (genObj == "Pin" ) {
					System.out.println("pinning: " + rcm.getGenericObject().getClass().getSimpleName());
				}
				
				if (genObj == "New Window") {
					try {
						IWindowParent n = w.getClass().getDeclaredConstructor().newInstance();
						IWindowParent old = null;
						
						EArrayList<? extends IWindowParent> windows = QoT.getTopRenderer().getAllWindowInstances(n);
						if (windows != null && windows.isNotEmpty()) {
							old = windows.get(windows.size() - 1);
						}
						
						ObjectPosition pos = (old != null && !old.isMaximized()) ? ObjectPosition.OBJECT_INDENT : ObjectPosition.SCREEN_CENTER;
						QoT.getTopRenderer().displayWindow(n, old, true, false, false, pos);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (genObj.equals("Close") || genObj.equals("Close All")) {
					if (w != null) {
						Class c = w.getClass();
						EArrayList<WindowParent> windows = QoT.getTopRenderer().getAllWindowInstances(c);
						windows.forEach(p -> p.close());
					}
				}
				
				if (genObj.equals("Recenter")) {
					if (w != null) {
						Class c = w.getClass();
						EArrayList<WindowParent> windows = QoT.getTopRenderer().getAllWindowInstances(c);
						
						if (windows.size() == 1) {
							WindowParent p = windows.get(0);
							WindowHeader h = p.getHeader();
							TaskBar b = QoT.getTopRenderer().getTaskBar();
							
							double hh = (h != null) ? h.height : 0;
							double bh = (b != null) ? b.height : 0;
							double oH = hh + bh;
							double maxW = NumberUtil.clamp(p.width, 0, res.getWidth() - 40);
							double maxH = NumberUtil.clamp(p.height, 0, res.getHeight() - 40 - oH);
							
							p.setSize(maxW, maxH);
							p.centerObjectWithSize(maxW, maxH);
							p.setPosition(p.startX, p.startY + hh);
							p.reInitObjects();
						}
					}
				} //recenter
			}
		} //instanceof rcm
	}
	
	@Override
	public void close() {
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		super.close();
	}
	
	//---------
	// Methods
	//---------
	
	public void forceUpdate() {
		updateLists();
	}
	
	//---------
	// Getters
	//---------
	
	public boolean isVertical() {
		return drawSide == ScreenLocation.LEFT || drawSide == ScreenLocation.RIGHT;
	}
	
	//---------
	// Setters
	//---------
	
	public void setDrawSide(ScreenLocation sideIn) {
		if (sideIn != ScreenLocation.OUT) {
			drawSide = sideIn;
			reorient();
			reInitObjects();
		}
	}
	
	//------------------------
	//TaskBar Internal Methods
	//------------------------
	
	private void reorient() {
		res = QoT.getWindowSize();
		int w = res.getWidth();
		int h = res.getHeight();
		
		//change draw dimensions
		switch (drawSide) {
		case TOP: setDimensions(0, 0, w, drawSize()); break;
		case LEFT: setDimensions(0, 0, drawSize(), h); break;
		case RIGHT: setDimensions(w - drawSize(), 0, drawSize(), h); break;
		case BOT: setDimensions(0, h - drawSize(), w, drawSize()); break;
		default: break;
		}
		
		repositionButtons();
	}
	
	private void updateLists() {
		try {
			EArrayList<IWindowObject<?>> removeGhosts = new EArrayList();
			for (IWindowObject<?> o : getObjects()) {
				if (o instanceof TaskButton tb) {
					if (!QoT.getTopRenderer().isWindowOpen(tb.getWindowType().getClass()))
						removeGhosts.add(o);
				}
			}
			
			for (IWindowObject<?> o : removeGhosts) {
				removeObject(o);
			}
			
			//check for ghost buttons
			for (TaskButton b : buttons) {
				if (!QoT.getTopRenderer().isWindowOpen(b.getWindowType().getClass()))
					toRemove.add(b.getWindowType());
			}
			
			//process objects to be added
			EUtil.ifForEach(toAdd.isNotEmpty(), toAdd, p -> addButton(p));
			toAdd.clear();
			
			//process objects to be removed
			if (toRemove.isNotEmpty()) {
				EArrayList<TaskButton> removing = new EArrayList();
				
				for (IWindowParent p : toRemove) {
					for (TaskButton b : buttons) {
						if (b.getWindowType().getClass() != p.getClass()) continue;
						//only remove if there are no more
						if (b.getTotal() == 0) removing.add(b);
					}
				}
				
				//remove old ones
				buttons.removeAll(removing);
				removing.forEach(b -> removeObject(null, b));
				
				//update the remaining
				buttons.forEach(b -> b.update());
				repositionButtons();
				
				toRemove.clear();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addButton(IWindowParent window) {
		if (window.showInTaskBar()) {
			if (!typeExists(window)) {
				TaskButton b = new TaskButton(this, window);
				
				int sX = 0;
				int sY = 0;
				int w = drawSize() + 2;
				int h = drawSize() - 1;
				boolean v = isVertical();
				
				if (v) { sY += (w * buttons.size()) + (0 * buttons.size()); }
				else { sX += (w * buttons.size()) + (0 * buttons.size()); }
				
				b.setDimensions(sX, sY, w, h);
				
				//add to bar
				buttons.add(b);
				addObject(b);
			}
			else {
				for (TaskButton b : buttons) {
					if (b.getWindowType().getClass() == window.getClass()) {
						b.update();
					}
				}
			} //else
		}
	}
	
	private void repositionButtons() {
		for (int i = 0; i < buttons.size(); i++) {
			TaskButton b = buttons.get(i);
			
			int sX = 0;
			int sY = 0;
			int w = drawSize() + 2;
			int h = drawSize() - 1;
			boolean v = isVertical();
			
			if (v) { sY += (w * i) + (0 * i); }
			else { sX += (w * i) + (0 * i); }
			
			b.setDimensions(sX, sY, w, h);
		}
	}
	
	private boolean typeExists(IWindowParent testIn) {
		if (testIn != null) {
			if (buttons.isEmpty()) return false;
			
			for (TaskButton b : buttons) {
				if (b.getWindowType().getClass() == testIn.getClass()) return true;
			}
		}
		return false;
	}
	
	private void buildFromScratch() {
		windowObjects.clear();
		objsToBeAdded.clear();
		objsToBeRemoved.clear();
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		
		EArrayList<IWindowParent> windows = QoT.getTopRenderer().getAllActiveWindows();
		EArrayList<IWindowParent> filtered = new EArrayList();
		EArrayList<TaskButton> toBuild = new EArrayList();
		
		for (IWindowParent w : windows) {
			boolean contains = false;
			for (IWindowParent f : filtered) {
				//if (w.getClass() == SettingsWindowMain.class) continue;
				if (f.getClass() == w.getClass()) {
					contains = true;
					break;
				}
			}
			if (!contains) filtered.add(w);
		}
		
		filtered.forEach(w -> toBuild.add(new TaskButton(this, w)));
		Collections.sort(toBuild);
		
		for (TaskButton b : toBuild) {
			buttons.add(b);
			addObject(b);
		}
		
		repositionButtons();
	}
	
}
