package windowLib.windowTypes;

import eutil.EUtil;
import eutil.colors.EColors;
import eutil.misc.ScreenLocation;
import eutil.storage.EArrayList;
import eutil.storage.EDims;
import java.util.Stack;
import main.QoT;
import renderEngine.textureSystem.GameTexture;
import windowLib.windowObjects.advancedObjects.header.WindowHeader;
import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowTypes.interfaces.IWindowParent;
import windowLib.windowUtil.ObjectPosition;

//Author: Hunter Bragg

public class WindowParent<E> extends WindowObject<E> implements IWindowParent<E>, Comparable<WindowParent<?>> {
	
	public static int defaultWidth = 220, defaultHeight = 255;
	
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
	protected Object oldObject = null;
	protected EArrayList<String> aliases = new EArrayList();
	protected GameTexture windowIcon = null;
	protected EDims preMaxFull = new EDims();
	protected EDims preMaxSide = new EDims();
	protected boolean showInTaskBar = true;
	protected long initTime = 0l;
	
	//---------------------------
	// WindowParent Constructors
	//---------------------------
	
	/** By default, set the parent to the EMC Renderer. */
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
		preMaxFull = new EDims(xIn, yIn, widthIn, heightIn);
	}
	
	//----------------------
	// Comparable Overrides
	//----------------------
	
	@Override public int compareTo(WindowParent<?> p) { return Long.compare(initTime, p.getInitTime()); }
	
	//-------------------------
	// IWindowObject Overrides
	//-------------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawDefaultBackground) { drawDefaultBackground(); }
		/*
		if (EnhancedMC.isDebugMode()) {
			if (!isMaximized()) {
				double y = hasHeader() ? getHeader().startY - 9 : startY - 9;
				int pos = 0;
				int half = -1;
				String draw = "";
				String time = String.valueOf(initTime);
				
				if (time.length() > 6) { time = time.substring(time.length() - 6); }
				
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
				
				drawRect(startX, y - 1, startX + pos + 5, y + FONT_HEIGHT + 1, EColors.black);
				drawRect(startX + 1, y, startX + pos + 4, y + FONT_HEIGHT, EColors.dgray);
				if (half > 0) { drawRect(startX + half, y, startX + half + 1, y + FONT_HEIGHT, EColors.black); }
				drawString(draw, startX + 3, y + 1, EColors.cyan);
				
				if (DebugFunctions.drawWindowDimensions) {
					String dims = "[" + (int) startX + ", " + (int) startY + ", " + width + " " + height + "]";
					int eX = getStringWidth(dims);
					
					drawRect(startX + pos + 5, y - 1, startX + pos + eX + 9, y + FONT_HEIGHT + 1, EColors.black);
					drawRect(startX + pos + 5, y, startX + pos + eX + 8, y + FONT_HEIGHT, EColors.dgray);
					drawString(dims, startX + pos + 7, y + 1, EColors.seafoam);
				}
			}
		}
		*/
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void close() {
		//MinecraftForge.EVENT_BUS.post(new WindowClosedEvent(this));
		super.close();
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1) {
			if (args[0] instanceof String) {
				String msg = (String) args[0];
				if (msg.equals("Reload")) {
					boolean any = false;
					
					for (IWindowObject<?> o : getAllChildren()) {
						if (o.hasFocus()) { any = true; break; }
					}
					
					reInitObjects();
					
					if (getMaximizedPosition() != ScreenLocation.OUT) {
						maximize();
					}
					
					if (any) { requestFocus(); }
				}
				else {
					super.sendArgs(args);
				}
			}
		}
	}
	
	//-------------------------
	// IWindowParent Overrides
	//-------------------------
	
	@Override public boolean isPinned() { return pinned; }
	@Override
	public boolean isMaximized() {
		return maximized == ScreenLocation.CENTER || maximized == ScreenLocation.LEFT || maximized == ScreenLocation.RIGHT ||
			   maximized == ScreenLocation.TOP_LEFT || maximized == ScreenLocation.TOP_RIGHT || maximized == ScreenLocation.BOT_LEFT || maximized == ScreenLocation.BOT_RIGHT;
	}
	@Override public boolean isMinimized() { return minimized; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public boolean isMaximizable() { return maximizable; }
	@Override public boolean isMinimizable() { return minimizable; }
	@Override public IWindowParent<E> setPinned(boolean val) { pinned = val; return this; }
	@Override public IWindowParent<E> setMaximized(ScreenLocation val) { if (maximizable) { maximized = val; } return this; }
	@Override public IWindowParent setMinimized(boolean val) { if (minimizable) { minimized = val; } return this; }
	@Override public ScreenLocation getMaximizedPosition() { return maximized; }
	@Override public IWindowParent<E> setPinnable(boolean val) { pinnable = val; return this; }
	@Override public IWindowParent<E> setMaximizable(boolean val) { maximizable = val; return this; }
	@Override public IWindowParent<E> setMinimizable(boolean val) { minimizable = val; return this; }
	@Override public IWindowParent<E> setDrawWhenMinimized(boolean val) { drawMinimized = val; return this; }
	@Override public boolean drawsWhileMinimized() { return drawMinimized; }
	
	@Override
	public void maximize() {
		EDims screen = getTopParent().getDimensions();
		//boolean hasTaskBar = EnhancedMC.getRenderer().getTaskBar() != null;
		boolean hasTaskBar = true;
		
		double sw = screen.width;
		double sh = screen.height;
		//double tb = TaskBar.drawSize - 1;
		double tb = 0;
		double hh = header.height;
		
		if (maximized == ScreenLocation.CENTER) {
			if (hasTaskBar) { setDimensions(0, hh + tb, sw, sh - (hh + tb)); }
			else { setDimensions(0, hh, sw, sh - hh); }
		}
		else if (maximized == ScreenLocation.LEFT) {
			if (hasTaskBar) { setDimensions(0, hh + tb, sw / 2 + 1, sh - (hh + tb)); }
			else { setDimensions(0, hh, sw / 2 + 1, sh - hh); }
		}
		else if (maximized == ScreenLocation.RIGHT) {
			if (hasTaskBar) { setDimensions(sw / 2, hh + tb, sw / 2, sh - (hh + tb)); }
			else { setDimensions(sw / 2, hh, sw / 2, sh - hh); }
		}
		else if (maximized == ScreenLocation.TOP_LEFT) {
			if (hasTaskBar) { setDimensions(0, hh + tb, sw / 2 + 1, (sh / 2) - (hh + tb)); }
			else { setDimensions(0, hh, sw / 2 + 1, (sh / 2) - hh); }
		}
		else if (maximized == ScreenLocation.BOT_LEFT) {
			if (hasTaskBar) { setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, ((sh - tb - (hh / 2)) / 2) - 2); }
			else { setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, (sh / 2) - hh + 2); }
		}
		else if (maximized == ScreenLocation.TOP_RIGHT) {
			if (hasTaskBar) { setDimensions(sw / 2, hh + tb, sw / 2, (sh / 2) - (hh + tb)); }
			else { setDimensions(sw / 2, hh, sw / 2, (sh / 2) - hh); }
		}
		else if (maximized == ScreenLocation.BOT_RIGHT) {
			if (hasTaskBar) { setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, ((sh - tb - (hh / 2)) / 2) - 2); }
			else { setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, (sh / 2) - hh + 2); }
		}
		
		reInitObjects();
	}
	
	@Override
	public void miniaturize() {
		setDimensions(getPreMax());
		
		//TaskBar bar = GameRenderer.instance.getTaskBar();
		//double tb = (bar != null) ? bar.height : 0;
		double tb = 0;
		
		EDims dims = getDimensions();
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
		
		reInitObjects();
	}
	
	@Override public EDims getPreMax() { return preMaxFull; }
	@Override public IWindowParent<E> setPreMax(EDims dimIn) { preMaxFull = new EDims(dimIn); return this; }
	
	@Override public boolean isOpWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	@Override public boolean showInLists() { return true; }
	
	@Override public Stack<IWindowParent<?>> getWindowHistory() { return WindowHistory; }
	
	@Override
	public WindowParent<E> setWindowHistory(Stack<IWindowParent<?>> historyIn) {
		WindowHistory = historyIn;
		if (header != null) { header.updateButtonVisibility(); }
		return this;
	}
	
	@Override public long getInitTime() { return initTime; }
	
	/** Not planned for 1.0 */
	@Override
	public void renderTaskBarPreview(double xPos, double yPos) {
		
	}
	
	@Override
	public void drawHighlightBorder() {
		WindowHeader<?> header = getHeader();
		double sY = (header != null) ? header.startY : startY;
		drawRect(startX - 1, sY - 1, endX + 1, endY + 1, EColors.red);
	}
	
	@Override public EArrayList<String> getAliases() { return aliases; }
	
	//--------
	// zLevel
	//--------
	
	@Override public int getZLevel() { return windowZLevel; }
	@Override public IWindowParent<E> setZLevel(int zLevelIn) { windowZLevel = zLevelIn; return this; }
	@Override public IWindowParent<E> bringToFront() { return EUtil.nullDoR(getTopParent(), p -> p.bringObjectToFront(this), this); }
	@Override public IWindowParent<E> sendToBack() { return EUtil.nullDoR(getTopParent(), p -> p.sendObjectToBack(this), this); }
	
	//----------------------
	// WindowParent Methods
	//----------------------
	
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
			oldObject = objectIn;
			if (objectIn instanceof WindowParent<?>) {
				WindowHistory = ((WindowParent<?>) objectIn).getWindowHistory();
				WindowHistory.push(objectIn);
			}
		}
	}
	
	@Override public void initWindow() {}
	
	protected void defaultDims() { setDimensions(startX, startY, defaultWidth, defaultHeight); }
	protected void defaultHeader(IWindowParent<?> in) { setHeader(new WindowHeader(in)); }
	
	public void fileUpAndClose() {
		if (!WindowHistory.isEmpty() && WindowHistory.peek() != null) {
			Object oldGuiPass = WindowHistory.pop();
			if (oldGuiPass instanceof WindowParent<?>) {
				try {
					WindowParent<?> newGui = ((WindowParent<?>) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
					newGui.setWindowHistory(((WindowParent<?>) oldGuiPass).getWindowHistory());
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
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		close();
	}
	
	public WindowParent<E> enableHeader(boolean val) {
		if (header != null) { header.setEnabled(val); }
		return this;
	}
	
	public void drawDefaultBackground() {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	public WindowParent<E> setHeader(WindowHeader<?> headerIn) {
		if (header != null) { removeObject(header); }
		header = headerIn;
		if (header != null) { header.updateButtonVisibility(); }
		addObject(headerIn);
		return this;
	}
	
	//----------------------
	// WindowParent Getters
	//----------------------
	
	public GameTexture getWindowIcon() { return windowIcon; }
	public WindowHeader<?> getHeader() { return header; }
	public boolean movesWithParent() { return moveWithParent; }
	public boolean showInTaskBar() { return showInTaskBar; }
	
	//----------------------
	// WindowParent Setters
	//----------------------	
	
	public WindowParent<E> setDrawDefaultBackground(boolean val) { drawDefaultBackground = val; return this; }
	public WindowParent<E> setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	
}
