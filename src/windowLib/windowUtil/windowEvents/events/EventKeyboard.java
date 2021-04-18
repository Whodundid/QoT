package windowLib.windowUtil.windowEvents.events;

import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.ObjectEvent;
import windowLib.windowUtil.windowEvents.eventUtil.EventType;
import windowLib.windowUtil.windowEvents.eventUtil.KeyboardType;

//Author: Hunter Bragg

public class EventKeyboard extends ObjectEvent {

	KeyboardType type;
	char eventChar;
	int eventKey;
	
	public EventKeyboard(IWindowObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
		super(parentIn, EventType.KEYBOARD);
		eventChar = charIn;
		eventKey = keyIn;
		type = typeIn;
	}
	
	public KeyboardType getKeyboardType() { return type; }
	public char getEventChar() { return eventChar; }
	public int getEventKey() { return eventKey; }
	
}
