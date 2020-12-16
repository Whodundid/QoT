package envisionEngine.eWindow.windowTypes.interfaces;

import java.util.Stack;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;

//Author: Hunter Bragg

public interface IWindowParent<E> extends IWindowObject<E> {

	//--------------
	//Drawing Checks
	//--------------
	
	/** Returns true if this object will draw even if minimized. */
	public boolean drawsWhileMinimized();
	/** Event fired when this window should draw its preview on taskbar hovers. */
	public void renderTaskBarPreview(double xPos, double yPos);
	/** Event fired when this window should draw a border around it when it is highlighted. */
	public void drawHighlightBorder();
	
	//-----------------
	//Size and Position
	//-----------------
	
	/** Returns true if this object will remain on the hud when a RendererProxyGui is closed. */
	public boolean isPinned();
	/** Returns true if this window is currently maximized. */
	public boolean isMaximized();
	/** Returns true if this window is currently minimized. */
	public boolean isMinimized();
	/** Returns true if this window can be pinned. */
	public boolean isPinnable();
	/** Returns true if this window can be maximized. */
	public boolean isMaximizable();
	/** Returns true if this window can be minimized. */
	public boolean isMinimizable();
	
	/** Sets this object to remain drawn on the renderer even when an IRendererProxy gui is displayed. */
	public IWindowParent<E> setPinned(boolean val);
	/** Sets ths window to be maximized. */
	public IWindowParent<E> setMaximized(ScreenLocation position);
	/** Sets this window to be minimized. */
	public IWindowParent<E> setMinimized(boolean val);
	
	/** Returns the way this window is currently maximized. If the window is not maximized, this will return ScreenLocation.out. */
	public ScreenLocation getMaximizedPosition();
	
	/** Makes this window pinnable. */
	public IWindowParent<E> setPinnable(boolean val);
	/** Makes this window maximizable. */
	public IWindowParent<E> setMaximizable(boolean val);
	/** Makes this window minimizeable. */
	public IWindowParent<E> setMinimizable(boolean val);
	/** Sets this object to draw even when minimized. Used for TaskBar previews. */
	public IWindowParent<E> setDrawWhenMinimized(boolean val);
	
	//------
	//zLevel
	//------
	
	/** Returns this object's Z level added on top of all of it's combined parent's Z levels.*/
	public int getZLevel();
	/** Sets this object's Z Level, this value is added to the combination of all of its parent's Z levels. */
	public IWindowParent<E> setZLevel(int zLevelIn);
	/** Signals the top parent to bring this object and it's children to the very front of the draw order on the next draw cycle. */
	public IWindowParent<E> bringToFront();
	/** Signals the top parent to bring this object and it's children to the very back of the draw order on the next draw cycle. */
	public IWindowParent<E> sendToBack();
	
	//------------------
	//Maximize Functions
	//------------------
	
	/** Runs functions to maximize the window to the currently specified maximized position. */
	public void maximize();
	/** Runs functions to minimize the window back from the currently specified maximized position. */
	public void miniaturize();
	
	/** Returns the original dimensions for this object before it was maximized. */
	public EDimension getPreMax();
	/** Specifies the dimensions this object had before it was maximized. */
	public IWindowParent<E> setPreMax(EDimension dimIn);
	
	//-----------------------
	//Special Argument Checks
	//-----------------------
	
	/** Returns true if this window should only be accessible when in dev mode. */
	public boolean isOpWindow();
	/** Returns true if this window should only be accessible when in debug mode. */
	public boolean isDebugWindow();
	/** Returns true if this window will be shown in the taskbar and the HUD right click menu. */
	public boolean showInLists();
	
	//--------------
	//Window History
	//--------------
	
	/** Returns the chain of windows that will be opened when pressing the 'back' or 'file up' button. */
	public Stack<IWindowParent<?>> getWindowHistory();
	/** Sets the chain of windows that will be opened when pressing the 'back' or 'file up' button. */
	public IWindowParent<E> setWindowHistory(Stack<IWindowParent<?>> historyIn);
	
	//--------------
	//Window Aliases
	//--------------
	
	/** Returns a list of Strings this window can be referenced as within the EMC Terminal. */
	public EArrayList<String> getAliases();
	
	//------------
	//Window Debug
	//------------
	
	/** Returns the time this window was created as a long. */
	public long getInitTime();
	
}
