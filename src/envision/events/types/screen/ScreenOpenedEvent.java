package envision.events.types.screen;

import envision.events.EventType;
import envision.game.screens.GameScreen;

public class ScreenOpenedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenOpenedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_OPENED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
