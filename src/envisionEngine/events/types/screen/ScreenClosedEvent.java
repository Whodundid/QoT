package envisionEngine.events.types.screen;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.gameSystems.screens.GameScreen;

public class ScreenClosedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenClosedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_CLOSED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
