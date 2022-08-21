package envision.windowLib.windowTypes;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.EObjectGroup;
import envision.windowLib.windowUtil.FutureTaskManager;
import envision.windowLib.windowUtil.windowEvents.ObjectEventHandler;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;

/**
 * The set of all properties that each and every WindowObject have.
 * 
 * @author Hunter Bragg
 * 
 * @param <E> A custom type that this object can store for later use
 */
public class WindowObjectProperties<E> {
	
	//--------
	// Fields
	//--------
	
	/** The single WindowObject instance for which these properties pertain to. */
	public final WindowObject<E> instance;
	
	/** The event handler for this object. */
	public ObjectEventHandler eventHandler;
	/** The future task manager of this object. */
	public FutureTaskManager futureTaskManager;
	
	/** The immediate parent of this object. */
	public IWindowObject<?> parent;
	/** The object that will receive focus once this object is closed. */
	public IWindowObject<?> focusObjectOnClose;
	/** The object that will receive focus once this object is fully added to its parent. */
	public IWindowObject<?> defaultFocusObject;
	
	/** The current children on this object. */
	public EArrayList<IWindowObject<?>> children = new EArrayList();
	/** The children that will be removed on the next draw cycle. */
	public EArrayList<IWindowObject<?>> childrenToBeRemoved = new EArrayList();
	/** The children that will be added on the next draw cycle. */
	public EArrayList<IWindowObject<?>> childrenToBeAdded = new EArrayList();
	
	/** Specifies a region for which this object is interactably restricted by beyond its normal dimensions. */
	public EDimension boundaryDimension;
	
	/** The text that is drawn is the mouse is hovering over this object. */
	public String hoverText = null;
	/** The color of the hover text. */
	public int hoverTextColor = EColors.lime.intVal;
	
	/** Internal boolean to keep track of whether or not the mouse has entered this object. */
	public boolean mouseEntered = false;
	/** Specifies whether or not this object should be closed if this object's top parent is hidden. */
	public boolean closesWithHud = false;
	
	/** The object group that this object belongs to (if there is one). */
	public EObjectGroup objectGroup;
	
	//---------------------
	// Fields : Object IDs
	//---------------------
	
	/** The display name of this object. If a IWindowParent, this name is displayed on the window's header. */
	public String objectName = "noname";
	/** The ID of this object. */
	public final long objectId;
	
	//----------------------------------
	// Fields : Basic Object Properties
	//----------------------------------
	
	/** True if this object is interact-able or not. */
	public boolean isEnabled = true;
	/** Specifies whether or not this object should be drawn. */
	public boolean isVisible = true;
	/** Specifies whether or not this object should be drawn. */
	public boolean isHidden = false;
	/** Sets this object to be drawn regardless of visible or not. */
	public boolean isAlwaysVisible = false;
	/** Sets this object to be drawn on top of every other object (that isn't also specifying this setting). */
	public boolean isAlwaysOnTop = false;
	/** Specifies whether or not this object can be resized. */
	public boolean isResizeable = false;
	/** Specifies whether or not this object can be moved from its original position. */
	public boolean isMoveable = true;
	/** Specifies whether or not this object can be clicked on by the mouse. */
	public boolean isClickable = true;
	/** Specifies whether or not this object can actually be closed. */
	public boolean isClosable = true;
	
	//--------------------------------
	// Fields : Tracked Object States
	//--------------------------------
	
	/** Tracked state of whether or not this object has been initialized. */
	public volatile boolean isInit = false;
	/** Tracked state of whether or not this object has had its children initialized. */
	public volatile boolean isChildInit = false;
	/** Tracked state of whether or not this object has been drawn at least once. */
	public volatile boolean hasFirstDraw = false;
	/** Tracked state of whether or not this object has received focus at least once. */
	public volatile boolean hasReceivedFocus = false;
	/** Tracked state of whether or not this object is currently being added to some parent object. */
	public volatile boolean isBeingAdded = false;
	/** Tracked state of whether or not this object has been fully added to its parent. */
	public volatile boolean isAdded = false;
	/** Tracked state of whether or not this object is currently being removed from its current parent object. */
	public volatile boolean isBeingRemoved = false;
	/** Tracked state of whether or not this object has been fully removed from its most recent parent object. */
	public volatile boolean isRemoved = false;
	/** Tracked state of whether or not this object is in the process of being closed. */
	public volatile boolean isClosing = false;
	/** Tracked state of whether or not this object has been closed. */
	public volatile boolean isClosed = false;
	
	//------------------------
	// Fields : Stored Object
	//------------------------
	
	/** An internally stored object of a specific type for customized functionality. */
	public E genericObject = null;
	
	//--------------
	// Constructors
	//--------------
	
	WindowObjectProperties(WindowObject<E> instanceIn) {
		instance = instanceIn;
		objectId = getNextPID();
		
		eventHandler = new ObjectEventHandler(instanceIn);
		futureTaskManager = new FutureTaskManager(instanceIn);
	}
	
	//----------------
	// Static Methods
	//----------------
	
	private static volatile int curObjectID = 0;
	
	/** Returns the next available id that will be assigned to a requesting object. */
	public static synchronized int getNextPID() { return curObjectID++; }
	
}
