package envision.windowLib.windowUtil.windowEvents.events;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.ObjectEvent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.KeyboardType;

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
