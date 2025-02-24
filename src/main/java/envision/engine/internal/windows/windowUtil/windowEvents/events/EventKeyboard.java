package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.KeyboardType;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

public class EventKeyboard extends WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    private final KeyboardType type;
    private final char eventChar;
    private final int eventKey;    
    //==============
    // Constructors
    //==============
    
    public EventKeyboard(IWindowObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
        super(parentIn, WindowEventType.KEYBOARD, true);
        eventChar = charIn;
        eventKey = keyIn;
        type = typeIn;
    }    
    //=========
    // Getters
    //=========
    
    public KeyboardType getKeyboardType() { return type; }
    public char getEventChar() { return eventChar; }
    public int getEventKey() { return eventKey; }
    
}
