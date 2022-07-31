package engine.topOverlay.desktopOverlay;

import assets.textures.TaskBarTextures;
import assets.textures.WindowTextures;
import engine.renderEngine.textureSystem.GameTexture;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.utilityObjects.RightClickMenu;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import main.QoT;

public class TaskBarButton<E> extends WindowButton<E> implements Comparable<TaskBarButton<?>> {
	
	//--------
	// Fields
	//--------
	
	private TaskBar<?> parentBar;
	private IWindowParent base;
	private boolean pressed = false;
	private int total = 0;
	protected boolean pinned = false;
	private long earliest = 0l;
	private boolean drawingList = false;
	private boolean listMade = false;
	private WindowDropDown dropDown;
	
	//--------------
	// Constructors
	//--------------
	
	public TaskBarButton(TaskBar<?> barIn, IWindowParent<?> baseIn) {
		super(barIn);
		parentBar = barIn;
		base = baseIn;
		
		//Set the hover text -- use the class name if an object name is not set.
		if (base != null) {
			String name = base.getObjectName();
			setHoverText(name.equals("noname") ? base.getClass().getSimpleName() : name);
		}
		
		//get number of instances
		update();
		
		setDrawTextures(false);
		setImage();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public int compareTo(TaskBarButton<?> b) {
		return Long.compare(getEarliest(), b.getEarliest());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		//draw a highlight overlay if the mouse is over the button
		if (isMouseOver()) {
			drawRect(0xaa505050);
			getWindows().forEach(w -> w.drawHighlightBorder());
		}
		
		//draw highlight if the currently focused window is of the same type as this button's base
		var o = QoT.getTopRenderer().getFocusedObject();
		if (base != null &&
			o != null &&
			o.getWindowParent() != null &&
			o.getWindowParent().getClass() == base.getClass())
		{
			drawRect(0xaa808080);
		}
		
		//draw icon
		double iW = 0;
		double iH = 0;
		double sX = startX;
		double sY = startY;
		double w = width;
		double h = height;
		double smaller = 0;
		
		GameTexture icon = btnTexture;
		if (icon == null) icon = TaskBarTextures.window;
		
		iW = icon.getWidth();
		iH = icon.getHeight();
		w = NumberUtil.clamp(iW, 0, width - 3);
		h = NumberUtil.clamp(iH, 0, height - 3);
		smaller = (w < h) ? w : h;
		sX = startX + (width - smaller) / 2.0;
		sY = startY + 0.5 + (height - smaller) / 2.0;
		drawTexture(icon, sX, sY, smaller, smaller);
		
		super.drawObject(mXIn, mYIn);
		
		//draw number of windows
		drawTotal();
		
		if (!pressed && (isDrawingHover() || (dropDown != null && dropDown.isMouseInside(mXIn, mYIn)))) {
			if (!listMade) createList();
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (isEnabled() && willBeDrawn()) {
			pressedButton = button;
			if (runActionOnPress) onPress(button);
			else if (button == 0) {
				playPressSound();
				
				pressed = true;
				
				if (total == 1) {
					destroyList();
					var p = getWindows().get(0);
					
					if (p.isMinimizable()) {
						//check if at front
						if (p == QoT.getTopRenderer().getAllActiveWindows().getLast()) {
							if (p.isMinimized()) performAction(p);
							else p.setMinimized(true);
						}
						else performAction(p);
					}
					else performAction(p);
					
				}
				else if (total >= 1) {
					if (!listMade) createList();
					else destroyList();
				}
			}
			else if (button == 1) {
				var windows = getWindows();
				
				RightClickMenu rcm = new RightClickMenu();
				rcm.setTitle(getHoverText());
				rcm.setActionReceiver(parentBar);
				rcm.setGenericObject(base);
				
				rcm.addOption(total == 1 ? "Close" : "Close All", WindowTextures.close);
				if (QoT.isDebugMode() && total == 1 && windows.get(0).isPinnable()) {
					rcm.addOption(windows.get(0).isPinned() ? "Unpin" : "Pin", WindowTextures.pin);
				}
				if (total == 1) rcm.addOption("Recenter", WindowTextures.move);
				if (base.showInLists()) rcm.addOption("New Window", WindowTextures.plus);
				
				QoT.getTopRenderer().displayWindow(rcm, ObjectPosition.CURSOR_CORNER);
			}
		}
	}
	
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		if (pressed) pressed = false;
		if (listMade && (dropDown != null && !dropDown.isMouseInside(mXIn, mYIn))) destroyList();
		super.mouseExited(mXIn, mYIn);
	}
	
	@Override
	public void close() {
		super.close();
		destroyList();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		System.out.println(object);
	}
	
	//---------
	// Methods
	//---------
	
	/** Updates the visible number of window instances represented by this button. */
	public void update() {
		total = getTotal();
	}
	
	public void destroyList() {
		if (dropDown != null) {
			dropDown.close();
			dropDown = null;
			QoT.getTopRenderer().revealHiddenObjects();
			for (var w : QoT.getTopRenderer().getAllActiveWindows()) {
				w.setDrawWhenMinimized(false);
			}
		}
		listMade = false;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	/** Sets the image of the window that this button represents. */
	private void setImage() {
		if (base != null) setTextures(base.getWindowIcon(), base.getWindowIcon());
		else setButtonTexture(WindowTextures.problem);
	}
	
	private void drawTotal() {
		//only draw if there is more than 1 instance
		if (total > 1) {
			drawStringC(total, midX + 1, endY - 8, EColors.lime);
		}
	}
	
	private void createList() {
		dropDown = new WindowDropDown(this, startX, endY, 20, false);
		
		var windows = getWindows();
		
		for (int i = 1; i <= windows.size(); i++) {
			var p = windows.get(i - 1);
			dropDown.addEntry(i + ": " + EColors.green + p.getObjectName(), EColors.lorange, p);
		}
		
		QoT.getTopRenderer().addChild(dropDown);
		
		listMade = true;
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowParent<?> getWindowType() { return base; }
	public TaskBarButton<?> setPinned(boolean val) { pinned = val; return this; }
	public boolean isPinned() { return pinned; }
	public long getEarliest() { return earliest; }
	
	/**
	 * Returns a list of all current window instances of the same type
	 * that this button represents.
	 */
	public EArrayList<IWindowParent<?>> getWindows() {
		return (EArrayList<IWindowParent<?>>) QoT.getTopRenderer().getAllWindowInstances(base.getClass());
	}
	
	/** Returns the total number of window instances that this button represents. */
	public int getTotal() {
		var windows = QoT.getTopRenderer().getAllWindowInstances(base.getClass());
		
		if (total != windows.size()) {
			earliest = Long.MAX_VALUE;
			for (var w : windows) {
				if (earliest < w.getInitTime()) earliest = w.getInitTime();
			}
		}
		
		return windows.size();
	}

}
