package engine.windowLib.windowTypes;

import engine.debug.DebugFunctions;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.renderEngine.textureSystem.GameTexture;
import engine.topOverlay.desktopOverlay.TaskBar;
import engine.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.misc.ScreenLocation;
import main.QoT;

import java.util.Stack;

//Author: Hunter Bragg

public class WindowParent<E> extends WindowObject<E> implements IWindowParent<E>, Comparable<WindowParent<?>> {

	public static int defaultWidth = 220, defaultHeight = 255;
	
	//--------
	// Fields
	//--------
	
	public WindowParent<E> windowInstance;
	protected WindowHeader<?> header;
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
	protected Stack<IWindowParent<?>> WindowHistory = new Stack();
	protected EArrayList<String> aliases = new EArrayList();
	protected GameTexture windowIcon = null;
	protected EDimension preMaxFull = new EDimension();
	protected EDimension preMaxSide = new EDimension();
	protected boolean showInTaskBar = true;
	protected long initTime = 0l;
	
	//--------------
	// Constructors
	//--------------
	
	/** By default, set the parent to the QoT top overlay. */
	public WindowParent() { this(QoT.getActiveTopParent(), null); }
	public WindowParent(IWindowParent<?> oldGuiIn) { this(QoT.getActiveTopParent(), oldGuiIn); }
	public WindowParent(int xPos, int yPos) { windowInstance = this; initTime = System.currentTimeMillis(); }
	public WindowParent(int xPos, int yPos, IWindowParent<?> oldGuiIn) { initTime = System.currentTimeMillis(); windowInstance = this; pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject<?> parentIn) { this(parentIn, null); }
	public WindowParent(IWindowObject<?> parentIn, IWindowParent<?> oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultPos(parentIn); pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject<?> parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public WindowParent(IWindowObject<?> parentIn, int xPos, int yPos, IWindowParent<?> oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultDims(parentIn, xPos, yPos); pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject<?> parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public WindowParent(IWindowObject<?> parentIn, int xIn, int yIn, int widthIn, int heightIn, IWindowParent<?> oldGuiIn) {
		initTime = System.currentTimeMillis();
		init(parentIn, xIn, yIn, widthIn, heightIn);
		pullHistoryFrom(oldGuiIn);
		windowInstance = this;
		preMaxFull = new EDimension(xIn, yIn, widthIn, heightIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public int compareTo(WindowParent<?> p) {
		return Long.compare(initTime, p.getInitTime());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawDefaultBackground) drawDefaultBackground();
		if (QoT.isDebugMode()) {
			if (!isMaximized()) {
				double y = hasHeader() ? getHeader().startY - 9 : startY - 9;
				int pos = 0;
				int half = -1;
				String draw = "";
				String time = String.valueOf(initTime);
				
				if (time.length() > 6) time = time.substring(time.length() - 6);
				
				if (DebugFunctions.drawWindowPID) {
					pos = getStringWidth("ID: " + EColors.yellow + getObjectID());
					draw = EColors.mc_aqua + "ID: " + EColors.yellow + getObjectID();
					
					if (DebugFunctions.drawWindowInit) {
						half = pos + 6;
						pos += getStringWidth(EColors.mc_aqua + "  InitTime: " + EColors.yellow + time);
						draw += EColors.mc_aqua + "  InitTime: " + EColors.yellow + time;
					}
				}
				else if (DebugFunctions.drawWindowInit) {
					pos = getStringWidth("InitTime: " + EColors.yellow + time);
					draw += "InitTime: " + EColors.yellow + time;
				}
				
				drawRect(startX, y - 1, startX + pos + 5, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
				drawRect(startX + 1, y, startX + pos + 4, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
				if (half > 0) drawRect(startX + half, y, startX + half + 1, y + FontRenderer.FONT_HEIGHT, EColors.black);
				drawString(draw, startX + 3, y + 1, EColors.cyan);
				
				if (DebugFunctions.drawWindowDimensions) {
					String dims = "[" + (int) startX + ", " + (int) startY + ", " + width + " " + height + "]";
					int eX = getStringWidth(dims);
					
					drawRect(startX + pos + 5, y - 1, startX + pos + eX + 9, y + FontRenderer.FONT_HEIGHT + 1, EColors.black);
					drawRect(startX + pos + 5, y, startX + pos + eX + 8, y + FontRenderer.FONT_HEIGHT, EColors.dgray);
					drawString(dims, startX + pos + 7, y + 1, EColors.seafoam);
				}
			}
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void close() {
		if (getTopParent() == QoT.getTopRenderer()) {
			//update the taskbar
			TaskBar.windowClosed(this);
			
			//get all active windows on the top renderer
			var windows = QoT.getTopRenderer().getAllActiveWindows();
			
			//check if this is the only window on the top renderer
			//if so, close the top renderer when closing the window
			if (windows.size() == 1 && windows.getFirst() == this) {
				QoT.getTopRenderer().setFocused(false);
			}
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
		EDimension screen = getTopParent().getDimensions();
		boolean hasTaskBar = QoT.getTopRenderer().getTaskBar() != null;
		
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
		
		EDimension dims = getDimensions();
		double headerHeight = hasHeader() ? getHeader().height : 0;
		double sX = dims.startX;
		double sY = dims.startY;
		double w = dims.width;
		double h = dims.height;
		
		sX = sX < 0 ? 4 : sX;
		sY = (sY - headerHeight) < 2 ? tb + 4 + headerHeight : sY;
		sX = sX + w > res.getWidth() ? -4 + sX - (sX + w - res.getWidth()) : sX;
		sY = sY + h > res.getHeight() ? -4 + sY - (sY + h - res.getHeight()) : sY;
		setDimensions(sX, sY, w, h);
		
		reInitChildren();
	}
	
	@Override public EDimension getPreMax() { return preMaxFull; }
	@Override public void setPreMax(EDimension dimIn) { preMaxFull = new EDimension(dimIn); }
	
	@Override public boolean isDevWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	@Override public boolean showInLists() { return true; }
	
	@Override
	public Stack<IWindowParent<?>> getWindowHistory() {
		return WindowHistory;
	}
	
	@Override
	public void setWindowHistory(Stack<IWindowParent<?>> historyIn) {
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
	
	@Override public EArrayList<String> getAliases() { return aliases; }
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
	protected void defaultHeader(IWindowParent<?> in) { setHeader(new WindowHeader(in)); }
	
	private void initDefaultPos(IWindowObject<?> parentIn) {
		init(parentIn);
		windowInstance = this;
	}
	
	private void initDefaultDims(IWindowObject<?> parentIn, int xPos, int yPos) {
		init(parentIn, xPos, yPos, defaultWidth, defaultHeight);
		windowInstance = this;
	}
	
	private void pullHistoryFrom(IWindowParent<?> objectIn) {
		if (objectIn != null) {
			if (objectIn instanceof WindowParent<?> wp) {
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
	
	//---------
	// Setters
	//---------
	
	public void setDrawDefaultBackground(boolean val) { drawDefaultBackground = val; }
	public void setMoveWithParent(boolean val) { moveWithParent = val; }
	
	public WindowParent<E> setHeader(WindowHeader<?> headerIn) {
		if (header != null) removeChild();
		header = headerIn;
		if (header != null) header.updateButtonVisibility();
		addChild(header);
		return this;
	}
	
}
