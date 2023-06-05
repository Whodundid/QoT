package envision.engine.events.eventTypes.screen;

import envision.engine.events.EventType;
import envision.engine.screens.GameScreen;

public class ScreenClosedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenClosedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_CLOSED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
