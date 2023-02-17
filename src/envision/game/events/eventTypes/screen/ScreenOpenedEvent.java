package envision.game.events.eventTypes.screen;

import envision.engine.screens.GameScreen;
import envision.game.events.EventType;

public class ScreenOpenedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenOpenedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_OPENED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
