package envision.engine.events.eventTypes.screen;

import envision.engine.events.EnvisionEventType;
import envision.engine.loader.built.game.GameScreen;

public class ScreenOpenedEvent extends ScreenEvent {

    private final GameScreen screen;
    
    public ScreenOpenedEvent(GameScreen screenIn) {
        super(EnvisionEventType.SCREEN_OPENED, true);
        screen = screenIn;
    }
    
    public GameScreen getScreen() { return screen; }
    
}
