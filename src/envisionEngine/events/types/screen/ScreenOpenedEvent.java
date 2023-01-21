package envisionEngine.events.types.screen;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.gameSystems.screens.GameScreen;

public class ScreenOpenedEvent extends ScreenEvent {

	private final GameScreen screen;
	
	public ScreenOpenedEvent(GameScreen screenIn) {
		super(EventType.SCREEN_OPENED, true);
		screen = screenIn;
	}
	
	public GameScreen getScreen() { return screen; }
	
}
