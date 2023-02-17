package envision.game.events.eventTypes.screen;

import envision.engine.screens.GameScreen;
import envision.game.events.EventType;

public class ScreenClosedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenClosedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_CLOSED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
