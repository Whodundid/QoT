package envision.engine.events.eventTypes.screen;

import envision.engine.events.EnvisionEventType;
import envision.engine.loader.built.game.GameScreen;

public class ScreenClosedEvent extends ScreenEvent {

    private final GameScreen screen;
    
    public ScreenClosedEvent(GameScreen screenIn) {
        super(EnvisionEventType.SCREEN_CLOSED, true);
        screen = screenIn;
    }
    
    public GameScreen getScreen() { return screen; }
    
}
