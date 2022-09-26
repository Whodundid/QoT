package envision.topOverlay.desktopOverlay;

import java.util.Collections;

import envision.windowLib.windowObjects.utilityObjects.RightClickMenu;
import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import eutil.strings.EStringUtil;
import game.QoT;
import game.assets.textures.window.WindowTextures;

public class TaskBar<E> extends WindowObject<E> {
	
	//----------------------------------------------
	
	public static int drawSize() { return QoT.getHeight() / 20; }
	
	//----------------------------------------------
	
	//--------
	// Fields
	//--------
	
	public static EArrayList<TaskBarButton<?>> buttons = new EArrayList();
	protected static EArrayList<IWindowParent<?>> toAdd = new EArrayList();
	protected static EArrayList<IWindowParent<?>> toRemove = new EArrayList();
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
	
	public static synchronized void windowOpened(IWindowParent<?> window) { toAdd.add(window); }
	public static synchronized void windowClosed(IWindowParent<?> window) { toRemove.add(window); }
	
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
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object instanceof TaskBarButton<?> b) {
			if (args.length > 0 && args[0] instanceof WindowParent<?> window) {
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
			else if (genObj instanceof WindowParent<?> w) {
				if (args.length != 1 || !(args[0] instanceof String)) return;
				String selection = (String) args[0];
				
				if (genObj == "Pin") {
					System.out.println("pinning: " + genObj.getClass().getSimpleName());
				}
				
				if (genObj == "New Window") {
					try {
						var n = w.getClass().getDeclaredConstructor().newInstance();
						IWindowParent<?> old = null;
						
						var windows = QoT.getTopRenderer().getAllWindowInstances(n.getClass());
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
				
				if (EStringUtil.equalsAny(selection, "Close", "Close All")) {
					if (w != null) {
						var c = w.getClass();
						var windows = QoT.getTopRenderer().getAllWindowInstances(c);
						windows.forEach(p -> p.close());
					}
				}
				
				if (selection.equals("Recenter") && w != null) {
					var c = w.getClass();
					EArrayList<? extends WindowParent> windows = QoT.getTopRenderer().getAllWindowInstances(c);
					
					if (windows.size() == 1) {
						var p = windows.get(0);
						var h = p.getHeader();
						var b = QoT.getTopRenderer().getTaskBar();
						
						double hh = (h != null) ? h.height : 0;
						double bh = (b != null) ? b.height : 0;
						double oH = hh + bh;
						double maxW = ENumUtil.clamp(p.width, 0, res.getWidth() - 40);
						double maxH = ENumUtil.clamp(p.height, 0, res.getHeight() - 40 - oH);
						
						p.setSize(maxW, maxH);
						p.centerObjectWithSize(maxW, maxH);
						p.setPosition(p.startX, p.startY + hh);
						p.reInitChildren();
						p.bringToFront();
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
			reInitChildren();
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
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
			EArrayList<IWindowObject<?>> removeGhosts = new EArrayList<>();
			for (var o : getChildren()) {
				if (o instanceof TaskBarButton<?> tb) {
					if (!QoT.getTopRenderer().isWindowOpen(tb.getWindowType().getClass()))
						removeGhosts.add(o);
				}
			}
			
			for (var o : removeGhosts) {
				removeObject(o);
			}
			
			//check for ghost buttons
			for (TaskBarButton<?> b : buttons) {
				if (!QoT.getTopRenderer().isWindowOpen(b.getWindowType().getClass()))
					toRemove.add(b.getWindowType());
			}
			
			//process objects to be added
			EUtil.ifForEach(toAdd.isNotEmpty(), toAdd, p -> addButton(p));
			toAdd.clear();
			
			//process objects to be removed
			if (toRemove.isNotEmpty()) {
				EArrayList<TaskBarButton<?>> removing = new EArrayList<>();
				
				for (var p : toRemove) {
					for (var b : buttons) {
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
	
	private void addButton(IWindowParent<?> window) {
		if (window.showInTaskBar()) {
			if (!typeExists(window)) {
				TaskBarButton<?> b = new TaskBarButton(this, window);
				
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
				for (var b : buttons) {
					if (b.getWindowType().getClass() == window.getClass()) {
						b.update();
					}
				}
			} //else
		}
	}
	
	private void repositionButtons() {
		for (int i = 0; i < buttons.size(); i++) {
			var b = buttons.get(i);
			
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
	
	private boolean typeExists(IWindowParent<?> testIn) {
		if (testIn != null) {
			if (buttons.isEmpty()) return false;
			
			for (var b : buttons) {
				if (b.getWindowType().getClass() == testIn.getClass()) return true;
			}
		}
		return false;
	}
	
	private void buildFromScratch() {
		getChildren().clear();
		getAddingChildren().clear();
		getRemovingChildren().clear();
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		
		var windows = QoT.getTopRenderer().getAllActiveWindows();
		EArrayList<IWindowParent<?>> filtered = new EArrayList<>();
		EArrayList<TaskBarButton<?>> toBuild = new EArrayList<>();
		
		for (var w : windows) {
			boolean contains = false;
			for (var f : filtered) {
				//if (w.getClass() == SettingsWindowMain.class) continue;
				if (f.getClass() == w.getClass()) {
					contains = true;
					break;
				}
			}
			if (!contains) filtered.add(w);
		}
		
		filtered.forEach(w -> toBuild.add(new TaskBarButton(this, w)));
		Collections.sort(toBuild);
		
		for (var b : toBuild) {
			buttons.add(b);
			addObject(b);
		}
		
		repositionButtons();
	}
	
	public void onScreenResized() {
		setDimensions(startX, startX, QoT.getWidth(), drawSize());
		buildFromScratch();
	}
	
}
