package envision.engine.windows.windowTypes;

import java.util.Stack;

import envision.Envision;
import envision.debug.DebugFunctions;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.developerDesktop.taskbar.TaskBar;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class WindowParent extends WindowObject implements IWindowParent, Comparable<WindowParent> {

	//----------------
	// Static Methods
	//----------------
	
	public static int defaultWidth = 220, defaultHeight = 255;
	private static volatile int curWindowID = 0;
	
	/** Returns the next available id that will be assigned to a requesting object. */
	public static synchronized int getNextWindowPID() { return curWindowID++; }
	
	//--------
	// Fields
	//--------
	
	public WindowParent windowInstance;
	protected WindowHeader header;
	protected int windowZLevel = 0;
	protected boolean moveWithParent = false;
	protected boolean pinned = false;
	protected boolean pinnable = false;
	protected ScreenLocation maximized = ScreenLocation.OUT;
	protected boolean minimizable = true;
	protected boolean minimized = false;
	protected boolean maximizable = false;
	protected boolean drawMinimized = false;
	protected boolean drawDefaultBackground = false;
	protected boolean highlighted = false;
	protected Stack<IWindowParent> WindowHistory = new Stack();
	protected EList<String> aliases = EList.newList();
	protected GameTexture windowIcon = null;
	protected Dimension_d preMaxFull = new Dimension_d();
	protected Dimension_d preMaxSide = new Dimension_d();
	protected boolean showInTaskBar = true;
	protected long initTime = 0l;
	protected long windowPID = -1;
	
	//--------------
	// Constructors
	//--------------

	/** By default, set the parent to the QoT top overlay. */
	
	public WindowParent() { this(Envision.getActiveTopParent(), null); }
	public WindowParent(IWindowParent oldGuiIn) { this(Envision.getActiveTopParent(), oldGuiIn); }
	public WindowParent(IWindowObject parentIn) { this(parentIn, null); }
	public WindowParent(IWindowObject parentIn, IWindowParent oldGuiIn) {
		__INIT__();
		initDefaultPos(parentIn);
		pullHistoryFrom(oldGuiIn);
	}
	
	public WindowParent(int xPos, int yPos) {
		__INIT__();
	}
	
	public WindowParent(int xPos, int yPos, IWindowParent oldGuiIn) {
		__INIT__();
		pullHistoryFrom(oldGuiIn);
	}
	
	public WindowParent(IWindowObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public WindowParent(IWindowObject parentIn, int xPos, int yPos, IWindowParent oldGuiIn) {
		__INIT__();
		initDefaultDims(parentIn, xPos, yPos);
		pullHistoryFrom(oldGuiIn);
	}
	
	public WindowParent(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public WindowParent(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn, IWindowParent oldGuiIn) {
		__INIT__();
		init(parentIn, xIn, yIn, widthIn, heightIn);
		pullHistoryFrom(oldGuiIn);
		preMaxFull = new Dimension_d(xIn, yIn, widthIn, heightIn);
	}
	
	private void __INIT__() {
		initTime = System.currentTimeMillis();
		windowInstance = this;
		windowPID = getNextWindowPID();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public int compareTo(WindowParent p) {
		return Long.compare(initTime, p.getInitTime());
	}
	
	@Override
	public void drawObject_i(int mXIn, int mYIn) {
		if (drawDefaultBackground) drawDefaultBackground();
		super.drawObject_i(mXIn, mYIn);
		if (!willBeDrawn() && Envision.isDebugMode()) return;
		
		if (Envision.isDebugMode()) {
			if (!isMaximized()) {
				double y = hasHeader() ? getHeader().startY - FontRenderer.FONT_HEIGHT : startY - FontRenderer.FONT_HEIGHT;
				int pos = 0;
				//int half = -1;
				String draw = "";
				String time = String.valueOf(initTime);
				
				if (time.length() > 6) time = time.substring(time.length() - 6);
				
				if (DebugFunctions.drawWindowPID) {
					pos = FontRenderer.strWidth("ID: " + getWindowID());
					draw = EColors.mc_aqua + "ID: " + EColors.yellow + getWindowID();
					drawRect(startX, y - 1, startX + pos + 13, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
	                drawRect(startX + 1, y, startX + pos + 12, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
	                pos += 13;
	                draw += " ";
				}
				
				if (DebugFunctions.drawWindowInit) {
				    var sX = startX + pos;
					pos += (int) strWidth("InitTime: " + time);
					draw += EColors.mc_aqua + "InitTime: " + EColors.yellow + time;
					drawRect(sX, y - 1, startX + pos + 13, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
	                drawRect(sX + 1, y, startX + pos + 12, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
	                pos += 13;
                    draw += " ";
				}
				
//				drawRect(startX, y - 1, startX + pos + 5, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
//				drawRect(startX + 1, y, startX + pos + 4, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
//				if (half > 0) drawRect(startX + half, y, startX + half + 1, y + FontRenderer.FONT_HEIGHT, EColors.black);
				
				if (DebugFunctions.drawWindowDimensions) {
					String dims = "[" + (int) startX + ", " + (int) startY + ", " + (int) width + " " + (int) height + "]";
					int eX = FontRenderer.strWidth(dims);
					var sX = startX + pos;
					pos += eX;
					draw += EColors.seafoam + dims;
					
					drawRect(sX, y - 1, startX + pos + 13, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
					drawRect(sX + 1, y, startX + pos + 12, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
	                pos += 13;
	                draw += " ";
				}
				
				drawString(draw, startX + 3, y + 3);
			}
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void close() {
		if (getTopParent() == Envision.getTopScreen()) {
			//update the taskbar
			TaskBar.windowClosed(this);
		}
		
		super.close();
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length != 1) return;
		if (!(args[0] instanceof String)) return;
		
		String msg = (String) args[0];
		if (msg.toLowerCase().equals("reload")) {
			boolean any = false;
			
			for (var o : getAllChildren()) {
				if (o.hasFocus()) {
					any = true;
					break;
				}
			}
			
			reInitChildren();
			
			if (getMaximizedPosition() != ScreenLocation.OUT) {
				maximize();
			}
			
			if (any) requestFocus();
		}
		else {
			super.sendArgs(args);
		}
	}
	
	@Override public boolean isPinned() { return pinned; }
	@Override public boolean isMaximized() { return maximized != ScreenLocation.OUT; }
	@Override public boolean isMinimized() { return minimized; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public boolean isMaximizable() { return maximizable; }
	@Override public boolean isMinimizable() { return minimizable; }
	@Override public void setPinned(boolean val) { pinned = val; }
	@Override public void setMaximized(ScreenLocation val) { if (maximizable) maximized = val; }
	@Override public void setMinimized(boolean val) { if (minimizable) minimized = val; }
	@Override public ScreenLocation getMaximizedPosition() { return maximized; }
	@Override public void setPinnable(boolean val) { pinnable = val; }
	@Override public void setMaximizable(boolean val) { maximizable = val; }
	@Override public void setMinimizable(boolean val) { minimizable = val; }
	@Override public void setDrawWhenMinimized(boolean val) { drawMinimized = val; }
	@Override public boolean drawsWhileMinimized() { return drawMinimized; }
	
	@Override
	public void maximize() {
		Dimension_d screen = getTopParent().getDimensions();
		boolean hasTaskBar = Envision.getTopScreen().getTaskBar() != null;
		
		double sw = screen.width;
		double sh = screen.height;
		double tb = TaskBar.drawSize() - 1;
		double hh = header.height;
		
		if (maximized == ScreenLocation.TOP) {
			if (hasTaskBar) setDimensions(0, hh + tb, sw, sh - (hh + tb));
			else setDimensions(0, hh, sw, sh - hh);
		}
		else if (maximized == ScreenLocation.LEFT) {
			if (hasTaskBar) setDimensions(0, hh + tb, sw / 2 + 1, sh - (hh + tb));
			else setDimensions(0, hh, sw / 2 + 1, sh - hh);
		}
		else if (maximized == ScreenLocation.RIGHT) {
			if (hasTaskBar) setDimensions(sw / 2, hh + tb, sw / 2, sh - (hh + tb));
			else setDimensions(sw / 2, hh, sw / 2, sh - hh);
		}
		else if (maximized == ScreenLocation.TOP_LEFT) {
			if (hasTaskBar) setDimensions(0, hh + tb, sw / 2 + 1, (sh / 2) - (hh + tb));
			else setDimensions(0, hh, sw / 2 + 1, (sh / 2) - hh);
		}
		else if (maximized == ScreenLocation.BOT_LEFT) {
			if (hasTaskBar) setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, ((sh - tb - (hh / 2)) / 2) - 2);
			else setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, (sh / 2) - hh + 2);
		}
		else if (maximized == ScreenLocation.TOP_RIGHT) {
			if (hasTaskBar) setDimensions(sw / 2, hh + tb, sw / 2, (sh / 2) - (hh + tb));
			else setDimensions(sw / 2, hh, sw / 2, (sh / 2) - hh);
		}
		else if (maximized == ScreenLocation.BOT_RIGHT) {
			if (hasTaskBar) setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, ((sh - tb - (hh / 2)) / 2) - 2);
			else setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, (sh / 2) - hh + 2);
		}
		
		reInitChildren();
	}
	
	@Override
	public void miniaturize() {
		setDimensions(getPreMax());
		
		//TaskBar bar = GameRenderer.instance.getTaskBar();
		//double tb = (bar != null) ? bar.height : 0;
		double tb = 0;
		
		Dimension_d dims = getDimensions();
		double headerHeight = hasHeader() ? getHeader().height : 0;
		double sX = dims.startX;
		double sY = dims.startY;
		double w = dims.width;
		double h = dims.height;
		
		sX = sX < 0 ? 4 : sX;
		sY = (sY - headerHeight) < 2 ? tb + 4 + headerHeight : sY;
		sX = sX + w > res.width ? -4 + sX - (sX + w - res.width) : sX;
		sY = sY + h > res.height ? -4 + sY - (sY + h - res.height) : sY;
		setDimensions(sX, sY, w, h);
		
		reInitChildren();
	}
	
	@Override public Dimension_d getPreMax() { return preMaxFull; }
	@Override public void setPreMax(Dimension_d dimIn) { preMaxFull = new Dimension_d(dimIn); }
	
	@Override public boolean isDevWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	@Override public boolean showInLists() { return true; }
	
	@Override
	public Stack<IWindowParent> getWindowHistory() {
		return WindowHistory;
	}
	
	@Override
	public void setWindowHistory(Stack<IWindowParent> historyIn) {
		WindowHistory = historyIn;
		if (header != null) header.updateButtonVisibility();
	}
	
	@Override public long getInitTime() { return initTime; }
	
	@Override
	public void drawHighlightBorder() {
		var header = getHeader();
		double sY = (header != null) ? header.startY : startY;
		drawHRect(startX - 1, sY - 1, endX + 1, endY + 1, 1, EColors.red);
	}
	
	@Override public boolean isHighlighted() { return highlighted; }
	@Override public void setHighlighted(boolean val) { highlighted = val; }
	
	@Override public EList<String> getAliases() { return aliases; }
	@Override public GameTexture getWindowIcon() { return windowIcon; }
	@Override public boolean showInTaskBar() { return showInTaskBar; }
	
	//--------
	// zLevel
	//--------
	
	@Override public int getZLevel() { return windowZLevel; }
	@Override public void setZLevel(int zLevelIn) { windowZLevel = zLevelIn; }
	@Override public void bringToFront() { EUtil.nullDo(getTopParent(), p -> p.bringObjectToFront(this)); }
	@Override public void sendToBack() { EUtil.nullDo(getTopParent(), p -> p.sendObjectToBack(this)); }
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void defaultDims() { setDimensions(startX, startY, defaultWidth, defaultHeight); }
	protected void defaultHeader() { setHeader(new WindowHeader(this)); }
	protected void defaultHeader(IWindowParent in) { setHeader(new WindowHeader(in)); }
	
	private void initDefaultPos(IWindowObject parentIn) {
		init(parentIn);
		windowInstance = this;
	}
	
	private void initDefaultDims(IWindowObject parentIn, int xPos, int yPos) {
		init(parentIn, xPos, yPos, defaultWidth, defaultHeight);
		windowInstance = this;
	}
	
	private void pullHistoryFrom(IWindowParent objectIn) {
		if (objectIn != null) {
			if (objectIn instanceof WindowParent wp) {
				WindowHistory = wp.getWindowHistory();
				WindowHistory.push(objectIn);
			}
		}
	}
	
	//---------
	// Methods
	//---------
	
	public void fileUpAndClose() {
		if (!WindowHistory.isEmpty() && WindowHistory.peek() != null) {
			var oldGuiPass = WindowHistory.pop();
			try {
				var newGui = oldGuiPass.getClass().getConstructor().newInstance();
				newGui.setWindowHistory(oldGuiPass.getWindowHistory());
				IWindowParent p = getTopParent().displayWindow(newGui, this, true, true, false, ObjectPosition.OBJECT_CENTER);
				p.setPinned(isPinned());
				if (isMaximized() && newGui.isMaximizable()) {
					newGui.setPreMax(getPreMax());
					newGui.setMaximized(getMaximizedPosition());
					newGui.maximize();
				}
				
				//THIS IS NOT RIGHT
				// V V V V V V V V
				
				getTopParent().setFocusedObject(p);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
	}
	
	public void drawDefaultBackground() {
		drawRect(0xff000000);
		drawRect(0xff383838, 1);
		drawRect(0xff3f3f3f, 2);
		drawRect(0xff424242, 3);
		//drawRect(0xffc6c6c6, 1);
		//drawRect(startX, startY, endX, endY, 0xff000000);
		//drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		//drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		//drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	public void enableHeader(boolean val) {
		if (header != null) header.setEnabled(val);
	}
	
	//---------
	// Getters
	//---------
	
	public boolean movesWithParent() { return moveWithParent; }
	@Override public long getWindowID() { return windowPID; }
	
	//---------
	// Setters
	//---------
	
	public void setDrawDefaultBackground(boolean val) { drawDefaultBackground = val; }
	public void setMoveWithParent(boolean val) { moveWithParent = val; }
	
	/** Returns true if this object has a header. */
    //@Override public boolean hasHeader() { return header != null; }
    /** If this object has a header, returns the header object, otherwise returns null. */
    //@Override public WindowHeader getHeader() { return header; }
	
	public WindowParent setHeader(WindowHeader headerIn) {
		if (header != null) removeObject(header);
		header = headerIn;
		//properties().objectHeader = header;
		if (header != null) header.updateButtonVisibility();
		addObject(header);
		return this;
	}
	
	public void showOnCurrent() { showOnCurrent(ObjectPosition.SCREEN_CENTER); }
	public void showOnCurrent(ObjectPosition position) {
	    Envision.getActiveTopParent().displayWindow(this, position);
	}
	
	public void showOnTop() { showOnTop(ObjectPosition.SCREEN_CENTER); }
	public void showOnTop(ObjectPosition position) { Envision.getTopScreen().displayWindow(this, position); }
	
}
