package envision.engine.events.eventTypes.screen;

import envision.engine.events.EventType;
import envision.engine.screens.GameScreen;

public class ScreenOpenedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenOpenedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_OPENED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}