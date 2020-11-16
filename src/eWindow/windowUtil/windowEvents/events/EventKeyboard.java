package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;
import eWindow.windowUtil.windowEvents.eventUtil.KeyboardType;

//Author: Hunter Bragg

public class EventKeyboard extends ObjectEvent {

	KeyboardType type;
	char eventChar;
	int eventKey;
	
	public EventKeyboard(IWindowObject parentIn, char charIn, int keyIn, KeyboardType typeIn) {
		super(parentIn, EventType.Keyboard);
		eventChar = charIn;
		eventKey = keyIn;
		type = typeIn;
	}
	
	public KeyboardType getKeyboardType() { return type; }
	public char getEventChar() { return eventChar; }
	public int getEventKey() { return eventKey; }
	
}
