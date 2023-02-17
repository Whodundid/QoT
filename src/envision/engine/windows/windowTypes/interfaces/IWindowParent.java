package envision.engine.windows.windowTypes.interfaces;

import eutil.datatypes.EArrayList;
import eutil.math.dimensions.EDimension;
import eutil.misc.ScreenLocation;

import java.util.Stack;

import envision.engine.rendering.textureSystem.GameTexture;

//Author: Hunter Bragg

public interface IWindowParent<E> extends IWindowObject<E> {

	//------
	// Init
	//------
	
	/** Call to initialize the window. THIS SHOULD ONLY BE CALLED INTERNALLY! */
	public default void initWindow() {}
	
	//----------------
	// Drawing Checks
	//----------------
	
	/** Returns true if this object will draw even if minimized. */
	public boolean drawsWhileMinimized();
	/** Event fired when this window should draw its preview on taskbar hovers. */
	public default void renderTaskBarPreview(double xPos, double yPos) {}
	/** Event fired when this window should draw a border around it when it is highlighted. */
	public void drawHighlightBorder();
	/** Specifies whether or not this window's border should draw highlighted. */
	public void setHighlighted(boolean val);
	/** Returns true if this window's border will draw highlighted. */
	public boolean isHighlighted();
	/** Returns the icon that will be drawn to represent this window on the taskbar. */
	public default GameTexture getWindowIcon() { return null; }
	/** Specifies whether or not this window be displayed in the taskbar or not. */
	public default boolean showInTaskBar() { return true; }
	
	//-------------------
	// Size and Position
	//-------------------
	
	/** Returns true if this object will remain drawn when the top overlay is closed. */
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
	public void setPinned(boolean val);
	/** Sets this window to be maximized. */
	public void setMaximized(ScreenLocation position);
	/** Sets this window to be minimized. */
	public void setMinimized(boolean val);
	
	/** Returns the way this window is currently maximized. If the window is not maximized, this will return ScreenLocation.out. */
	public ScreenLocation getMaximizedPosition();
	
	/** Makes this window pin-able. */
	public void setPinnable(boolean val);
	/** Makes this window maximize-able. */
	public void setMaximizable(boolean val);
	/** Makes this window minimize-able. */
	public void setMinimizable(boolean val);
	/** Sets this object to draw even when minimized. Used for TaskBar previews. */
	public void setDrawWhenMinimized(boolean val);
	
	//--------
	// zLevel
	//--------
	
	/** Returns this object's Z level added on top of all of it's combined parent's Z levels.*/
	public int getZLevel();
	/** Sets this object's Z Level, this value is added to the combination of all of its parent's Z levels. */
	public void setZLevel(int zLevelIn);
	/** Signals the top parent to bring this object and it's children to the very front of the draw order on the next draw cycle. */
	public void bringToFront();
	/** Signals the top parent to bring this object and it's children to the very back of the draw order on the next draw cycle. */
	public void sendToBack();
	
	//--------------------
	// Maximize Functions
	//--------------------
	
	/** Runs functions to maximize the window to the currently specified maximized position. */
	public void maximize();
	/** Runs functions to minimize the window back from the currently specified maximized position. */
	public void miniaturize();
	
	/** Returns the original dimensions for this object before it was maximized. */
	public EDimension getPreMax();
	/** Specifies the dimensions this object had before it was maximized. */
	public void setPreMax(EDimension dimIn);
	
	//-------------------------
	// Special Argument Checks
	//-------------------------
	
	/** Returns true if this window should only be accessible when in dev mode. */
	public boolean isDevWindow();
	/** Returns true if this window should only be accessible when in debug mode. */
	public boolean isDebugWindow();
	/** Returns true if this window will be shown in the taskbar and the HUD right click menu. */
	public boolean showInLists();
	
	//----------------
	// Window History
	//----------------
	
	/** Returns the chain of windows that will be opened when pressing the 'back' or 'file up' button. */
	public Stack<IWindowParent<?>> getWindowHistory();
	/** Sets the chain of windows that will be opened when pressing the 'back' or 'file up' button. */
	public void setWindowHistory(Stack<IWindowParent<?>> historyIn);
	
	//----------------
	// Window Aliases
	//----------------
	
	/** Returns a list of Strings this window can be referenced as within the EMC Terminal. */
	public EArrayList<String> getAliases();
	
	//--------------
	// Window Debug
	//--------------
	
	/** Returns the time this window was created as a long. */
	public long getInitTime();
	
}
