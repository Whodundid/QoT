package envision.engine.windows.windowTypes;

import java.util.Stack;

import envision.engine.registry.types.Sprite;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.ScreenLocation;

public class WindowParentProperties extends WindowObjectProperties {
    
    //========
    // Fields
    //========
    
    /** The single WindowParent instance for which these properties pertain to. */
    public final WindowParent windowInstance;
    public final long initTime;
    public final int windowPid;
    
    public int windowZLevel = 0;
    public boolean movesWithParent = false;
    public boolean pinned = false;
    public boolean pinnable = false;
    public ScreenLocation maximized = ScreenLocation.OUT;
    public boolean minimizable = true;
    public boolean minimized = false;
    public boolean maximizable = false;
    public boolean drawMinimized = false;
    public boolean highlighted = false;
    public Stack<IWindowParent> windowHistory = new Stack<>();
    public EList<String> aliases = EList.newList();
    public Sprite windowIcon = null;
    public Dimension_d preMaxFull = new Dimension_d();
    public Dimension_d preMaxSide = new Dimension_d();
    public boolean showInTaskBar = true;
    
    //==============
    // Constructors
    //==============
    
    WindowParentProperties(WindowParent instanceIn) {
        super(instanceIn);
        
        windowInstance = instanceIn;
        initTime = System.currentTimeMillis();
        windowPid = getNextWindowPID();
    }
    
    //================
    // Static Methods
    //================
    
    private static volatile int curWindowID = 0;
    
    /** Returns the next available id that will be assigned to a requesting object. */
    public static synchronized int getNextWindowPID() { return curWindowID++; }
    
}
