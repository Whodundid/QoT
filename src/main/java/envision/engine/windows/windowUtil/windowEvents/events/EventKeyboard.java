package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.KeyboardType;

//Author: Hunter Bragg

public class EventKeyboard extends ObjectEvent {

	//--------
	// Fields
	//--------
	
	private final KeyboardType type;
	private final char eventChar;
	private final int eventKey;
	
	//--------------
	// Constructors
	//--------------
	
	public EventKeyboard(IWindowObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
		super(parentIn, EventType.KEYBOARD);
		eventChar = charIn;
		eventKey = keyIn;
		type = typeIn;
	}
	
	//---------
	// Getters
	//---------
	
	public KeyboardType getKeyboardType() { return type; }
	public char getEventChar() { return eventChar; }
	public int getEventKey() { return eventKey; }
	
}
